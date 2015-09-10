(ns totalisator.service.team-service
  (:require [totalisator.repository.queries :as q]
            [schema.core :as s])
  (:import (java.math RoundingMode)))

(defn round-money [money]
  (.setScale (bigdec money) 2 RoundingMode/HALF_DOWN))

;(odds 10 2) => 5
;(odds 19 0) => 0
;Hint with-precision in case of ArithmeticException
(defn odds [money-pool waggered]
  "Calculates odds based on formula odds = money-pool / waggered. If waggered is 0 odds should be 0."
  (with-precision 10
    (if (> waggered 0)
      (/ money-pool waggered)
      0)))

;(sum-bet-amounts [{:amount 2} {:amount 4}]) => 6
(defn sum-bet-amounts [bets]
  "Given bets containing :amount keys sums them up"
  nil)

;Using above functions calculate odds for given team
;You need to sum up all team bet amounts and caclucate odds based on formula
;(calculate-team-odds 30 {:id 1 :bets [{:amount 5.0} {:amount 15}]})
; => 1.5
(defn calculate-team-odds [money-pool team]
  nil)

;you should collect all :bets from each team
(defn all-winner-bets [teams]
  "Given a list of teams produces list of all bets from all teams"
  nil)

;You should calculate money pool - amount of all bet money
;Then for each team you should calculate odds for that team and save them in :odds key
(defn calculate-odds [teams]
  "Calculates odds for all teams and places them under :odds key"
  (let [money-pool "calculate money pool"]
    "cacluate odds per team and assoc them into :odds"))

;Schema for result of get-teams function
(s/defschema Team {:id s/Int
                   :name String
                   :totalisator-id s/Int
                   :created-by s/Int
                   :odds s/Num})

(defn ->team [team]
  "Constructs team response with appropriate information"
  (select-keys team [:id :name :totalisator-id :created-by :odds]))

;Using above functions retrieve all teams for totalisator from db and enrich them with calculated odds
;Result should be sequence of Team maps. Remember q/get-teams-with-bets?
(s/defn ^:always-validate get-teams :- [Team]
  [totalisator-id :- s/Int]
  "hi")

(s/defschema Payout
  {:username String
   :payout s/Num
   :invested-amount-on-winner s/Num
   :invested-amount s/Num
   :profit s/Num})


(defn calculate-payout [odds invested-amount invested-on-winner]
  (round-money
    (if (= 0M (bigdec odds))
      invested-amount
      (with-precision 10 (* odds invested-on-winner)))))

;Given list of teams and id of team find that team or nil
(defn find-team [team-id teams]
  (first (filter #(= (:id %) team-id) teams)))

;
(defn partition-bets-per-user [teams]
  (vals (group-by :username (all-winner-bets teams))))

(defn ->user-payout [winner-team user-bets]
  (let [username (:username (first user-bets))
        invested-amount (sum-bet-amounts user-bets)
        bets-on-winner (filter #(= (:totalisator-team-id %) (:id winner-team)) user-bets)
        invested-on-winner (sum-bet-amounts bets-on-winner)
        payout (calculate-payout (:odds winner-team) invested-amount invested-on-winner)]
    {:username                  username
     :payout                    payout
     :invested-amount-on-winner invested-on-winner
     :invested-amount           invested-amount
     :profit                    (- payout invested-amount)}))

(s/defn ^:always-validate get-payouts :- [Payout]
  [totalisator-id :- s/Int winner-team-id :- s/Int]
  (let [teams (calculate-odds (q/get-teams-with-bets totalisator-id))
        winner-team (find-team winner-team-id teams)
        bets-per-user (partition-bets-per-user teams)
        payouts (map (partial ->user-payout winner-team) bets-per-user)]
    (reverse (sort-by :payout payouts))))

(s/defschema New-Team {:name String :current-user-id s/Int})
(s/defn ^:always-validate save-team! :- Team
  [team :- New-Team totalisator-id :- s/Int]
  (q/insert-or-update! q/save-team<! q/update-team!
    (-> team
      (clojure.set/rename-keys {:current-user-id :created-by})
      (assoc :totalisator-id totalisator-id)
      (assoc :odds 0))))


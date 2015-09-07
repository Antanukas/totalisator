(ns totalisator.service.team-service
  (:require [totalisator.repository.queries :as q]
            [schema.core :as s])
  (:import (java.math RoundingMode)))

(defn- round-money [money]
  (.setScale (bigdec money) 2 RoundingMode/HALF_DOWN))

(defn- odds [money-pool waggered]
  (with-precision 10
    (if (> waggered 0)
      (/ money-pool waggered)
      0)))

(defn- sum-bet-amounts [bets]
  (reduce + (map :amount bets)))

(defn- calculate-team-odds [money-pool team]
  (odds money-pool (sum-bet-amounts (:bets team))))

(defn- all-winner-bets [teams]
  (flatten (map :bets teams)))

(defn- calculate-odds [teams]
  (let [money-pool (sum-bet-amounts (all-winner-bets teams))]
    (map #(assoc % :odds (calculate-team-odds money-pool %)) teams)))

; Public functions

(s/defschema New-Team {:name String :current-user-id s/Int})
(s/defschema Team {:id s/Int
                   :name String
                   :totalisator-id s/Int
                   :created-by s/Int
                   (s/optional-key :odds) s/Num})
(s/defschema Teams [Team])
(s/defschema Payout
  {:username String
   :payout s/Num
   :invested-amount-on-winner s/Num
   :invested-amount s/Num
   :profit s/Num})

(s/defn ^:always-validate save-team! :- Team
  [team :- New-Team totalisator-id :- s/Int]
  (q/insert-or-update! q/save-team<! q/update-team!
    (-> team
      (clojure.set/rename-keys {:current-user-id :created-by})
      (assoc :totalisator-id totalisator-id))))

(defn ->team [team]
  (select-keys team [:id :name :totalisator-id :created-by :odds]))

(s/defn ^:always-validate get-teams :- Teams
  [totalisator-id :- s/Int]
  (map ->team (calculate-odds (q/get-teams-with-bets totalisator-id))))

(defn- calculate-payout [odds invested-amount invested-on-winner]
  (round-money
    (if (= 0M (bigdec odds))
      invested-amount
      (* odds invested-on-winner))))

(s/defn ^:always-validate get-payouts :- [Payout]
  [totalisator-id :- s/Int winner-team-id :- s/Int]
  (with-precision 10
    (let [teams (calculate-odds (q/get-teams-with-bets totalisator-id))
          winner-team (first (filter #(= (:id %) winner-team-id) teams))
          bets-per-user (vals (group-by :username (all-winner-bets teams)))

          ->user-payout (fn [winner-team user-bets]
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


          payouts (map (partial ->user-payout winner-team) bets-per-user)]
      (reverse (sort-by :payout payouts)))))

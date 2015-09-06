(ns totalisator.service.team-service
  (:require [totalisator.repository.queries :as q]
            [schema.core :as s])
  (:import (java.math RoundingMode)))

(defn- map-values [f m]
  "Applies f to each value in the map and returns new map"
  (into {} (for [[k v] m] [k (f v)])))

(defn- round-money [money]
  (.setScale (bigdec money) 2 RoundingMode/HALF_DOWN))

(defn- odds-fn [money-pool]
  (fn [runner-money] (/ money-pool runner-money)))

(defn- sum-bet-amounts [bets]
  (reduce + (map :amount bets)))

(defn- get-odds-per-team [winner-bets]
  (let [team-bets (group-by :totalisator-team-id winner-bets)
        wagered-per-team (map-values sum-bet-amounts team-bets)
        total-money-pool (sum-bet-amounts winner-bets)
        odds-per-team (map-values (odds-fn total-money-pool) wagered-per-team)]
    odds-per-team))

(defn- get-winner-bets [totalisator-id]
  (q/query q/get-winner-bets-by-totalisator-id {:totalisator-id totalisator-id}))

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

(s/defn ^:always-validate save-team! :- Team [team :- New-Team totalisator-id :- s/Int]
  (q/insert-or-update! q/save-team<! q/update-team!
    (-> team
      (clojure.set/rename-keys {:current-user-id :created-by})
      (assoc :totalisator-id totalisator-id))))

(s/defn ^:always-validate get-teams :- Teams [totalisator-id :- s/Int]
  (with-precision 10
    (let [teams (q/query q/get-teams {:totalisator-id totalisator-id})
          odds-per-team (get-odds-per-team (get-winner-bets totalisator-id))
          assoc-odds-fn (fn [m odds] (if odds (assoc m :odds odds) m))]
      (map #(assoc-odds % (get odds-per-team (:id %))) teams))))

(s/defn ^:always-validate get-payouts :- [Payout]
  [totalisator-id :- s/Int winner-team-id :- s/Int]
  (with-precision 10
    (let [winner-bets (get-winner-bets totalisator-id)
          bets-per-user (partition-by :username winner-bets)
          winner-odds (get (get-odds-per-team winner-bets) winner-team-id)

          ->payout (fn [winner-team-id odds user-bets]
                     (let [username (:username (first user-bets))
                           invested-amount (sum-bet-amounts user-bets)
                           bets-on-winner (filter #(= (:totalisator-team-id %) winner-team-id) user-bets)
                           invested-on-winner (sum-bet-amounts bets-on-winner)
                           payout (round-money (* odds invested-on-winner))]
                       {:username                  username
                        :payout                    payout
                        :invested-amount-on-winner invested-on-winner
                        :invested-amount           invested-amount
                        :profit                    (- payout invested-amount)}))

          payouts (map (partial ->payout winner-team-id winner-odds) bets-per-user)]
      (reverse (sort-by :payout payouts)))))

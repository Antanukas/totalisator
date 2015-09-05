(ns totalisator.service.team-service
  (:require [totalisator.repository.queries :as q])
  (:import (java.math RoundingMode)))

(defn- map-values [f m]
  "Applies f to each value in the map and returns new map"
  (into {} (for [[k v] m] [k (f v)])))

(defn- round-money [num]
  (.setScale (bigdec num) 2 RoundingMode/HALF_DOWN))

(defn- get-odds-per-team [winner-bets]
  (let [team-bets (group-by :totalisator-team-id winner-bets)
        wagered-per-team (map-values #(reduce + (map :amount %)) team-bets)
        total-money-pool (reduce + (vals wagered-per-team))
        odds-per-team (map-values #(/ total-money-pool %) wagered-per-team)]
    odds-per-team))

(defn- sum-bet-amounts [bets]
  (reduce + (map :amount bets)))

(defn- get-winner-bets [totalisator-id]
  (q/query q/get-winner-bets-by-totalisator-id {:totalisator-id totalisator-id}))

; Public functions

(defn save-team! [team totalisator-id]
  (q/insert-or-update! q/save-team<! q/update-team! (assoc team :totalisator-id totalisator-id)))

(defn get-teams [totalisator-id]
  (with-precision 10
    (let [teams (q/query q/get-teams {:totalisator-id totalisator-id})
          ->team (fn [team odds] (assoc team :odds odds))
          odds-per-team (get-odds-per-team (get-winner-bets totalisator-id))]
      (map (fn [team] (->team team (get odds-per-team (:id team)))) teams))))

(defn get-payouts [totalisator-id winner-team-id]
  (with-precision 10
    (let [winner-bets (get-winner-bets totalisator-id)
          bets-per-user (vals (group-by :username winner-bets))
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

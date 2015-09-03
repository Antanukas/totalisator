(ns totalisator.service.team-service
  (:require [totalisator.repository.queries :as q]))

(defn save-team! [team totalisator-id]
  (q/insert-or-update! q/save-team<! q/update-team! (assoc team :totalisator-id totalisator-id)))

(defn- map-values [f m]
  "Applies f to each value in the map and returns new map"
  (into {} (for [[k v] m] [k (f v)])))

(defn- get-odds-per-team [winner-bets]
  (let [team-bets (group-by :totalisator-team-id winner-bets)
        wagered-per-team (map-values #(reduce + (map :amount %)) team-bets)
        total-money-pool (reduce + (vals wagered-per-team))
        odds-per-team (map-values #(/ total-money-pool %) wagered-per-team)]
    odds-per-team))

(defn get-teams [totalisator-id]
  (with-precision 5
    (let [teams (q/query q/get-teams {:totalisator-id totalisator-id})
          winner-bets (q/query q/get-winner-bets-by-totalisator-id {:totalisator-id totalisator-id})
          odds-per-team (get-odds-per-team winner-bets)
          teams-with-odds (map #(assoc % :odds (get odds-per-team (:id %))) teams)]
      teams-with-odds)))

(defn get-leaderboard [totalisator-id winner-team-id]
  (with-precision 5
    (let [winner-bets (q/query q/get-winner-bets-by-totalisator-id {:totalisator-id totalisator-id})
          winner-odds (get (get-odds-per-team winner-bets) winner-team-id)
          won-bets (filter #(= (:totalisator-team-id %) winner-team-id) winner-bets)
          bets-per-user (group-by :username won-bets)
          winnings-per-user (map-values #(* winner-odds (reduce + (map :amount %))) bets-per-user)
          sorted-winnigs-per-user (sort-by :winnings (for [[username winnings] winnings-per-user] {:username username :winnings winnings}))]
      sorted-winnigs-per-user)))

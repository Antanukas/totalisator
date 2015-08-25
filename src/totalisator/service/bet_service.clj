(ns totalisator.service.bet-service
  (:require [totalisator.repository.queries :as q]))


(defn new-winner-bet [bet team-id]
  (q/insert<! q/new-winner-bet<! (assoc bet :totalisator_team_id team-id)))

(defn new-match-bet [bet]
  (q/insert<! q/new-match-bet<! bet))

(defn get-team-bets [user-id]
  (q/query q/get-winner-bets-by-id {:current-user-id user-id}))

(defn get-match-bets [user-id]
  (q/query q/get-winner-bets-by-id {:current-user-id user-id}))
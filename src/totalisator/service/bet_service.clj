(ns totalisator.service.bet-service
  (:require [totalisator.repository.queries :as q]))


(defn new-winner-bet [bet totalisator-id team-id]
  (if (q/query-single q/get-totalisator-by-id {:id totalisator-id})
    (q/insert<! q/new-winner-bet<! (assoc bet :totalisator-team-id team-id))
    (throw (ex-info (str "Totalisator with id " totalisator-id " not found") {:type :data-not-found}))))

(defn new-match-bet [bet]
  (q/insert<! q/new-match-bet<! bet))

(defn get-team-bets [user-id]
  (q/query q/get-winner-bets-by-id {:current-user-id user-id}))

(defn get-match-bets [user-id]
  (q/query q/get-winner-bets-by-id {:current-user-id user-id}))
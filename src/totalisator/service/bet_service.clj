(ns totalisator.service.bet-service
  (:require [totalisator.repository.queries :as q]))


(defn new-winner-bet [bet totalisator-id team-id]
  (if (q/query-single q/get-totalisator-by-id {:id totalisator-id})
    (q/insert<! q/new-winner-bet<! (assoc bet :totalisator-team-id team-id))
    (throw (ex-info (str "Totalisator with id " totalisator-id " not found") {:type :data-not-found}))))
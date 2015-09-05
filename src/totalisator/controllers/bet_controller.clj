(ns totalisator.controllers.bet-controller
  (:require [totalisator.service.bet-service :as bets]
            [totalisator.controllers.utils :as utl]
            [schema.core :as s]))

(defn create-bets [totalisator-id team-id bets]
  (println (class (:amount (first bets))))
  (utl/batch-response bets bets/new-winner-bet totalisator-id team-id))

(ns totalisator.service.bet-service
  (:require [totalisator.repository.queries :as q]
            [schema.core :as s]
            [totalisator.service.exceptions :as ex]))

(s/defschema New-Winner-Bet
  {:current-user-id Long
   :amount s/Num})

(s/defschema Winner-Bet
  { :id Long
    :amount s/Num
    :totalisator-team-id Long})

;Hints: q/get-totalisator-by-id, q/new-winner-bet<!
(s/defn ^:always-validate new-winner-bet :- Winner-Bet
  [bet :- New-Winner-Bet totalisator-id :- s/Int team-id :- s/Int]
  "Given new bet persists it to database. Throws data not found exception when totalisator with given id doesn't exist"
  (if (q/query-single q/get-totalisator-by-id {:id totalisator-id})
    (q/insert<! q/new-winner-bet<! (assoc bet :totalisator-team-id team-id))
    (ex/throw-data-not-found (str "Totalisator with id " totalisator-id " not found"))))
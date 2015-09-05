(ns totalisator.service.bet-service
  (:require [totalisator.repository.queries :as q]
            [schema.core :as s]))

(s/defschema New-Winner-Bet
  {:current-user-id Long
   :amount s/Num})

(s/defschema Winner-Bet
  { :id Long
    :amount s/Num
    :totalisator-team-id Long})

(s/defn ^:always-validate new-winner-bet :- Winner-Bet
  [bet :- New-Winner-Bet totalisator-id :- Long team-id :- Long]
  (if (q/query-single q/get-totalisator-by-id {:id totalisator-id})
    (q/insert<! q/new-winner-bet<! (assoc bet :totalisator-team-id team-id))
    (throw (ex-info (str "Totalisator with id " totalisator-id " not found") {:type :data-not-found}))))
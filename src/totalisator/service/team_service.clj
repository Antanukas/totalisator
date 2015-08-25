(ns totalisator.service.team-service
  (:require [totalisator.repository.queries :as q]))

(defn save-team! [team totalisator-id]
  (q/insert-or-update! q/save-team<! q/update-team! (assoc team :totalisator-id totalisator-id)))

(defn get-teams [totalisator-id]
  (q/query q/get-teams {:totalisator-id totalisator-id}))

(defn get-team [totalisator-id team-id]
  (q/query q/get-team {:totalisator-id totalisator-id :team-id team-id}))

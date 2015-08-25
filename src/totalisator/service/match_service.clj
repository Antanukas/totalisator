(ns totalisator.service.match-service
  (:require [totalisator.repository.queries :as q]))

(defn save-match! [match totalisator-id]
  (q/insert-or-update! q/save-match<! q/update-match! (assoc match :totalisator-id totalisator-id)))

(defn get-matches [totalisator-id]
  (q/query q/get-matches {:totalisator-id totalisator-id}))

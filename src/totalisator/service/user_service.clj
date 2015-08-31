(ns totalisator.service.user-service
  (:require [totalisator.repository.queries :as q]))

(defn get-users [] (q/query q/get-users))
(defn save-user! [user]
  (if-let [existing-user (q/query-single q/get-user-by-name user)]
    existing-user
    (q/insert-or-update! q/save-user<! q/update-user<! user)))

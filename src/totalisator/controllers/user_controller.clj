(ns totalisator.controllers.user-controller
  (:require [totalisator.repository.queries :as q]))

(defn get-users [] (q/query q/users))
(defn save-user! [user]
  (let [{:keys [name]} user
        new-user-id (q/insert<! q/save-user<! name)]
    (assoc user :id new-user-id)))

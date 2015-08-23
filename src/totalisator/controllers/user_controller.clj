(ns totalisator.controllers.user-controller
  (:require [totalisator.repository.queries :as q]))

(defn get-users [] (q/query q/users))
(defn save-user! [user] (q/insert-or-update! q/save-user<! q/update-user<! user))

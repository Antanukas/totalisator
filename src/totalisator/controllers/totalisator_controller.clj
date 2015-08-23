(ns totalisator.controllers.totalisator-controller
  (:require [totalisator.repository.queries :as q]))

(defn save-totalisator! [totalisator]
  (let [{:keys [user-id name description winner-count]} totalisator
        new-totalisator-id (q/insert<! q/save-totalisator<! user-id name description winner-count)]
    (assoc totalisator :id new-totalisator-id)))

(defn get-totalisators [] (q/query q/totalisators))
(defn get-totalisator [id] (q/query-single q/totalisator-by-id id))

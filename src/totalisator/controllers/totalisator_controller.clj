(ns totalisator.controllers.totalisator-controller
  (:require [totalisator.repository.queries :as q]))

(defn save-totalisator! [totalisator]
  (q/insert-or-update! q/save-totalisator<! q/update-totalisator<! totalisator))
(defn get-totalisators [] (q/query q/totalisators))
(defn get-totalisator [id] (q/query-single q/totalisator-by-id id))

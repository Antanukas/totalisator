(ns totalisator.service.totalisator-service
  (:require [totalisator.repository.queries :as q]))

(defn save-totalisator! [totalisator]
  (q/insert-or-update! q/save-totalisator<! q/update-totalisator<! totalisator))
(defn get-totalisators [] (q/query q/get-totalisators))
(defn get-totalisator [id] (q/query-single q/get-totalisator-by-id id))

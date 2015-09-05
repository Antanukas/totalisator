(ns totalisator.controllers.totalisator-controller
  (:require [ring.util.response :as resp]
            [totalisator.service.totalisator-service :as ts]
            [totalisator.controllers.utils :as utl]))

(defn get-totalisators []
  (resp/response (ts/get-totalisators)))

(defn save-totalisators [totalisators]
  (utl/batch-response totalisators ts/save-totalisator!))

(defn get-totalisator [totalisator-id]
  (resp/response (ts/get-totalisator totalisator-id)))

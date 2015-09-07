(ns totalisator.controllers.auth-controller
  (:require [totalisator.service.auth-service :as asrv]
            [ring.util.response :as resp]))

(defn authenticate [credentials]
  (resp/response
      (asrv/authenticate credentials)))
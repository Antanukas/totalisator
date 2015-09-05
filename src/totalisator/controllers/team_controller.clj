(ns totalisator.controllers.team-controller
  (:require [totalisator.service.team-service :as tms]
            [ring.util.response :as resp]
            [totalisator.controllers.utils :as utl]))

(defn get-payouts [totalisator-id winner-team-id]
  (resp/response (tms/get-payouts totalisator-id winner-team-id)))

(defn get-teams [totalisator-id]
  (resp/response (tms/get-teams totalisator-id)))

(defn save-teams [totalisator-id, teams]
  (utl/batch-response teams tms/save-team! totalisator-id))
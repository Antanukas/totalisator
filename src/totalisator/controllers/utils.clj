(ns totalisator.controllers.utils
  (:require [ring.util.response :as resp]))

(defn batch-response [requests func & args]
  (resp/response (->> requests (map #(cons % args)) (map #(apply func %)))))

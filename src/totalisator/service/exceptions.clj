(ns totalisator.service.exceptions
  (:import (clojure.lang ExceptionInfo)))

(defn- throw-ex [msg type]
  (throw (ex-info msg {:type type :message msg})))

(defn- json-response [status message]
  {:status status :body {:status status :message message}})

(defn wrap-exception-handling
  [handler]
  (fn [request]
    (try
      (handler request)
      (catch ExceptionInfo e
        (let [{:keys [type message]} (ex-data e)]
          (case type
            :data-not-found (json-response 404 message)
            :invalid-data (json-response 400 message)
            (throw e)))))))


(defn throw-data-not-found [msg]
  (throw-ex msg :data-not-found))

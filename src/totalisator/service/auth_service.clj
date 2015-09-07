(ns totalisator.service.auth-service
  (:require [clj-jwt.core :refer :all]
            [clj-jwt.key :refer [private-key]]
            [clj-jwt.intdate :refer [intdate->joda-time]]
            [clj-time.core :refer [after? now plus days]]
            [totalisator.repository.queries :as q]
            [totalisator.context.context :as ctx]
            [schema.core :as s]
            [totalisator.service.exceptions :as ex]))

(defn encode-claims [token-claims secret]
  (-> token-claims jwt (sign :HS256 secret) to-str))

(defn expired-token? [token]
  (after? (now) (:exp token)))

(defn decode-token [encoded-token]
  (-> encoded-token str->jwt :claims
    (update :exp intdate->joda-time)
    (update :iat intdate->joda-time)))

(defn valid-token? [encoded-token secret]
  (try
    (-> encoded-token str->jwt (verify secret))
    (catch Exception e
      (.printStackTrace e)
      (ex/throw-unauthorized "Corrupted token passed")
      false)))

(defn new-claims [& [{:keys [username id]}]]
  {:iss      "totalisator-app"
   :exp      (plus (now) (days 1))
   :iat      (now)
   :username username
   :user-id   id})

(s/defschema Credentials {:username String :password String})
(s/defschema Token {:token String})

(s/defn ^:always-validate authenticate :- Token
  [credentials :- Credentials]
  (if-let [existing-user (q/query-single q/get-user-by-name credentials)]
    (if (= (:password existing-user) (:password credentials))
      {:token (encode-claims (new-claims existing-user) @ctx/jwt-secret)}
      (ex/throw-unauthorized "Invalid password"))
    (ex/throw-data-not-found "User not found" )))


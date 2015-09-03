(ns totalisator.context.jwt-workflow
  (:require [totalisator.service.auth-service :as asrv]
            [cemerick.friend :as friend]
            [cemerick.friend.workflows :refer [make-auth]]
            [totalisator.context.context :as ctx]))

(defn- assoc-seq-or-map-fn [key value]
  (fn [m]
    (let [assoc-userid-fn (fn [m] (assoc m key value))]
      (cond
        (seq? m) (->> m (filter map?) (map assoc-userid-fn))
        (map? m) (assoc-userid-fn m)
        :else m))))

(defn wrap-current-user-id-body
  [handler & [{:keys [user-id-key-name] :or {user-id-key-name :current-user-id}}]]
  (fn [request]
    (handler
      (if-let [user-id (get-in (friend/current-authentication request) [:identity :user-id])]
        (-> request
          (update :body (assoc-seq-or-map-fn user-id-key-name user-id))
          (update :params (assoc-seq-or-map-fn user-id-key-name user-id)))
        request))))

(defn jwt-token-workflow [req]
  (if-let [[_ token] (clojure.string/split (get-in req [:headers @ctx/jwt-header] "") #" ")]
    (when (asrv/valid-token? token @ctx/jwt-secret)
      (let [decoded-token (asrv/decode-token token)]
        (when (not (asrv/expired-token? decoded-token))
          (make-auth
            {:identity (asrv/decode-token token) :roles #{:user}}
            {::friend/workflow          :no-password-workflow
             ::friend/redirect-on-auth? false
             ::friend/ensure-session    false}))))))

(defn- unauthorized-handler
  [_]
  {:status 403
   :body   {:code 403 :message "Forbidden"}})

(defn- unauthenticated-handler
  [_]
  {:status 401
   :body   {:code 401 :message "Unauthorized"}})

(defn wrap-authenticate [handler]
  (friend/authenticate handler
    {:unauthorized-handler    unauthorized-handler
     :workflows               [jwt-token-workflow]
     :unauthenticated-handler unauthenticated-handler}))

(ns totalisator.context.handler
  (:require [compojure.core :refer :all :as core]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [ring.middleware.json :as middleware]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [camel-snake-kebab.core :refer :all]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [totalisator.controllers.routes :as routes]
            [totalisator.context.jwt-workflow :as jwt]
            [cemerick.friend :as friend]))



;middleware for printlns
(defn- printlner [handler]
  (fn [request]
    (println "Request" request)
    (let [resp (handler request)]
      (println "Response: " resp)
      resp)))

(defn- wrap-json-keys [handler]
  (fn [request]
    (let [response (handler request)]
      (update response :body (partial transform-keys ->camelCaseKeyword)))))

(defn wrap-api [routes]
  (-> routes
    (wrap-keyword-params)
    (wrap-params)
    (middleware/wrap-json-body {:keywords? true :bigdecimals? true})
    (wrap-json-keys)
    (middleware/wrap-json-response)
    (wrap-defaults api-defaults)))

(def app
  (core/routes
    (context "/api" []
      (-> routes/api-routes
        ;(printlner)
        (jwt/wrap-current-user-id-body)
        (friend/wrap-authorize #{:user})
        (jwt/wrap-authenticate)
        (wrap-api)))
    (context "/authapi" []
      (-> routes/auth-routes (wrap-api)))
    (-> routes/site-routes
      (wrap-defaults site-defaults))))
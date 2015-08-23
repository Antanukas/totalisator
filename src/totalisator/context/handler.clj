(ns totalisator.context.handler
  (:require [compojure.core :refer :all :as core]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults api-defaults]]
            [ring.middleware.json :as middleware]
            [camel-snake-kebab.core :refer :all]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [totalisator.controllers.routes :as routes]))

(defn wrap-user-id-body-path
  [handler & [{:keys [user-id-key-name] :or {user-id-key-name :user-id}}]]
  (fn [request]
    (handler
      (if-let [user-id (get-in request [:params user-id-key-name])]
        (assoc-in request [:body user-id-key-name] user-id)
        request))))

(defn wrap-json-keys [handler]
  (fn [request]
    (let [response (handler request)]
      (println (:body response))
      (update response :body (partial transform-keys ->camelCaseKeyword)))))

(def app
  (core/routes
    (context "/api" []
      (-> routes/api-routes
        (middleware/wrap-json-body {:keywords? true :bigdecimals? true})
        (wrap-user-id-body-path)
        (wrap-json-keys)
        (middleware/wrap-json-response)
        (wrap-defaults api-defaults)))
    (-> routes/site-routes
      (wrap-defaults site-defaults))))

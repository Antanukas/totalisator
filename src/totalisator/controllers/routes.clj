(ns totalisator.controllers.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [totalisator.controllers.user-controller :as user-controller]
            [totalisator.controllers.totalisator-controller :as totalisator-controller]))

(defn- batch-response [requests func & args]
  (resp/response (->> requests (map #(cons % args)) (map #(apply func %)))))

(defroutes site-routes
  (GET "/" [] (resp/redirect "/index.html"))
  (route/resources "/" {:root "public"})
  (route/not-found "Not Found"))

(defroutes api-routes
  ;Routes that can be managed with own user only
  (POST "/users" [:as {users :body}] (batch-response users user-controller/save-user!))
  (GET "/users" [] (resp/response (user-controller/get-users)))
  (POST "/users/:user-id/totalisators" [:as {totalisators :body}] (batch-response totalisators totalisator-controller/save-totalisator!))
  (POST "/users/:user-id/totalisators/:totalisator-id/teams" [user-id totalisator-id :as {teams :body}] (resp/response teams))
  (POST "/users/:user-id/totalisators/:totalisator-id/teams/:team-id" [user-id totalisator-id team-id :as {teams :body}] (resp/response teams))
  (POST "/users/:user-id/totalisators/:totalisator-id/teams/:team-id/bets" [user-id totalisator-id team-id :as {bets :body}] (resp/response bets))
  (POST "/users/:user-id/totalisators/:totalisator-id/matches" [totalisator-id :as {matches :body}] (resp/response matches))
  (POST "/users/:user-id/totalisators/:totalisator-id/matches/:match-id" [totalisator-id match-id :as {match :body}] (resp/response match))
  (POST "/users/:user-id/totalisators/:totalisator-id/matches/:match-id/bets" [totalisator-id match-id :as {bet :body}] (resp/response bet))

  (GET "/totalisators" [] (resp/response (totalisator-controller/get-totalisators)))
  (GET "/totalisators/:totalisator-id" [totalisator-id] (resp/response (totalisator-controller/get-totalisator totalisator-id)))
  (GET "/totalisators/:totalisator-id/matches" [] (resp/response [{:id 1 :home-team "Lithuania" :away-team "Serbia"}]))
  (GET "/totalisators/:totalisator-id/teams" [] (resp/response [{:id 1 :home-team "Lithuania" :away-team "Serbia"}])))

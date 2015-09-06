(ns totalisator.controllers.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [totalisator.controllers.auth-controller :as actrl]
            [totalisator.controllers.totalisator-controller :as tctrl]
            [totalisator.controllers.bet-controller :as bctrl]
            [totalisator.controllers.team-controller :as tmctrl]
            [compojure.coercions :as c]))

(defroutes site-routes
  ;routing webjars
  (route/resources "/" {:root "META-INF/resources/"})
  (GET "/" [] (resp/redirect "/index.html"))
  (route/resources "/" {:root "public"})
  (route/not-found "Not Found"))

(defroutes auth-routes
  (POST "/authentication" [:as {creds :body}] (actrl/authenticate creds)))

(defroutes totalisator-resource
  (GET "/totalisators" [] (tctrl/get-totalisators))
  (POST "/totalisators" [:as {totalisators :body}] (tctrl/save-totalisators totalisators))
  (GET "/totalisators/:totalisator-id" [totalisator-id :<< c/as-int] (tctrl/get-totalisator totalisator-id)))

(defroutes bet-resource
  (POST "/totalisators/:totalisator-id/teams/:team-id/bets"
        [totalisator-id :<< c/as-int team-id :<< c/as-int :as {bets :body}] (bctrl/create-bets totalisator-id team-id bets)))

(defroutes team-resource
  (GET "/totalisators/:totalisator-id/teams/:winner-team-id/payouts"
       [totalisator-id :<< c/as-int winner-team-id :<< c/as-int]
    (tmctrl/get-payouts totalisator-id winner-team-id))
  (GET "/totalisators/:totalisator-id/teams" [totalisator-id :<< c/as-int] (tmctrl/get-teams totalisator-id))
  (POST "/totalisators/:totalisator-id/teams"
        [totalisator-id :<< c/as-int :as {teams :body}] (tmctrl/save-teams totalisator-id teams)))

(defroutes api-routes
  totalisator-resource
  bet-resource
  team-resource
  (route/not-found (resp/not-found {:status 404 :message "Route not found"})))

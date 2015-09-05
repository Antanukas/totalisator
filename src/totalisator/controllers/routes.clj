(ns totalisator.controllers.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [totalisator.service.totalisator-service :as ts]
            [totalisator.service.team-service :as tms]
            [totalisator.service.bet-service :as bets]
            [totalisator.controllers.auth-controller :as actrl]
            [totalisator.controllers.totalisator-controller :as tctrl]
            [compojure.coercions :as c]))

(defn- batch-response [requests func & args]
  (resp/response (->> requests (map #(cons % args)) (map #(apply func %)))))

(defroutes site-routes
  ;routing webjars
  (route/resources "/" {:root "META-INF/resources/"})
  (GET "/" [] (resp/redirect "/index.html"))
  (route/resources "/" {:root "public"})
  (route/not-found "Not Found"))

(defroutes auth-routes
  (POST "/authentication" [:as {creds :body}] (actrl/authenticate creds)))

(defroutes api-routes
  (GET "/totalisators" [] (tctrl/get-totalisators))
  (POST "/totalisators" [:as {totalisators :body}] (tctrl/save-totalisators totalisators))
  (GET "/totalisators/:totalisator-id"
       [totalisator-id] (tctrl/get-totalisator totalisator-id))

  (GET "/totalisators/:totalisator-id/teams/:winner-team-id/payouts"
       [totalisator-id :<< c/as-int winner-team-id :<< c/as-int]
       (resp/response (tms/get-payouts totalisator-id winner-team-id)))
  (POST "/totalisators/:totalisator-id/teams/:team-id/bets"
        [totalisator-id team-id :as {bets :body}] (batch-response bets bets/new-winner-bet totalisator-id team-id))

  (GET "/totalisators/:totalisator-id/teams"
       [totalisator-id] (resp/response (tms/get-teams totalisator-id)))
  (POST "/totalisators/:totalisator-id/teams"
        [totalisator-id :as {teams :body}] (batch-response teams tms/save-team! totalisator-id))
  (route/not-found (resp/not-found {:status 404 :message "Route not found"})))

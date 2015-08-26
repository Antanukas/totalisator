(ns totalisator.controllers.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [totalisator.service.user-service :as us]
            [totalisator.service.totalisator-service :as ts]
            [totalisator.service.team-service :as tms]
            [totalisator.service.bet-service :as bets]
            [totalisator.service.match-service :as ms]))

(defn- batch-response [requests func & args]
  (resp/response (->> requests (map #(cons % args)) (map #(apply func %)))))

(defroutes site-routes
  ;routing webjars
  (route/resources "/" {:root "META-INF/resources/"})
  (GET "/" [] (resp/redirect "/index.html"))
  (route/resources "/" {:root "public"})
  (route/not-found "Not Found"))

;TODO secure routes
(defroutes api-routes
  ;Routes that can be managed with own user only
  (POST "/users" [:as {user :body}] (resp/response (us/save-user! user)))
  (GET "/users" [] (resp/response (us/get-users)))
  (POST "/users/:current-user-id/totalisators"
        [:as {totalisators :body}] (batch-response totalisators ts/save-totalisator!))
  (POST "/users/:current-user-id/totalisators/:totalisator-id/teams"
        [totalisator-id :as {teams :body}] (batch-response teams tms/save-team! totalisator-id))
  (POST "/users/:current-user-id/totalisators/:totalisator-id/teams/:team-id/bets"
        [team-id :as {bets :body}] (batch-response bets bets/new-winner-bet team-id))
  (POST "/users/:current-user-id/totalisators/:totalisator-id/matches"
        [totalisator-id :as {matches :body}] (batch-response matches ms/save-match! totalisator-id))
  (POST "/users/:current-user-id/totalisators/:totalisator-id/matches/:match-id/bets"
        [match-id :as {bets :body}] (batch-response bets bets/new-match-bet match-id))

  (GET "/users/:current-user-id/bets"
    [current-user-id] (resp/response (bets/get-team-bets current-user-id)))
  (GET "/totalisators" [] (resp/response (ts/get-totalisators)))
  (GET "/totalisators/:totalisator-id"
       [totalisator-id] (resp/response (ts/get-totalisator totalisator-id)))
  (GET "/totalisators/:totalisator-id/matches"
       [totalisator-id] (resp/response (ms/get-matches totalisator-id)))
  (GET "/totalisators/:totalisator-id/teams"
       [totalisator-id] (resp/response (tms/get-teams totalisator-id))))

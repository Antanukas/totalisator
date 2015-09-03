(ns totalisator.controllers.routes
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as resp]
            [totalisator.service.user-service :as us]
            [totalisator.service.totalisator-service :as ts]
            [totalisator.service.team-service :as tms]
            [totalisator.service.bet-service :as bets]
            [totalisator.service.match-service :as ms]
            [totalisator.service.auth-service :as asrv]
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
  (POST "/authentication" [:as {authentication :body}] (resp/response (asrv/authenticate authentication))))

(defroutes api-routes
  (POST "/totalisators/:totalisator-id/teams/:team-id/bets"
        [totalisator-id team-id :as {bets :body}] (batch-response bets bets/new-winner-bet totalisator-id team-id))
  ;(GET "/totalisators/:totalisator-id/teams/:team-id/bets"
  ;[totalisator-id team-id] (resp/response (bets/get-team-bets totalisator-id team-id))
  (POST "/totalisators/:totalisator-id/matches/:match-id/bets"
        [match-id :as {bets :body}] (batch-response bets bets/new-match-bet match-id))

  ;(GET "/users" [] (resp/response (us/get-users)))
  ;(POST "/users" [:as {users :body}] (batch-response users us/login!))
  (GET "/users/:user-id/bets"
       [user-id] (resp/response (bets/get-team-bets user-id)))
  (GET "/users/:user-id/bets"
       [current-user-id] (resp/response (bets/get-team-bets current-user-id)))

  (GET "/totalisators" [] (resp/response (ts/get-totalisators)))
  (POST "/totalisators" [:as {totalisators :body}] (batch-response totalisators ts/save-totalisator!))
  (GET "/totalisators/:totalisator-id"
       [totalisator-id] (resp/response (ts/get-totalisator totalisator-id)))

  (GET "/totalisators/:totalisator-id/matches"
       [totalisator-id] (resp/response (ms/get-matches totalisator-id)))
  (POST "/totalisators/:totalisator-id/matches"
        [totalisator-id :as {matches :body}] (batch-response matches ms/save-match! totalisator-id))

  (GET "/totalisators/:totalisator-id/teams/:winner-team-id/leaderboards"
       [totalisator-id :<< c/as-int winner-team-id :<< c/as-int]
       (resp/response (tms/get-leaderboard totalisator-id winner-team-id)))
  (GET "/totalisators/:totalisator-id/teams"
       [totalisator-id] (resp/response (tms/get-teams totalisator-id)))
  (POST "/totalisators/:totalisator-id/teams"
        [totalisator-id :as {teams :body}] (batch-response teams tms/save-team! totalisator-id))
  (route/not-found (resp/not-found {:status 404 :message "Route not found"})))

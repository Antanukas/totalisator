(ns user
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [totalisator.context.context :as c]
            [totalisator.service.user-service :as us]
            [totalisator.service.totalisator-service :as ts]
            [totalisator.service.team-service :as tms]
            [totalisator.service.bet-service :as bets]
            [totalisator.service.match-service :as ms]))

(defn load-config []
  {:datastore  (jdbc/sql-database @c/db-configuration)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))


(defn init []
  (reset! c/db-configuration {:connection-uri "jdbc:h2:mem:test2;DB_CLOSE_DELAY=-1"})
  (migrate)

  (let [_ (println (us/get-users))
        antanas-id (:id (us/login! {:name "Antanas"}))
        other-user-id (:id (us/login! {:name "OtherGuy"}))

        eurobasked-id (:id (ts/save-totalisator! {:current-user-id antanas-id :description nil :created-by antanas-id :name "Eurobasket" :winner-count 1}))

        team-zalgiris-id (:id (tms/save-team! {:name "Zalgiris" :current-user-id antanas-id} eurobasked-id))
        team-rytas-id (:id (tms/save-team! {:name "Rytas" :current-user-id antanas-id} eurobasked-id))

        match1 (:id (ms/save-match! {:match-date "2015-09-11 18:50:00" :home-team team-rytas-id :away-team team-zalgiris-id :current-user-id antanas-id :home-team-score nil :away-team-score nil} eurobasked-id))
        _ (bets/new-winner-bet {:current-user-id other-user-id :amount 20} eurobasked-id)
        _ (bets/new-winner-bet {:current-user-id antanas-id :amount 10} eurobasked-id)]))



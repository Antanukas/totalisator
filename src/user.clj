(ns user
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [totalisator.context.context :as c]
            [totalisator.service.totalisator-service :as ts]
            [totalisator.service.team-service :as tms]
            [totalisator.service.bet-service :as bets]))

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

  (let [antanas-id 1
        other-user-id 2

        eurobasked-id (:id (ts/save-totalisator! {:current-user-id antanas-id :description nil :created-by antanas-id :name "Eurobasket"}))

        team-zalgiris-id (:id (tms/save-team! {:name "Zalgiris" :current-user-id antanas-id} eurobasked-id))
        team-rytas-id (:id (tms/save-team! {:name "Rytas" :current-user-id antanas-id} eurobasked-id))

        _ (bets/new-winner-bet {:current-user-id other-user-id :amount 20} eurobasked-id team-rytas-id)
        _ (bets/new-winner-bet {:current-user-id antanas-id :amount 10} eurobasked-id team-rytas-id)]))



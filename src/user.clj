(ns user
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [totalisator.context.context :as c]))

(defn load-config []
  {:datastore  (jdbc/sql-database c/db-configuration)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate []
  (repl/migrate (load-config)))

(defn rollback []
  (repl/rollback (load-config)))
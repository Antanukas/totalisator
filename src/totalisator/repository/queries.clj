(ns totalisator.repository.queries
  (:require [yesql.core :refer [defqueries]]
            [totalisator.context.context :as c]
            [camel-snake-kebab.core :refer :all]
            [camel-snake-kebab.extras :refer [transform-keys]]))

(defqueries "queries/users.sql")
(defqueries "queries/totalisators.sql")

(defn query [get-fn & args]
  (map #(transform-keys ->kebab-case-keyword %) (apply get-fn (cons c/db-configuration args))))

(defn query-single [get-fn & args]
  (first (query get-fn args)))

(defn insert<! [insert-fn & args]
  "Performs insert and return auto generated primary key value"
  (first (vals (apply insert-fn (cons c/db-configuration args)))))
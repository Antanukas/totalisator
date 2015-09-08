(ns totalisator.repository.queries
  (:require [yesql.core :refer [defqueries]]
            [totalisator.context.context :as c]
            [camel-snake-kebab.core :refer [->kebab-case-keyword ->snake_case_keyword]]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [schema.core :as s]))

(defqueries "queries/users.sql")
(defqueries "queries/totalisators.sql")
(defqueries "queries/teams.sql")
(defqueries "queries/winner_bets.sql")

(defn- db-spec [] {:connection (:connection-uri @c/db-configuration)})
(defn- ->kebab-case [map] (transform-keys ->kebab-case-keyword map))
(defn- ->snake-case [map] (transform-keys ->snake_case_keyword map))

(defn query
  ;yesql design issue. empty map not supported
  ([get-fn] (query get-fn {}))
  ([get-fn parameters] (map ->kebab-case (get-fn (->snake-case parameters) (db-spec)))))

(defn query-single [get-fn parameters]
  (first (query get-fn parameters)))

(defn update<! [update-fn data]
  (update-fn (->snake-case data) (db-spec))
  (dissoc data :current-user-id))

(defn- insert-and-get-id<! [insert-fn data]
  (-> data (->snake-case) (insert-fn (db-spec)) (vals) (first)))

(defn insert<! [insert-fn data]
  "Performs insert and returns data with autogenerated :id"
  (-> data
    (assoc :id (insert-and-get-id<! insert-fn data))
    (dissoc :current-user-id)))

(defn insert-or-update! [insert-fn update-fn data]
  (if (:id data)
    (update<! update-fn data)
    (insert<! insert-fn data)))


;Helper functions for joining tables

(comment
  (select-columns [{:team-id 1 :name "john" :other-table-column "s"}] [:team-id :name] :team-id)
  :=> ({:name "john", :id 1}))
;Hints select-keys, clojure.set/rename-keys
(defn select-columns [map-coll columns id-column-name]
  "Given collection of maps selects only specified columns. Specified id-column-name is renmaed to ID"
  (->> map-coll
    (map #(select-keys % columns))
    (map #(clojure.set/rename-keys % {id-column-name :id}))))

(comment
  (join [{:id 1 :name "parent1"} {:id 2 :name "parent2"}] [{:id 1 :parent-id 2} {:id 2 :parent-id 1}] :childs :parent-id)
  :=> ({:id 1, :name "parent1", :childs [{:id 2, :parent-id 1}]} {:id 2, :name "parent2", :childs [{:id 1, :parent-id 2}]}))
;Hints group-by, assoc, get, (:keyword map) => value of :keyword in map
(defn join [parent-coll child-coll parent-column join-column]
  "Given to collections of tables joins them using join column. Parent will contain collection under parent-column key"
  (let [grouped-childs (group-by join-column child-coll)]
    (map #(assoc % parent-column (get grouped-childs (:id %) [])) parent-coll)))

(s/defschema Team-With-Bets
  {:id s/Int
   :name String
   :totalisator-id s/Int
   :created-by s/Int
   :bets [{:id s/Int :betor-id s/Int :totalisator-team-id s/Int :amount s/Num :username String}]})

;Implement using select-columns and join
;Switch to namespace using (use 'totalisator.repository.queries)
(s/defn ^:always-validate get-teams-with-bets :- [Team-With-Bets]
  [totalistator-id :- s/Num]
  (let [team-bet-table (query get-teams-with-bets-raw {:totalisator-id totalistator-id})
        bets (select-columns team-bet-table [:bet-id :betor-id :totalisator-team-id :amount :username] :bet-id)
        teams (distinct (select-columns team-bet-table [:team-id :name :totalisator-id :created-by] :team-id))
        teams-with-bets (join teams bets :bets :totalisator-team-id)]
    teams-with-bets))

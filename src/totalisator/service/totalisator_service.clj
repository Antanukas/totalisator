(ns totalisator.service.totalisator-service
  (:require [totalisator.repository.queries :as q]
            [schema.core :as s]
            [totalisator.service.exceptions :as ex]))

(s/defschema New-Totalisator {:name String :description String :current-user-id s/Num})
(s/defschema Totalisator {:id s/Num :name String :description String :created-by s/Num})

(s/defn ^:always-validate save-totalisator! :- Totalisator
  [totalisator :- New-Totalisator]
  (q/insert-or-update! q/save-totalisator<! q/update-totalisator<!
    (clojure.set/rename-keys totalisator {:current-user-id :created-by})))

(s/defn ^:always-validate get-totalisators :- [Totalisator]
  []
  (q/query q/get-totalisators))

(s/defn ^:always-validate get-totalisator :- Totalisator
  [id :- Long]
  (or
    (q/query-single q/get-totalisator-by-id {:id id})
    (ex/throw-data-not-found (str "Totalisator " id " not found"))))

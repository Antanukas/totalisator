(defproject totalisator "0.1.0-SNAPSHOT"
  :description "Simple Totalisator webapp for Basketball events"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [compojure "1.4.0"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.4.0"]
                 [com.h2database/h2 "1.4.188"]
                 [ragtime "0.5.1"]
                 [yesql "0.5.0"]
                 [camel-snake-kebab "0.3.2"]]
  :plugins [[lein-ring "0.9.6"]]
  :ring {:handler totalisator.context.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}}
  :aliases {"migrate"  ["run" "-m" "user/migrate"]
            "rollback" ["run" "-m" "user/rollback"]})

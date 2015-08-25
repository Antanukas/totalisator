(ns totalisator.context.context)

(def db-configuration
  (atom
    {:connection-uri "jdbc:h2:./h2/totalisator;USER=test1;PASSWORD=test1;MODE=PostgreSQL;AUTO_SERVER=TRUE"}))

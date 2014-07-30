(defproject mobile-geo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.2.5"]
                 [compojure "1.1.8"]
                 [clj-http-lite "0.2.0"]
                 [org.clojure/java.jdbc "0.3.4"]
                 [mysql/mysql-connector-java "5.1.25"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler mobile-geo.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}})

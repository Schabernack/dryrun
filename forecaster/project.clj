(defproject forecaster "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.7"]
                 [cheshire "5.2.0"]
                 [ring "1.2.0"]
                 [ring/ring-json "0.2.0" :exclusions [ring-core]]
                 [compojure "1.1.5" :exclusions [clout]]
                 [clout "1.1.0"]
                 [http-kit "2.1.10"]
                 [clj-time "0.6.0"]
                 [org.slf4j/slf4j-log4j12 "1.7.5"]
                 [org.clojure/tools.logging "0.2.6"]]
  :aot :all
  :uberjar-name "forecaster.jar"
  :main forecaster.core)

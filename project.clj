(defproject coup "0.1.0-SNAPSHOT"
  :description "Simple application to test out probabilities for The Coup board game."
  :url "http://github.com/jonathan/coup"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/core.async "0.1.338.0-5c5012-alpha"]]
  :main ^:skip-aot coup.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[midje "1.6.3"]]}})

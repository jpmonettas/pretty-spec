(defproject pretty-spec "0.1.0-SNAPSHOT"
  :description "A pretty printer for clojure.spec forms."
  :url "https://github.com/jpmonettas/pretty-spec"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17" :scope "provided"]
                 [org.clojure/clojurescript "1.9.660" :scope "provided"]
                 [fipp "0.6.8"]]
  :target-path "target/%s"

  :source-paths ["src"]
  
  :cljsbuild {
    :builds [{:source-paths ["src"]
              :compiler {:output-to "target/main.js"
                         :optimizations :none
                         :pretty-print true}}]}
  
  :profiles {:uberjar {:aot :all}
             :dev {:plugins [[lein-cljsbuild "1.1.6"]]
                   :dependencies [[com.cemerick/piggieback "0.2.1"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})

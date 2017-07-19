(defproject pretty-spec "0.1.0-SNAPSHOT"
  :description "A pretty printer for clojure.spec forms."
  :url "https://github.com/jpmonettas/pretty-spec"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17" :scope "provided"]
                 [org.clojure/clojurescript "1.9.671" :scope "provided"]]
  :target-path "target/%s"
  :plugins [[lein-cljsbuild "1.1.6"]]
  :cljsbuild {
    :builds [{:source-paths ["src"]
              :compiler {:output-to "target/main.js"
                         :optimizations :none
                         :pretty-print true}}]}
  :profiles {:uberjar {:aot :all}})

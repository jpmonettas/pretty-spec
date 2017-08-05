(defproject pretty-spec "0.1.1"
  :description "A pretty printer for clojure.spec forms."
  :url "https://github.com/jpmonettas/pretty-spec"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17" :scope "provided"]
                 [org.clojure/clojurescript "1.9.660" :scope "provided"]
                 [fipp "0.6.8"]]
  :target-path "target/%s"

  :source-paths ["src"]
  
  :cljsbuild {:builds [{:id "dev"
                        :figwheel true
                        :source-paths ["src"]
                        :compiler {:main pretty-spec.core
                                   :output-to "resources/public/js/compiled/app.js"
                                   :output-dir "resources/public/js/out"
                                   :asset-path "js/out"
                                   :optimizations :none}}]}
  
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[com.cemerick/piggieback "0.2.2"]
                                  [figwheel-sidecar "0.5.11"]]
                   :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}}})

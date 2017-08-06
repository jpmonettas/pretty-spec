(ns pretty-spec.core-test
  (:require
            [clojure.spec.alpha :as s]
            [clojure.test :refer :all]
            [clojure.test.check.generators :as gen]
            [com.gfredericks.test.chuck.clojure-test :refer [checking]]
            [com.stuartsierra.dependency :as deps]
            [pretty-spec.core :refer :all]
            [clojure.string :as string]
            [clojure.core.specs.alpha] ; side-effect: loads specs
            [ring.core.spec]           ; side effect: loads specs
            [fipp.clojure :as fipp]
            ))

(defn spec-dependencies [spec]
  (->> spec
       s/form
       (tree-seq coll? seq )
       (filter #(and (s/get-spec %)
                     (not= spec %)))
       distinct))

(defn topo-sort [specs]
  (deps/topo-sort
   (reduce
    (fn [gr spec]
      (reduce
       (fn [g d]
         ;; If this creates a circular reference, then
         ;; just skip it.
         (if (deps/depends? g d spec)
           g
           (deps/depend g spec d)))
       gr
       (spec-dependencies spec)))
    (deps/graph)
    specs)))

(defn spec-gen [prefix]
  (->> (s/registry)
       (map key)
       (filter #(string/starts-with? (str %) (str prefix)))
       topo-sort
       (filter keyword?)
       gen/elements))

(defn ignore-whitespace [s]
    (string/trim (string/replace s #"\s+" " ")))

(deftest clojure-core-specs
  (checking
   "all clojure.core specs are printed without losing information"
   100
   [spec (spec-gen :clojure.core)]
   (is (= (ignore-whitespace (with-out-str (fipp/pprint (s/form spec))))
          (ignore-whitespace (with-out-str (pprint (s/form spec))))))))

(deftest ring-specs
  (checking
   "all ring specs are printed without losing information"
   100
   [spec (spec-gen :ring)]
   (is (= (ignore-whitespace (with-out-str (fipp/pprint (s/form spec))))
          (ignore-whitespace (with-out-str (pprint (s/form spec))))))))

(ns pretty-spec.core
  (:require [clojure.spec.alpha :as s]
            [fipp.engine :refer [pprint-document]]
            [fipp.clojure :as fipp-clojure]
            [fipp.edn :as fipp-edn]
            [fipp.visit :as fipp-visit])) 

(defmulti build-document type)

(defn- build-arg-pairs [[f & args]]
  [:group "("
   [:align (build-document f) :line
    (->> (partition 2 args)
         (map (fn [[p1 p2]]
                [:span (build-document p1) " " (build-document p2)]))
         (interpose :line))
    ")"]])

(defn- build-one-arg-and-opts [[f & args]]
  [:group "("
   [:align (build-document f) :line (build-document (first args))
    (when (next args) :line)
    (->> (partition 2 (rest args))
         (map (fn [[optk optv]]
                [:span (build-document optk) " " (build-document optv)]))
         (interpose :line))
    ")"]])

(defn- build-args [[f & args]]
  [:group "("
   [:align (build-document f) :line 
    (->> args
         (map build-document)
         (interpose :line))
    ")"]])

(defn- build-keys-vec [ks]
  [:group "["
   [:align (->> ks
               (map build-document)
               (interpose :line))]
   "]"])

(defn- build-keys [[f & args]]
  [:group "("
   [:align (build-document f) :line 
    (->> (partition 2 (rest args))
         (map (fn [[k v]]
                [:span (build-document k) " " (build-keys-vec v)]))
         (interpose :line))
    ")"]])

(defn- build-one-arg [[f & args]]
  [:group "("
   [:align (build-document f) " " (build-document (first args)) ")"]])

(defn build-unknown [form]
  (fipp-visit/visit (fipp-edn/map->EdnPrinter {:symbols fipp-clojure/default-symbols
                                                    :print-length *print-length*
                                                    :print-level *print-level*
                                                    :print-meta *print-meta*})
                    form))

(defmethod build-document #?(:clj clojure.lang.ISeq :cljs cljs.core/List)
  [[f & _ :as form]]
  (cond
    (#{"fspec" "or" "cat" "alt"} (name f))
    (build-arg-pairs form)

    (#{"coll-of" "map-of"} (name f))
    (build-one-arg-and-opts form)

    (#{"and" "merge" "conformer"} (name f))
    (build-args form)

    (= "keys" (name f))
    (build-keys form)

    (#{"?" "+" "*" "nilable"} (name f))
    (build-one-arg form)
    
    true (build-unknown form)))

(defmethod build-document :default
  [x]
  (build-unknown x))

(defn pprint [form options]
  (let [doc (build-document form)]
    (pprint-document doc options)))



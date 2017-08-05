(ns pretty-spec.core
  (:require [clojure.spec.alpha :as s]
            [fipp.engine :refer [pprint-document]]
            [fipp.clojure :as fipp-clojure]
            [fipp.edn :as fipp-edn]
            [fipp.visit :as fipp-visit :refer [visit]])) 


(defn- build-arg-pairs [p [f & args]]
  [:group "("
   [:align (visit p f) :line
    (->> (partition 2 args)
         (map (fn [[p1 p2]]
                [:span (visit p p1) " " (visit p p2)]))
         (interpose :line))
    ")"]])

(defn- build-one-arg-and-opts [p [f & args]]
  [:group "("
   [:align (visit p f) :line (visit p (first args))
    (when (next args) :line)
    (->> (partition 2 (rest args))
         (map (fn [[optk optv]]
                [:span (visit p optk) " " (visit p optv)]))
         (interpose :line))
    ")"]])

(defn- build-two-arg-and-opts [p [f kp vp & args]]
  [:group "("
   [:align (visit p f) :line (visit p kp) :line (visit p vp)
    (when (next args) :line)
    (->> (partition 2 (rest args))
         (map (fn [[optk optv]]
                [:span (visit p optk) " " (visit p optv)]))
         (interpose :line))
    ")"]])

(defn- build-args [p [f & args]]
  [:group "("
   [:align (visit p f) :line 
    (->> args
         (map (partial visit p))
         (interpose :line))
    ")"]])

(defn- build-keys-vec [p ks]
  [:group "["
   [:align (->> ks
                (map (partial visit p))
                (interpose :line))]
   "]"])

(defn- build-keys [p [f & args]]
  [:group "("
   [:align (visit p f) :line 
    (->> (partition 2 args)
         (map (fn [[k v]]
                [:span (visit p k) " " (build-keys-vec p v)]))
         (interpose :line))
    ")"]])

(defn- build-one-arg [p [f & args]]
  [:group "("
   [:align (visit p f) " " (visit p (first args)) ")"]])


(defn build-symbol-map [dispatch]
  (into {} (for [[pretty-fn syms] dispatch
                 sym syms
                 sym (cons sym [(symbol "clojure.spec.alpha" (name sym))
                                (symbol "cljs.spec.alpha" (name sym))])]
             [sym pretty-fn])))

(def default-symbols
  (build-symbol-map
   {build-arg-pairs        '[fspec or cat alt]
    build-one-arg-and-opts '[coll-of map-of]
    build-two-arg-and-opts '[map-of]
    build-args             '[and merge conformer tuple]
    build-keys             '[keys]
    build-one-arg          '[? + * nilable]}))

(defn spec-printer [options]
  (let [effective-options (merge {:symbols (merge default-symbols
                                                  fipp-clojure/default-symbols)}
                                 options)]
    (fipp-edn/map->EdnPrinter effective-options)))

(def ^:dynamic *print-document* false)

(defn pprint
  "Pretty prints a spec form.
  form as returned by (clojure.spec/form ...)
  Options are the same as in https://github.com/brandonbloom/fipp"
  ([form] (pprint form {}))
  ([form options]
   (pprint form options (spec-printer options)))
  ([form options printer]
   (let [doc (fipp-visit/visit printer form)]
     (when *print-document*
       (prn "------------ Fipp doc ----------")
       (fipp-edn/pprint doc)
       (prn "------------ End Fipp doc ----------"))
     (pprint-document doc options))))


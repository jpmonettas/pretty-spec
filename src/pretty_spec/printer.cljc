(ns pretty-spec.printer
  (:require [fipp.visit :as fipp-visit :refer [visit]]
            [fipp.edn :as fipp-edn :refer [pretty-coll]]
            [fipp.ednize :refer [edn record->tagged]]
            [fipp.visit :as fipp-visit :refer [visit visit*]]))


;; Copy of fipps edn printer but with support for ns-aliases
(defrecord EdnPrinter [symbols ns-aliases]

  fipp-visit/IVisitor
  
  (visit-unknown [this x]  (visit this (edn x)))

  (visit-nil [this] [:text "nil"])

  (visit-boolean [this x]
    [:text (str x)])

  (visit-string [this x]
    [:text (pr-str x)])

  (visit-character [this x]
    [:text (pr-str x)])

  (visit-symbol [this x]
    (let [x' (if (and (qualified-symbol? x)
                      (contains? ns-aliases (namespace x)))
               (symbol (ns-aliases (namespace x)) (name x))
               x)]
     [:text (str x')]))

  (visit-keyword [this x]
    (let [x' (if (and (qualified-keyword? x)
                      (contains? ns-aliases (namespace x)))
               (keyword (ns-aliases (namespace x)) (name x))
               x)]
     [:text (str x')]))
  
  (visit-number [this x]
    [:text (pr-str x)])

  (visit-seq [this x]
    (if-let [pretty (symbols (first x))]
      (pretty this x)
      (pretty-coll this "(" x :line ")" visit)))

  (visit-vector [this x]
    (pretty-coll this "[" x :line "]" visit))

  (visit-map [this x]
    (pretty-coll this "{" x [:span "," :line] "}"
                 (fn [printer [k v]]
                   [:span (visit printer k) " " (visit printer v)])))

  (visit-set [this x]
    (pretty-coll this "#{" x :line "}" visit))

  (visit-tagged [this {:keys [tag form]}]
    [:group "#" (pr-str tag)
     (visit this form)])

  (visit-meta [this m x]
    (visit* this x))

  (visit-var [this x]
    [:text (str x)])

  (visit-pattern [this x]
    [:text (pr-str x)])

  (visit-record [this x]
    (visit this (record->tagged x))))


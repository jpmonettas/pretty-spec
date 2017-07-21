(ns pretty-spec.core
  (:require [clojure.spec.alpha :as s]
            [clojure.pprint :as pp])) 


(defn write [s]
  #?(:clj (.write ^java.io.Writer *out* s)
     :cljs (-write *out* s)))

(defn- pprint-arg-pairs [[f & args]]
  (pp/pprint-logical-block
   :prefix "(" :suffix ")"
   (pp/pprint-indent :block 1)
   (pp/write-out f)    
   (write " ")
   (pp/pprint-newline :linear)
   (pp/print-length-loop [[[p1 p2 ] & r] (partition 2 args)]
                         (when p1
                           (pp/write-out p1)
                           (write " ")
                           (pp/write-out p2)
                           (when r
                             (write " ")
                             (pp/pprint-newline :linear))
                           (recur r)))))

(defn- pprint-one-arg-and-opts [[f & args]]
  (pp/pprint-logical-block
   :prefix "(" :suffix ")"
   (pp/pprint-indent :block 1)
   (pp/write-out f)    
   (write " ")
   (pp/pprint-newline :linear)
   (pp/write-out (first args))
   (when (next args)
     (write " ")
     (pp/pprint-newline :linear))
   (pp/print-length-loop [[[optk optv] & r] (partition 2 (rest args))]
                         (when optk
                           (pp/write-out optk)
                           (write " ")
                           (pp/write-out optv)
                           (when r
                             (write " ")
                             (pp/pprint-newline :linear))
                           (recur r)))))
(defn- pprint-args [[f & args]]
  (pp/pprint-logical-block
   :prefix "(" :suffix ")"
   (pp/pprint-indent :block 1)
   (pp/write-out f)    
   (write " ")
   (pp/pprint-newline :linear)
   (pp/print-length-loop [[p & r] args]
                         (when p
                           (pp/write-out p)
                           (when r
                             (write " ")
                             (pp/pprint-newline :linear))
                           (recur r)))))

(defn- pprint-keys [[f & args]]
  (pp/pprint-logical-block
   :prefix "(" :suffix ")"
   (pp/pprint-indent :block 1)
   (pp/write-out f)    
   (write " ")
   (pp/pprint-newline :linear)
   (pp/print-length-loop [parts (partition 2 args)]
                         (let [[[kt ks] & r] parts]
                          (when kt
                            (pp/write-out kt)
                            (write " ")
                            (pp/pprint-logical-block
                             :prefix "[" :suffix "]"
                             (pp/print-length-loop [[k & rk] ks]
                                                   (when k
                                                     (pp/write-out k)
                                                     (when rk
                                                       (write " ")
                                                       (pp/pprint-newline :linear))
                                                     (recur rk))))
                            (when r
                              (pp/pprint-newline :mandatory))
                            (recur r))))))
(defn- pprint-one-arg [[f & args]]
  (pp/pprint-logical-block
   :prefix "(" :suffix ")"
   (pp/pprint-indent :block 1)
   (pp/write-out f)    
   (write " ")
   (pp/write-out (first args))))

(defn- pprint-multi-spec [[f & args]]
  (let [[mm retag & multi-specs] args]
    (pp/pprint-logical-block
     :prefix "(" :suffix ")"
     (pp/pprint-indent :block 1)
     (pp/write-out f)    
     (write " ")
     (pp/pprint-newline :linear)
     (pp/write-out mm)
     (write " ")
     (pp/pprint-newline :linear)
     (pp/write-out retag)
     (pp/pprint-newline :mandatory)
     (pp/print-length-loop [[[k s] & r] multi-specs]
                           (when k
                             (pp/write-out k)
                             (write " ")
                             (pp/write-out s)
                             (when r
                               (pp/pprint-newline :mandatory))
                             (recur r))))))

(defn- pprint-spec-list-form [[f & _ :as form]]
  (cond
    (#{'clojure.spec.alpha/fspec
       'clojure.spec.alpha/or
       'clojure.spec.alpha/cat
       'clojure.spec.alpha/alt} f)
    (pprint-arg-pairs form)

    (#{'clojure.spec.alpha/coll-of
       'clojure.spec.alpha/map-of} f)
    (pprint-one-arg-and-opts form)

    (#{'clojure.spec.alpha/and
       'clojure.spec.alpha/merge} f)
    (pprint-args form)

    (= 'clojure.spec.alpha/keys f)
    (pprint-keys form)

    (#{'clojure.spec.alpha/?
       'clojure.spec.alpha/+
       'clojure.spec.alpha/*
       'clojure.spec.alpha/nilable} f)
    (pprint-one-arg form)
    
    (= 'clojure.spec.alpha/multi-spec f)
    (pprint-multi-spec form)
    
    true (pp/with-pprint-dispatch pp/code-dispatch
           (pp/write form))))

(defmulti spec-dispatch
  "The pretty print dispatch function for pretty printing clojure.spec forms."
  type)

(defmethod spec-dispatch clojure.lang.ISeq
  [form]
  (pprint-spec-list-form form))

(defmethod spec-dispatch :default
  [form]
  (pp/with-pprint-dispatch pp/code-dispatch
    (pp/write form)))

(defn pprint [form]
  (pp/with-pprint-dispatch spec-dispatch
    (pp/pprint form)))



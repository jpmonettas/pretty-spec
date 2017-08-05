# Pretty-Spec

A clojure.spec form pretty printer for Clojure and ClojureScript.

Extends [fipp](https://github.com/brandonbloom/fipp) pretty printer with rules for printing
clojure.spec forms.

This is just a simple library that does this one thing.

Checkout [inspectable](https://github.com/jpmonettas/inspectable) or [expound](https://github.com/bhb/expound)
if you are looking for spec browsing and explain-data analyzing.

Installation
------------
To include the library add the following to your `:dependencies`

    [pretty-spec "0.1.1"]
    
Usage
-----

```
user> (require '[clojure.spec.alpha :as s])
nil
user> (require '[pretty-spec.core :as pspec])
nil
user> (pspec/pprint (s/form 'clojure.core/let))

(clojure.spec.alpha/fspec
 :args (clojure.spec.alpha/cat
        :bindings :clojure.core.specs.alpha/bindings
        :body (clojure.spec.alpha/* clojure.core/any?))
 :ret clojure.core/any?
 :fn nil)
 
nil
```

Comparing to vanilla clojure.pprint:

```
user> (clojure.pprint/pprint (s/form 'clojure.core/let))

(clojure.spec.alpha/fspec
 :args
 (clojure.spec.alpha/cat
  :bindings
  :clojure.core.specs.alpha/bindings
  :body
  (clojure.spec.alpha/* clojure.core/any?))
 :ret
 clojure.core/any?
 :fn
 nil)
 
nil
```

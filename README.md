# pretty-spec

A clojure.spec form pretty printer for Clojure and ClojureScript.

Extends [Fipp](https://github.com/brandonbloom/fipp) pretty printer with rules for printing
clojure.spec forms.

Installation
------------
To include the library add the following to your `:dependencies`

    [pretty-spec "0.1.0"]
    
Usage
-----

```clojure
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

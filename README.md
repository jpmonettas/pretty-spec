# Pretty-Spec

A clojure.spec form pretty printer for Clojure and ClojureScript.

Extends [fipp](https://github.com/brandonbloom/fipp) pretty printer with rules for printing
clojure.spec forms.

This is just a simple library that does this one thing.

Checkout [inspectable](https://github.com/jpmonettas/inspectable) or [expound](https://github.com/bhb/expound)
if you are looking for spec browsing and explain-data analyzing.

Installation
------------

**Pretty-Spec** is available as a Maven artifact from Clojars.

The latest released version is: [![Clojars Project](https://img.shields.io/clojars/v/pretty-spec.svg)](https://clojars.org/pretty-spec)<br>

Usage
-----

```clojure
user> (require '[clojure.spec.alpha :as s])
nil
user> (require '[pretty-spec.core :as pspec])
nil
user> (pspec/pprint (s/form 'clojure.core/let))

; (clojure.spec.alpha/fspec
;  :args (clojure.spec.alpha/cat
;         :bindings :clojure.core.specs.alpha/bindings
;         :body (clojure.spec.alpha/* clojure.core/any?))
;  :ret clojure.core/any?
;  :fn nil)

nil
```

Comparing to vanilla clojure.pprint:

```clojure
user> (clojure.pprint/pprint (s/form 'clojure.core/let))

; (clojure.spec.alpha/fspec
;  :args
;  (clojure.spec.alpha/cat
;   :bindings
;   :clojure.core.specs.alpha/bindings
;   :body
;   (clojure.spec.alpha/* clojure.core/any?))
;  :ret
;  clojure.core/any?
;  :fn
;  nil)

nil
```

Options
-------

Pretty-spec pprint accepts the same options as [fipp](https://github.com/brandonbloom/fipp) pprint
plus :ns-aliases which you can use to make your pprint even more redable.

```clojure
user> (pspec/pprint (s/form 'clojure.core/let)
                    {:ns-aliases {"clojure.spec.alpha" "s"
                                  "clojure.core.specs.alpha" "score"
                                  "clojure.core" nil}})

; (s/fspec
;  :args (s/cat :bindings :score/bindings :body (s/* any?))
;  :ret any?
;  :fn nil)

```

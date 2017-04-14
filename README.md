# dependency-problem

## Solving a dependency problem with clojure.spec and boot.

### Introduction

I had a weird problem with Manifold (comes with Yada and Aleph). The following expression would throw an exception:

```
@(d/let-flow [a (d/success-deferred {:a 1})]
  a)
;; => java.lang.ClassCastException: java.lang.Long cannot be cast to java.util.Map$Entry
```

I noticed that with a fresh project, this exception didn't occur:

```
;; boot -d manifold repl
(require '[manifold.deferred :as d])
(d/let-flow [a (d/success-deferred {:a 1})] a)
;;=> << {:a 1} >>
```

so the problem was probably in the combination of one of our other libraries with Manifold.
Because we have quite a few dependencies I decided it was too much work bisecting them by hand. 
Clojure.spec and Boot to the rescue!

## Solution

I used clojure.spec to generate a random subset of our dependencies and used this to generate a script that executes the above test case with those dependencies
If the script would return an exit code other than zero, we would have found the problematic dependency. Clojure.test.spec also uses shrinking, which find the smallest set of problematic dependencies.
It turned out to be only one dependency in our case.

## Running

Clone this repo and run `boot diagnose` to run the clojure.spec.test test or `boot confirm` to see the failing example..

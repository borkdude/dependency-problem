(set-env!
 :dependencies
 '[[org.clojure/clojure "1.8.0"]
   [aleph "0.4.1"]
   [clojure-future-spec "1.9.0-alpha13"]
   [selmer "1.10.7"]
   [org.clojure/test.check "0.9.0"]]
 :source-paths #{"src"})

(require '[dependency-problem.core :as dp])

(deftask diagnose []
  (with-pass-thru [_]
    (dp/diagnose)))

(deftask confirm []
  (with-pass-thru [_]
    (println (dp/confirm))))

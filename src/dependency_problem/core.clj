(ns dependency-problem.core
  (:require [clojure.spec :as s]
            [clojure.spec.gen :as gen]
            [manifold.deferred :as d]
            [clojure.test :as t]
            [clojure.java.shell]
            [selmer.parser :refer [render]]
            [clojure.spec.test :as stest]))

(def dependencies
  '#{[org.clojure/core.async "0.3.442"]
     [prismatic/schema "1.1.4"]
     [com.stuartsierra/component "0.3.2"]
     [suspendable "0.1.1"]
     [org.clojure/tools.nrepl "0.2.12"]
     [com.taoensso/timbre "4.8.0"]
     [org.slf4j/slf4j-log4j12 "1.7.25"]
     [log4j/log4j "1.2.17"]
     [metrics-clojure "2.9.0"]
     [metrics-clojure-graphite "2.9.0"]
     [metrics-clojure-jvm "2.9.0"]
     [com.google.guava/guava "20.0"]
     [cheshire "5.7.0"]
     [pjson "0.3.8"]
     [selmer "1.10.7"]
     [camel-snake-kebab "0.4.0"]
     [buddy "1.3.0"]
     [digest "1.4.5"]
     [clj-time "0.13.0"]
     [clojure-csv/clojure-csv "2.0.2"]
     [commons-io/commons-io "2.5"]
     [org.apache.commons/commons-math3 "3.6.1"]
     [com.velisco/clj-ftp "0.3.8"]
     [clj-http "3.4.1"]
     [ring-basic-authentication "1.0.5"]
     [com.github.ben-manes.caffeine/caffeine "2.4.0"]
     [conman "0.6.3"]
     [org.postgresql/postgresql "9.4.1212"]
     [migratus "0.8.33"]
     [clojurewerkz/quartzite "2.0.0"]
     [com.taoensso/carmine "2.16.0"]
     [com.fasterxml/aalto-xml "1.0.0"]
     [org.clojure/data.xml "0.0.8"]
     [com.github.kyleburton/clj-xpath "1.4.11"]})

(s/def ::dependency
  dependencies)

(s/def ::dependencies
  (s/coll-of ::dependency :kind vector? :distinct true))

(defn run-script-with-deps [dependencies]
  (println "Running script with dependencies" dependencies)
  (let [script (render (slurp "template") {:dependencies dependencies})
        _ (spit "/tmp/script" script)
        _ (clojure.java.shell/sh "chmod" "+x" "/tmp/script")]
    (clojure.java.shell/sh "/tmp/script")))

(s/fdef run-script-with-deps
        :args (s/cat :deps ::dependencies)
        :ret #(zero? (:exit %)))

(defn diagnose []
  (let [res (stest/check `run-script-with-deps)
        summary (stest/summarize-results res)]
    (println res)
    (println summary)))

(defn confirm []
  (run-script-with-deps '[[pjson "0.3.8"]]))

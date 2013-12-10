(ns leiningen.test-refresh
  (:require [leinjacker.deps :as deps]
            [leinjacker.eval :as eval]))

(defn- add-deps [project]
  (-> project
      (deps/add-if-missing '[com.jakemccrary/lein-test-refresh "0.1.4-SNAPSHOT"])
      (deps/add-if-missing '[org.clojure/tools.namespace "0.2.4"])))

(defn- clojure-test-directories [project]
  (vec (concat (:test-path project [])
               (:test-paths project []))))

(defn test-refresh
  "Autoruns clojure.test tests on source change

USAGE: lein test-refresh
Runs tests whenever there is a change to code in classpath.
Reports test successes and failures to STDOUT.

USAGE: lein test-refresh :growl
Runs tests whenever code changes.
Reports results to growl and STDOUT."
  [project & args]
  (let [should-growl (some #{:growl ":growl" "growl"} args)
        tests (clojure-test-directories project)]
    (eval/eval-in-project
     (add-deps project)
     `(com.jakemccrary.test-refresh/monitor-project ~should-growl ~tests)
     `(require 'com.jakemccrary.test-refresh))))

(ns edge.config
  (:require [cprop.core :as cprop]
            [cprop.source :refer [from-env from-resource]]
            [failjure.core :as f]
            [mount.core :as mount :refer [defstate]]))

(defn load-resource
  "Loads single configuration resource.
  Returns empty map when resource was not found."

  [resource]
  (let [res (f/try* (from-resource resource))]
    (if (f/failed? res) {} res)))

(defn load-config
  "Loads configuration file depending on environment."

  [env]
  (cprop/load-config :resource "edge.edn"
                     :merge [(load-resource (str "edge-" env ".edn"))
                             (from-env)
                             {:env env}]))

(defn init-config []
  (load-config (or (System/getenv "ENV") "local")))

(defstate app-config
  :start (init-config))

(ns edge.system
  (:require [clojure.tools.namespace.repl :as tn]
            [mount.core :refer [defstate] :as mount]
            [edge.http]))

(defn go []
  (mount/start))

(defn stop []
  (mount/stop))

(defn reset []
  (stop)
  (tn/refresh :after 'edge.system/go))

(defn refresh []
  (stop)
  (tn/refresh))

(defn refresh-all []
  (stop)
  (tn/refresh-all))

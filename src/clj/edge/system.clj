(ns edge.system
  (:require [clojure.tools.namespace.repl :as tn]
            [mount.core :refer [defstate] :as mount]
            [cerber.oauth2.standalone.server :as oauth2]
            [edge.http]))

(defn go []
  ;; track changes only from certain dirs
  (tn/set-refresh-dirs "src/clj")

  ;; we don't want to start cerber's http server as we start our own
  (mount/start-without #'oauth2/http-server))

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

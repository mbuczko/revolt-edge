(ns edge.http
  (:require [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [edge.config :refer [app-config]]
            [edge.middleware :as middleware]
            [compojure.core :refer [defroutes ANY GET POST routes wrap-routes]]
            [compojure.route :as route]
            [mount.core :as mount :refer [defstate]]
            [org.httpkit.server :as web]
            [selmer.parser :as selmer]))

(defroutes public-routes
  (GET  "/status" [] "ok")
  (GET  "/" [] (selmer/render-file "index.html" {})))

(defroutes static-routes
  (route/resources "/styles"  {:root "styles"})
  (route/resources "/scripts" {:root "scripts"})
  (route/resources "/fonts"   {:root "fonts"})
  (route/resources "/img"     {:root "img"})
  (route/not-found "Not Found"))

;; initialization routines

(defn init-routes []
  (routes
   public-routes
   static-routes))

(defn init-server []
  (log/info "starting http server")
  (selmer/set-resource-path! (io/resource "templates"))

  ;; no caching for dev environment, please.

  (when-not (= (:env app-config) "prod")
    (selmer/cache-off!))

  (web/run-server (middleware/wrap-middlewares (init-routes)) (:http app-config)))


(defstate http-server
  :start (init-server)
  :stop  (http-server))

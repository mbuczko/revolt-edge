(ns edge.http
  "Bunch of functions handling start-up and tear down of HTTP server.
  Also, all routes and handler are defined here."

  (:require [clojure.tools.logging :as log]
            [compojure.core :refer [defroutes GET routes]]
            [compojure.route :as route]
            [edge.config :refer [app-config]]
            [edge.middleware :as middleware]
            [mount.core :as mount :refer [defstate]]
            [org.httpkit.server :as web]
            [selmer.parser :as selmer]))

(defroutes public-routes
  (GET  "/status" [] "ok")
  (GET  "/" [] (selmer/render-file "templates/index.html" {})))

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

  ;; no caching for dev environment, please.

  (when-not (= (:env app-config) "prod")
    (selmer/cache-off!))

  (web/run-server (middleware/wrap-middlewares (init-routes)) (:http app-config)))


(defstate http-server
  :start (init-server)
  :stop  (http-server))

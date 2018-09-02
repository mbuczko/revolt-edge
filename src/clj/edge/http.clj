(ns edge.http
  "Bunch of functions handling start-up and tear down of HTTP server.
  Also, all routes and handler are defined here."

  (:require cerber.handlers
            [cerber.oauth2.context :as ctx]
            cerber.roles
            [clojure.tools.logging :as log]
            [compojure.core :refer [defroutes GET POST routes wrap-routes]]
            [compojure.route :as route]
            [edge.config :refer [app-config]]
            [edge.middleware :as middleware]
            [mount.core :as mount :refer [defstate]]
            [org.httpkit.server :as web]
            [ring.middleware.defaults :refer [api-defaults wrap-defaults]]
            [selmer.parser :as selmer]))

(defroutes oauth2-routes
  (GET  "/authorize" [] cerber.handlers/authorization-handler)
  (POST "/approve"   [] cerber.handlers/client-approve-handler)
  (GET  "/refuse"    [] cerber.handlers/client-refuse-handler)
  (POST "/token"     [] cerber.handlers/token-handler)
  (GET  "/login"     [] cerber.handlers/login-form-handler)
  (POST "/login"     [] cerber.handlers/login-submit-handler))

(defroutes static-routes
  (route/resources "/styles"  {:root "styles"})
  (route/resources "/scripts" {:root "scripts"})
  (route/resources "/fonts"   {:root "fonts"})
  (route/resources "/img"     {:root "img"})
  (route/not-found "Not Found"))

(defroutes public-routes
  (GET "/" [] (fn [req] (selmer/render-file
                         "templates/index.html"
                         {:client (::ctx/client req)
                          :user   (::ctx/user req)}))))

(defroutes user-api-routes
  (GET "/users/me" [] (fn [req]
                        {:status 200
                         :body (::ctx/user req)})))

(defn api-routes
  [roles scopes->roles]
  (wrap-defaults
   (routes oauth2-routes (-> user-api-routes
                             (wrap-routes cerber.roles/wrap-permissions roles scopes->roles)
                             (wrap-routes cerber.handlers/wrap-authorized)))
   api-defaults))

(defn maybe-authorized-routes
  [roles scopes->roles]
  (routes (-> public-routes
               (wrap-routes cerber.roles/wrap-permissions roles scopes->roles)
               (wrap-routes cerber.handlers/wrap-maybe-authorized))))

;; initialization routines

(defn init-routes []
  (let [roles (cerber.roles/init-roles (:roles app-config))
        scopes->roles (:scopes->roles app-config)]

    (routes
     ;; API routes - authentication required
     (api-routes roles scopes->roles)

     ;; public routes - authentication is optional
     (maybe-authorized-routes roles scopes->roles)

     ;; OAuth2 provider routes
     oauth2-routes

     ;; static assets
     static-routes)))

(defn init-server []
  (log/infof "starting HTTP server on %s port" (-> app-config :http :port))

  ;; no caching for dev environment, please.

  (when-not (= (:env app-config) "prod")
    (selmer/cache-off!))

  (web/run-server (middleware/wrap-middlewares (init-routes)) (:http app-config)))


(defstate http-server
  :start (init-server)
  :stop  (http-server))

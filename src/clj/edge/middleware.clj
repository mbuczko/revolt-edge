(ns edge.middleware
  "Production and development Ring middlewares."

  (:require [clojure.stacktrace :refer [root-cause]]
            [clojure.tools.logging :as log]
            [compojure.response :refer [Renderable]]
            [edge.config :refer [app-config]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [selmer.middleware :refer [wrap-error-page]]))

(defn wrap-internal-error [handler & {:keys [error-response error-handler]}]
  (fn [req]
    (try (handler req)
         (catch Throwable t
           (when error-handler (error-handler t))
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body error-response}))))

(defn development-middlewares [handler]
  (log/info "applying development middlewares")
  (-> handler
      (wrap-error-page)
      (wrap-exceptions {:app-namespaces ["edge"]})
      (wrap-restful-format :formats [:json-kw]
                           :response-options {:json-kw {:pretty true}})))

(defn production-middlewares [handler]
  (log/info "applying production middlewares")
  (-> handler
      (wrap-internal-error :error-handler  #(log/error %)
                           :error-response {:error 500
                                            :error_description "Uh oh, something bad happened on our side ¯\\_(ツ)_/¯"})
      (wrap-restful-format :formats [:json-kw])))

(defn wrap-middlewares [handler]
  (if (= (:env app-config) "prod")
    (production-middlewares handler)
    (development-middlewares handler)))

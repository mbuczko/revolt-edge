(ns edge.middleware
  (:require [clojure.stacktrace :refer [root-cause]]
            [clojure.tools.logging :as log]
            [compojure.response :refer [Renderable]]
            [edge.config :refer [app-config]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.format :refer [wrap-restful-format]]
            [selmer.middleware :refer [wrap-error-page]]))

(defn wrap-internal-error [handler & {:keys [error-response error-response-handler]}]
  (fn [req]
    (try (handler req)
         (catch Throwable t
           (log/error (.printStackTrace t))
           {:status 500
            :headers {"Content-Type" "text/html"}
            :body (if error-response-handler
                    (error-response-handler req)
                    error-response)}))))

(defn development-middlewares [handler]
  (log/info "development middlewares applied")
  (-> handler
      (wrap-error-page)
      (wrap-exceptions {:app-namespaces ["edge"]})
      (wrap-restful-format :formats [:json-kw]
                           :response-options {:json-kw {:pretty true}})))

(defn production-middlewares [handler]
  (log/info "production middlewares applied")
  (-> handler
      (wrap-internal-error :log (fn [e] (log/error e))
                           :error-response {:error 500
                                            :error_description "Something bad happened."})
      (wrap-restful-format :formats [:json-kw])))

(defn wrap-middlewares [handler]
  (if (= (:env app-config) "prod")
    (production-middlewares handler)
    (development-middlewares handler)))

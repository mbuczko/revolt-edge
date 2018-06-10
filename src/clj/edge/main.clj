(ns edge.main
  (:gen-class)
  (:require [edge.system :as system]
            [clojure.tools.logging :as log]))

(defn -main [& args]
  (system/go)
  (log/info "Edge started successfully."))

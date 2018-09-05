(ns edge.oauth2
  (:require [edge.db :refer [db-conn]]
            [cerber.oauth2.core :as oauth2.core]
            [cerber.store :refer [close!]]
            [mount.core :refer [defstate]]))

(defstate client-store
  :start (oauth2.core/create-client-store :sql db-conn)
  :stop  (close! client-store))

(defstate user-store
  :start (oauth2.core/create-user-store :sql db-conn)
  :stop  (close! user-store))

(defstate token-store
  :start (oauth2.core/create-token-store :sql db-conn)
  :stop  (close! token-store))

(defstate authcode-store
  :start (oauth2.core/create-authcode-store :sql db-conn)
  :stop  (close! authcode-store))

(defstate session-store
  :start (oauth2.core/create-session-store :sql db-conn)
  :stop  (close! session-store))

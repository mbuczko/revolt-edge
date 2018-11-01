(ns edge.db
  (:require [conman.core :as conman]
            [mount.core :refer [defstate]]))

(defstate db-conn
  :start (and (Class/forName "org.h2.Driver")
              (conman/connect! {:init-size  1
                                :min-idle   1
                                :max-idle   4
                                :max-active 32
                                :jdbc-url "jdbc:h2:mem:testdb;MODE=MySQL;INIT=RUNSCRIPT FROM 'classpath:/db/migrations/h2/cerber_schema.sql'"}))
  :stop (conman/disconnect! db-conn))

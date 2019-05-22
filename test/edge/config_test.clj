(ns edge.config-test
  (:require [clojure.test :refer [deftest testing is]]
            [edge.config :refer [app-config]]
            [mount.core :as mount]))

(mount/start)

(deftest sample-test
  (testing "http server configuration"
    (let [http-config (:http app-config)]
      (is (= 8090 (:port http-config))))))

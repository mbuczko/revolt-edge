(ns ^:figweel-load edge.main
  (:require [reagent.core :as r]
            [edge.layout :as layout]))

(set! *warn-on-infer* true)

(enable-console-print!)
(r/render-component [layout/main-layout] (.getElementById js/document "container"))

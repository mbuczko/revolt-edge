(ns edge.layout
  (:require [antizer.reagent :as ant]
            [edge.components.info :as info]))

(defn main-layout
  []
  [ant/row
   [ant/col {:class "intro"}
    [info/success
     "Welcome to the EDGE"
     "This is a simple template to prove that all revolt tasks work as expected."]]])

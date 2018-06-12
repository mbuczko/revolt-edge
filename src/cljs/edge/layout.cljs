(ns edge.layout)

(defn main-layout
  []
  [:div.pure-g
   [:div.pure-u-1.intro
    [:h1 "Welcome to EDGE :)"]
    [:p "If you see this page and it reloads immediately according to changes made to .cljs or .scss files that means "
     [:span "revolt"]
     " works perfectly."]
    [:p "Enjoy your REPL-ing!"]]])

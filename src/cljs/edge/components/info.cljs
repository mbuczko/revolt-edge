(ns edge.components.info)

(defn success
  [title text]
  [:div.success
   [:div.swal-icon.swal-icon--success
    [:div.swal-icon--success__line.swal-icon--success__line--long]
    [:div.swal-icon--success__line.swal-icon--success__line--tip]
    [:div.swal-icon--success__ring]
    [:div.swal-icon--success__hide-corners]]
   [:h3.title title]
   [:p.text text]])

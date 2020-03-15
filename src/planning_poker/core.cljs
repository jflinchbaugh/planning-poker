(ns planning-poker.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]))

;; --------
;; State

(defonce selected (r/atom nil))

(def card-values (map str [0 (char 0x00BD) 1 2 3 5 8 13 20 40 100 \? (char 0x221E)]))

(defn select-card! [v]
  (reset! selected v))

;; -------------------------
;; Views

(defn card [v]
  [:div.big.card {:on-click #(select-card! nil)}[:span v]])

(defn cards [vs]
  [:div.grid
   (for [v vs]
     ^{:key v}
     [:div.small.card {:on-click #(select-card! v)} v])])

(defn home-page []
  (if @selected
    (card (str @selected))
    (cards card-values)))

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

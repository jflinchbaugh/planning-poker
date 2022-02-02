(ns planning-poker.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]))

;; --------
;; State

(def no-sleep (js/NoSleep.))

(defonce selected (r/atom nil))

(def card-values (map str [0 (char 0x00BD) 1 2 3 5 8 13 20 40 100 \? (char 0x221E)]))

(defn select-card! [v]
  (if v
    (.enable no-sleep)
    (.disable no-sleep))
  (reset! selected v))

;; -------------------------
;; Views

(defn card [v]
  (if (= v "100")
    [:button.big.card.one-hundred {:on-click #(select-card! nil)} [:span v]]
    [:button.big.card {:on-click #(select-card! nil)} [:span v]]))

(defn cards [vs]
  [:div.grid
   (for [v vs]
     ^{:key v}
     [:button.small.card {:on-click #(select-card! v)} [:span v]])])

(defn home-page []
  (if @selected
    [card (str @selected)]
    [cards card-values]))

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))

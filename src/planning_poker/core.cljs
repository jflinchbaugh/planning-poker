(ns planning-poker.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]
   [reitit.frontend :as rf]
   [reitit.frontend.easy :as rfe]
   [reitit.frontend.controllers :as rfc]))

;; --------
;; State

(def no-sleep (js/NoSleep.))

(defn allow-sleep! [] (.disable no-sleep))

(defn block-sleep! [] (.enable no-sleep))

(defonce selected (r/atom nil))

(defonce selected-series (r/atom :fib))

(defonce match (r/atom nil))

(def infinity-char (char 0x221E))

(def card-series
  {:fib (map str [0 (char 0x00BD) 1 2 3 5 8 13 20 40 100 \? infinity-char])
   :pow2 (map str [0 1 2 4 8 16 32 64 128 256 \? infinity-char])
   :bin2 (map str [0 1 10 100 1000 10000 \? infinity-char])})

(defn select-card! [v]
  (if v
    (block-sleep!)
    (allow-sleep!))
  (reset! selected v))

;; -------------------------
;; Views

(defn card [v]
  (if (> (count v) 2)
    [:button.big.card.one-hundred {:on-click #(select-card! nil)} [:span v]]
    [:button.big.card {:on-click #(select-card! nil)} [:span v]]))

(defn cards [vs]
  [:div.grid
   (for [v vs]
     ^{:key v}
     [:button.small.card {:on-click #(select-card! v)} [:span v]])])

(defn home-page []
  [:div.main
   (if @selected
     [card (str @selected)]
     (if @match
       [(-> @match :data :view)]
       [cards (card-series :fib)]))
   [:div
    [:a {:href (rfe/href ::cards-fib)} "fib"]
    " "
    [:a {:href (rfe/href ::cards-pow2)} "pow2"]]])

;; -------------------------
;; Routes

(def routes
  [["fib"
    {:name ::cards-fib
     :view (partial cards (card-series :fib))}]
   ["pow2"
    {:name ::cards-pow2
     :view (partial cards (card-series :pow2))}]
   ["bin2"
    {:name ::cards-bin2
     :view (partial cards (card-series :bin2))}]
   ])

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (rfe/start!
   (rf/router routes)
   (fn [new-match]
     (reset! match new-match)
     (home-page))
   {})
  (mount-root))

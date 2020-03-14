(ns planning-poker.prod
  (:require
    [planning-poker.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)

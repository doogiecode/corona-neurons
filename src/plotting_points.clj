(ns plotting-points
  "Plotting a path"
  (:require [clojure2d.core :as c2d]
            [fastmath.core :as m]
            [clojure2d.pixels :as p]
            [point-growth :as pg]))

#_(defn plot-points [points]
  (let [canvas (c2d/create-canvas 800 600)]
    (c2d/with-graphics canvas
      (doseq [[p1 p2] (partition 2 1 points)]
        (c2d/draw-line (shapes/point (first p1) (second p1))
                       (shapes/point (first p2) (second p2)))))
    (c2d/show canvas)))


(defn main [& args]
  (let [p (pg/approach-until-within
              [1 1] [599 599] 12 (/ m/PI 4) 18)]
    (c2d/with-oriented-canvas-> :bottom-left+
      (c2d/canvas 600 600 :low)
      #_(c2d/xor-mode :gray)
      (c2d/set-color :blue 160)
      (c2d/path p)
      (c2d/save "hi.png"))))

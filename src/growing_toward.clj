(ns growing-toward
  "One toward another"
  (:require [clojure2d.core :as c2d]
            [fastmath.core :as m]
            [clojure2d.pixels :as p]
            [point-growth :as pg]))


(def paths-to-draw (pg/two-sided-approach-until-within
                   [1750 50] [50 1750] 40 (* m/PI 8) 100))

(defn draw-fun-impl [canvas window frame state]
  (c2d/with-oriented-canvas :bottom-left+ [c canvas]
    (c2d/set-background c :black 0)
    (c2d/set-color c :blue)
    (c2d/set-stroke c 15)
    (c2d/path-bezier c (paths-to-draw 0))
    (c2d/set-color c :red)
    (c2d/set-stroke c 20 :round)
    (c2d/path-bezier c (paths-to-draw 1))
    (c2d/set-color c :white 150)
    (c2d/set-stroke c 11 :round)
    (c2d/path-bezier c (paths-to-draw 1))))

(defn draw-fun [c w f s]
  (draw-fun-impl c w f s))

(def canv (c2d/canvas 1800 1800 :highest))

(def wind (c2d/show-window
           {:canvas canv :window-name "REPL"
            :refresher :safe :fps 1 :w 900 :h 900
            :draw-fn draw-fun}))
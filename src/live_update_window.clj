(ns live-update-window
  "Real time"
  (:require [clojure2d.core :as c2d]
            [fastmath.core :as m]
            [clojure2d.pixels :as p]
            [point-growth :as pg]))

(def canv (c2d/canvas 1800 1800 :highest))

(def path-to-draw (pg/approach-until-within
                   [50 50] [1750 1750] 40 (/ m/PI 8) 80))

(defn draw-fun-impl [canvas window frame state]
  (c2d/with-oriented-canvas :bottom-left+ [c canvas]
    (c2d/set-background c :white)
    (c2d/set-color c :blue)
    (c2d/path c path-to-draw)))
(defn draw-fun [c w f s]
  (draw-fun-impl c w f s))

(def wind (c2d/show-window 
           {:canvas canv :window-name "REPL" 
            :refresher :safe :fps 1 :w 900 :h 900 
            :draw-fn draw-fun}))
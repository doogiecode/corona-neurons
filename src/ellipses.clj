(ns ellipses
  "Learning ellipses"
  (:require [clojure2d.core :as c2d]
            [fastmath.core :as m]
            [clojure2d.pixels :as p]))

(def ellipses 
  [[200 200 100 50 (/ m/PI 4)]
   [300 300 70 50 0]
   [400 400 60 30 (/ m/PI 8)]
   [500 500 60 30 (/ m/PI -8)]
   [1200 1200 500 350 (/ m/PI -12)]])

(defn draw-fun-impl [canvas window frame state]
  (c2d/with-oriented-canvas :bottom-left+ [c canvas]
    (c2d/set-background c :white)
    (doseq [e ellipses]
      (c2d/set-color c :blue)
      (c2d/push-matrix c)
      (c2d/translate c (e 0) (e 1))
      (c2d/rotate c (e 4))
      (c2d/ellipse c 0 0 (e 2) (e 3))
      (c2d/set-color c :white 170)
      (c2d/ellipse c 0 0 (e 2) (e 3))
      (c2d/set-color c :blue)
      (c2d/set-stroke c (/ (e 3) 12))
      (c2d/ellipse c 0 0 (* (e 2) 1) (* (e 3) 1) :true)
      (c2d/ellipse c 0 0 (/ (e 3) 3) (/ (e 3) 3))
      (c2d/pop-matrix c))))


(defn draw-fun [c w f s]
  (draw-fun-impl c w f s))

(def canv (c2d/canvas 1800 1800 :highest))

(def wind (c2d/show-window
           {:canvas canv :window-name "REPL"
            :refresher :safe :fps 1 :w 900 :h 900
            :draw-fn draw-fun}))
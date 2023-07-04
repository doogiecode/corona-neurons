(ns scratch
  "Just scratch"
  (:require [clojure2d.core :as c2d]
            [fastmath.core :as m]
            [clojure2d.pixels :as p]))


(defn main [& args] 
  (println (m/sqrt 4))
  (println (m/dist [0 0] [3 4]))
  (c2d/with-canvas-> 
    (c2d/canvas 200 200) 
    #_(c2d/xor-mode :gray)
    (c2d/set-color :blue 160) 
    (c2d/rect 10 10 10 10) 
    (c2d/rect 50 50 40 40) 
    (c2d/save "hi.png")))
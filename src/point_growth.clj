(ns point-growth 
  "Trying out growing"
  (:require [fastmath.core :as m]
            [fastmath.random :as r]))

(def seed-rng (r/rng :mersenne))
(def the-seed (r/irandom seed-rng 1 100000))
(println the-seed)
(def my-prng (r/rng :mersenne the-seed))


(defn p1-p2->angle [[p1x p1y] [p2x p2y]]
  (m/atan2 (- p2y p1y) (- p2x p1x)))


(defn p1-dist-angle->p2 [[p1x p1y] d a]
  [(+ p1x (* (m/cos a) d))
   (+ p1y (* (m/sin a) d))])


(defn angle-noise [a n]
  (+ a (* n (r/drandom my-prng -1.0 1.0))))

(defn p1-p2-dist-noise->approach
  [[p1x p1y] [p2x p2y] d n]
  (p1-dist-angle->p2
   [p1x p1y] d
   (angle-noise (p1-p2->angle [p1x p1y] [p2x p2y]) n)))


(defn approach-until-within
  [[p1x p1y] [p2x p2y] d n uw]
  (loop [start [p1x p1y]
         target [p2x p2y]
         distance d
         noise n
         envelope uw
         path []]
    (if (> uw (m/dist start target))
      (do
        (println (count path))
        (conj path start))
      (recur
       (p1-p2-dist-noise->approach start target distance noise)
       target
       distance
       noise
       envelope
       (conj path start)))))

(defn two-sided-approach-until-within
  [[p1x p1y] [p2x p2y] d n uw]
  (loop [start [p1x p1y]
         target [p2x p2y]
         distance d
         noise n
         envelope uw
         path1 []
         path2 []]
    (if (> uw (m/dist start target))
      (do
        (println (count path1))
        [(conj path1 start) (conj path2 target)])
      (recur
       (p1-p2-dist-noise->approach start target distance noise)
       (p1-p2-dist-noise->approach target start distance noise)
       distance
       noise
       envelope
       (conj path1 start)
       (conj path2 target)))))

(defn main [& args]
  (let [path (approach-until-within [1 1] [100 100] 2 (/ m/PI 4) 3)]
    (println path)))
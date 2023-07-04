(ns main
  "Maker of the Corona Neurons"
  (:require [clojure2d.core :as c2d]
            [fastmath.core :as m]
            [clojure2d.pixels :as p]
            [point-growth :as pg]))

(set! *warn-on-reflection* true)
(set! *unchecked-math* :warn-on-boxed)

; this was in some examples but it works without it 
; disabled after a divide by zero
#_(m/use-primitive-operators)


(def dist-inc 20)
(def noise (/ m/PI 4))
(def envelope 40)

; color tool: 
; https://bytethisstore.com/tools/pg/color-visualizer?color=%234b8494
; angles are unused after figuring out the trig way
; numbering matches up with annotated PDF logo in scratch_images
(def somata
  {; 1 e59a44 -> d8811e   216, 129, 30
   :1a1 [188 1139 200 25 (+ (/ m/PI 2) 2.499555672593644) 216 129 30]
   :1a2 [327 1274 200 25 (/ m/PI 1) 216 129 30]
   :1a3 [497 1366 200 25 (+ (/ m/PI 2) 1.9288775797324333) 216 129 30]
   :1a4 [675 1402 200 25 (/ m/PI 1) 216 129 30]
   :1a5 [843 1389 200 25 (/ m/PI 1) 216 129 30]
   :1a6 [1021 1325 200 25 (/ m/PI 1) 216 129 30]
   :1a7 [1145 1243 200 25 (/ m/PI 1) 216 129 30]
   ;   d24f5d -> be3040   190, 48, 64
   :1b1 [1262 1073 230 15 (/ m/PI 1) 190 48 64]
   :1b2 [1335 904 230 15 (/ m/PI 1) 190 48 64]
   :1b3 [1355 716 230 15 (/ m/PI 1) 190 48 64]
   :1b4 [1312 509 230 15 (/ m/PI 1) 190 48 64]
   :1b5 [1203 318 230 15 (/ m/PI 1) 190 48 64]
   :1b6 [1068 197 230 15 (/ m/PI 1) 190 48 64]
   :1b7 [885 114 230 15 (/ m/PI 1) 190 48 64]

   ; 2 488494 -> 376572   55, 101, 114
   :2a1 [356 361 150 25 (/ m/PI 1) 55 101 114]
   :2a2 [286 444 150 25 (/ m/PI 1) 55 101 114]
   :2a3 [231 549 150 25 (/ m/PI 1) 55 101 114]
   :2a4 [202 664 150 25 (/ m/PI 1) 55 101 114]
   :2a5 [195 750 150 25 (/ m/PI 1) 55 101 114]
   ;   d24f5d -> be3040   190, 48, 64
   :2b1 [927 1383 190 30 (/ m/PI 1) 190 48 64] ; m 917 1393
   :2b2 [1090 1313 190 30 (/ m/PI 1) 190 48 64]
   :2b3 [1234 1196 190 30 (/ m/PI 1) 190 48 64]
   :2b4 [1334 1039 190 30 (/ m/PI 1) 190 48 64]
   :2b5 [1395 875 190 30 (/ m/PI 1) 190 48 64]

   ; 3 ffee5e -> ffe92b  255, 233, 43
   :3a1 [364 1237 220 40 (/ m/PI 1) 255 233 43]
   :3a2 [541 1322 220 40 (/ m/PI 1) 255 233 43]
   :3a3 [752 1352 220 40 (/ m/PI 1) 255 233 43]
   :3a4 [929 1316 220 40 (/ m/PI 1) 255 233 43]
   :3a5 [1087 1228 220 40 (/ m/PI 1) 255 233 43]
   ;   243652 -> 141f2f 20, 31, 47
   :3b1 [1204 364 180 30 (/ m/PI 1) 20 31 47]
   :3b2 [1128 282 180 30 (/ m/PI 1) 20 31 47]
   :3b3 [993 190 180 30 (/ m/PI 1)  20 31 47]
   :3b4 [856 141 180 30 (/ m/PI 1) 20 31 47]
   :3b5 [701 122 180 30 (/ m/PI 1) 20 31 47]

   ; 4  243652 -> 141f2f 20, 31, 47
   :4a1 [454 284 190 40 (/ m/PI -19)   20 31 47] ; m 424 224
   :4a2 [586 220 190 40 (/ m/PI -40)   20 31 47] ; m 626 190
   :4a3 [743 199 190 40 (/ m/PI 21)   20 31 47]
   ;    e59a44 -> d8811e 216, 129, 30 
   :4b1 [1109 854 190 50 (/ m/PI -3)   216 129 30] ; m 1099 864
   :4b2 [1118 699 190 50 (/ m/PI 2.05) 216 129 30]
   :4b3 [1073 547 190 50 (/ m/PI 3.7)   216 129 30] ; m 1073 577
   
   ; 5  4b8494 -> 3a6672
   :5a1 [207 895  220 60 (/ m/PI 2.3) 58 102 114] ; measured 183 855
   :5a2 [281 1061 200 60 (/ m/PI 3.6) 58 102 114]
   :5a3 [398 1183 200 60 (/ m/PI 4.5) 58 102 114]
   :5a4 [553 1262 200 60 (/ m/PI 8)   58 102 114]
   :5a5 [739 1283 200 60 0            58 102 114]
   ;    d24f5d -> be3040
   :5b1 [807  1258 310 100 (/ m/PI -20) 190 48 64]
   :5b2 [992  1195 310 100 (/ m/PI -5) 190 48 64]
   :5b3 [1137 1056 310 110 (/ m/PI -3.8) 190 48 64]
   :5b4 [1239 847  310 110 (/ m/PI -2.1) 190 48 64]
   :5b5 [1235 620 310 110 (/ m/PI 2.1) 190 48 64]

   ; 6  3d6679 -> 2c4957   44, 73, 87
   :6a1 [553 317 180 40 (/ m/PI 1) 44 73 87]
   :6a2 [440 388 180 40 (/ m/PI 1) 44 73 87]
   :6a3 [337 527 180 40 (/ m/PI 1) 44 73 87]
   :6a4 [286 669 180 40 (/ m/PI 1) 44 73 87]
   :6a5 [287 807 180 40 (/ m/PI 1) 44 73 87]
   ;    ffee5e -> ffe92b  255, 233, 43
   :6b1 [1140 877 170 15 (/ m/PI 1) 255 233 43]
   :6b2 [1161 717 170 15 (/ m/PI 1) 255 233 43]
   :6b3 [1135 589 170 15 (/ m/PI 1) 255 233 43]
   :6b4 [1046 439 170 15 (/ m/PI 1) 255 233 43]
   :6b5 [913 340 170 15 (/ m/PI 1) 255 233 43]

   ; 7  587e5c -> 436046  67, 96, 70
   :7a1 [391 1035 170 70 (/ m/PI 1) 67 96 70]
   :7a2 [514 1136 170 70 (/ m/PI 1) 67 96 70]
   :7a3 [677 1186 170 70 (/ m/PI 1) 67 96 70] ; m 687 1186
   :7a4 [851 1167 170 70 (/ m/PI 1) 67 96 70]
   :7a5 [987 1119 170 70 (/ m/PI 1) 67 96 70]
   ;    efb75e -> eaa230  234, 162, 48
   :7b1 [1197 932 190 30 (/ m/PI 1) 234 162 48]
   :7b2 [1228 795 190 30 (/ m/PI 1) 234 162 48]
   :7b3 [1226 632 190 30 (/ m/PI 1) 234 162 48]
   :7b4 [1150 448 190 30 (/ m/PI 1) 234 162 48]
   :7b5 [1021 315 190 30 (/ m/PI 1) 234 162 48]
   
   ; 8? a6c776 -> 8eb852  142, 184, 82
   :8a1 [491 1187 300 65 (/ m/PI 1) 142 184 82]
   :8a2 [699 1255 300 65 (/ m/PI 1) 142 184 82]
   :8a3 [932 1221 300 65 (/ m/PI 1) 142 184 82]
   :8a4 [1154 1055 300 65 (/ m/PI 1) 142 184 82]
   :8a5 [1251 854 300 65 (/ m/PI 1) 142 184 82]
   ;    ffee5e -> ffe92b  255, 233, 43
   :8b1 [1120 322 80 25 (/ m/PI 1) 255 233 43]
   :8b2 [1067 281 80 25 (/ m/PI 1) 255 233 43]
   :8b3 [1000 238 80 25 (/ m/PI 1) 255 233 43]
   :8b4 [923 199 80 25 (/ m/PI 1) 255 233 43]
   :8b5 [823 167 80 25 (/ m/PI 1) 255 233 43]
   })

(def axons
  [[:1a1 :1b1] [:1a2 :1b2] [:1a3 :1b3] [:1a4 :1b4] [:1a5 :1b5] [:1a6 :1b6] [:1a7 :1b7]
   [:2a1 :2b1] [:2a2 :2b2] [:2a3 :2b3] [:2a4 :2b4] [:2a5 :2b5]
   [:3a1 :3b1] [:3a2 :3b2] [:3a3 :3b3] [:3a4 :3b4] [:3a5 :3b5]
   [:4a1 :4b3] [:4a2 :4b2] [:4a3 :4b1]
   [:5a1 :5b1] [:5a2 :5b2] [:5a3 :5b3] [:5a4 :5b4] [:5a5 :5b5]
   [:6a1 :6b1] [:6a2 :6b2] [:6a3 :6b3] [:6a4 :6b4] [:6a5 :6b5]
   [:7a1 :7b1] [:7a2 :7b2] [:7a3 :7b3] [:7a4 :7b4] [:7a5 :7b5] 
   [:8a1 :8b1] [:8a2 :8b2] [:8a3 :8b3] [:8a4 :8b4] [:8a5 :8b5]
   ])

(defn drawable-axons [axons somata]
  (map (fn [a]
         (let [s1 (somata (a 0))
               s2 (somata (a 1))
               paths (pg/two-sided-approach-until-within
                      [(s1 0) (s1 1)] [(s2 0) (s2 1)]
                      dist-inc noise envelope)
               paths2 (pg/two-sided-approach-until-within
                       [(s1 0) (s1 1)] [(s2 0) (s2 1)]
                       dist-inc noise envelope)
               da1 {:p (paths 0) :r (s1 5) :g (s1 6) :b (s1 7)}
               da2 {:p (paths 1) :r (s2 5) :g (s2 6) :b (s2 7)}
               da3 {:p (paths2 0) :r (s1 5) :g (s1 6) :b (s1 7)}
               da4 {:p (paths2 1) :r (s2 5) :g (s2 6) :b (s2 7)}]
           [da1 da2 da3 da4])) axons))

(def drawable-axons-cache (drawable-axons axons somata))

(defn draw-axon [canvas a]
  (c2d/with-oriented-canvas :bottom-left+ [c canvas]
    (c2d/set-color c (a :r) (a :g) (a :b) 100)
    (c2d/set-stroke c 20 :round)
    (c2d/path-bezier c (a :p))
    (c2d/set-color c :white 150)
    (c2d/set-stroke c 11 :round)
    (c2d/path-bezier c (a :p))))

(defn draw-axons [canvas]
  (c2d/with-oriented-canvas :bottom-left+ [c canvas]
    (c2d/set-background c :white)
    (doseq [axon-list drawable-axons-cache]
      (doseq [a axon-list]
        (draw-axon c a)))))

(defn draw-somata [canvas]
  (c2d/with-oriented-canvas :bottom-left+ [c canvas]
    #_(c2d/set-background c :white)
    (doseq [e (vals somata)]
      (c2d/set-color c (e 5) (e 6) (e 7) 90)
      (c2d/push-matrix c)
      (c2d/translate c (e 0) (e 1))
      ; logo center wasn't quite at (750, 750)
      (c2d/rotate c (+ (/ m/PI 2)
                       (pg/p1-p2->angle
                        [735 730] [(e 0) (e 1)])))
      (c2d/ellipse c 0 0 (e 2) (e 3))
      (c2d/set-color c :white 110)
      (c2d/ellipse c 0 0 (e 2) (e 3))
      (c2d/set-color c (e 5) (e 6) (e 7) 120)
      (c2d/set-stroke c (/ (e 3) 12))
      (c2d/ellipse c 0 0 (* (e 2) 1) (* (e 3) 1) :true)
      (c2d/ellipse c 0 0 (/ (e 3) 3) (/ (e 3) 3))
      (c2d/pop-matrix c))))



(defn draw-fun-impl [canvas window frame state]
  (draw-axons canvas)
  (draw-somata canvas))


(defn draw-fun [c w f s]
  (draw-fun-impl c w f s))

(def canv (c2d/canvas 1500 1500 :highest))


(defn run-save [& args]
  (draw-fun-impl canv nil nil nil)
  (c2d/save canv "main.png"))


; run all the above in a REPL then run this for a 1 FPS window with 
; live updates on data/function redefinitions
#_(def wind (c2d/show-window
             {:canvas canv :window-name "Corona Neurons"
              :refresher :safe :fps 1 :w 900 :h 900
              :draw-fn draw-fun}))
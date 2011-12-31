(ns clojurebreaker.game-test
  (:use [clojure.test.generative :only (defspec) :as test])
  (:require [clojure.test.generative.generators :as gen]
            [clojurebreaker.game :as game])
)

(defn matches
  "Given a score, returns total number of exact plus unordered
  matches."
  [score]
  (+ (:exact score) (:unordered score))
)

(defn scoring-is-symmetric
  [secret guess]
  (= (game/score secret guess) (game/score guess secret))
)

(defn scoring-is-bounded-by-number-of-pegs
  [secret guess]
  (<= 0 (matches (game/score secret guess)) (count secret))
)

(defn random-secret
  []
  (gen/vec #(gen/one-of :r :g :b :y) 4)
)

(defspec score-invariants
  game/score
  [^{:tag `random-secret} secret
   ^{:tag `random-secret} guess]
  (assert (scoring-is-symmetric secret guess))
  (assert (scoring-is-bounded-by-number-of-pegs secret guess))
)

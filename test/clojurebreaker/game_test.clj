(ns clojurebreaker.game-test
  (:require [clojure.test.generative.generators :as gen]
            [clojurebreaker.game :as game])
)

(defn random-secret
  []
  (gen/vec #(gen/one-of :r :g :b :y) 4)
)

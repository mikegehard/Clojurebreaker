(ns clojurebreaker.game
  (:require clojure.data)
  (:require [clojure.math.combinatorics :as comb])
)

(defn exact-matches
  "Given two collections, return the number of positions
    where the two collections contain equal items"
  [c1, c2]
  (let [[_ _ matches] (clojure.data/diff c1 c2)]
    (count (remove nil? matches))
  )
)

(defn unordered-matches
  "Given two collections, return a map where each key is an items
    in both collections, and each value is the number of times the
    value occurs in the collection with the fewest occurrences."
  [c1 c2]
  (let [f1 (select-keys (frequencies c1) c2)
        f2 (select-keys (frequencies c2) c1)]
    (merge-with min f1 f2)
  )
)

(defn score
  [secret guess]
  (let [exact (exact-matches secret guess)
        unordered (apply + (vals (unordered-matches secret guess)))]
    {:exact exact :unordered (- unordered exact)}
  )
)

(defn generate-turn-inputs
  "Generate all possible turn inputs for a clojurebreaker game
    with colors and n columns"
  [colors number-of-columns]
  (let [combinations (comb/selections colors number-of-columns)]
    (comb/selections combinations 2)
  )
)

(defn score-inputs
  "Given a sequence of turn inputs, return a lazy sequence of 
    maps with :secret, :guess and :score."
  [inputs]
  (map 
    (fn [[secret guess]]
      {:secret (seq secret)
       :guess (seq guess)
       :score (score secret guess)})
    inputs
  )
)

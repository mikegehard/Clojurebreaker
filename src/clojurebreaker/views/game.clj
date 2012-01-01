(ns clojurebreaker.views.game
  (:require [clojurebreaker.views.common :as common]
            [clojurebreaker.models.game :as game]
            [noir.session :as session])
  (:use [noir.core :only (defpage defpartial render)]
        [hiccup.form-helpers :only (form-to text-field submit-button)])
)

(defpartial board [{:keys [one two three four exact unordered]}]
  (when (and exact unordered)
    [:div "Exact:" exact "Unordered:" unordered]
  )
  (form-to [:post "/guess"]
    (text-field "one" one)
    (text-field "two" two)
    (text-field "three" three)
    (text-field "four" four)
    (submit-button "Guess")
  )
)

(defpage "/" {:as guesses}
  (when-not (session/get :game)
    (session/put! :game (game/create)))
  (common/layout (board (or guesses nil)))
)

(defpage [:post "/guess"] {:keys [one two three four]}
  (let [result (game/score (session/get :game) [one two three four])]
    (if (= (:exact result) 4)
      (do (session/remove! :game)
          (common/layout
            [:h2 "Congratulations, you have solved the puzzle!"]
            (form-to [:get "/"]
              (submit-button "Start A New Game"))
          )   
      )
      (do (session/flash-put! result)
          (render "/" {:one one :two two :three three :four four :exact (:exact result) :unordered (:unordered result)}) 
      )
    )
  )
)

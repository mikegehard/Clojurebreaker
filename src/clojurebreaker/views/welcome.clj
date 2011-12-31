(ns clojurebreaker.views.welcome
  (:require [clojurebreaker.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to clojurebreaker"]))

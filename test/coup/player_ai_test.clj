(ns coup.player-ai_test
  (:use midje.sweet)               ;; <<==
  (:require [coup.player-ai :as player-ai]))

(def player-a {:influence [:duke :captain]})
(def player-b {:influence [:assassin :ambassador]})
(def game-state {:players [player-a player-b]
                 :deck    [:contessa :duke :ambassador :captain :assassin
                           :duke :assassin :contessa :captain :ambassador]
                 :bank    50})

(facts "about make-decision"
       (fact "takes income by default"
             (player-ai/make-decision (assoc player-a :coins 0) game-state) => :income)
       (fact "coups when the player has 7 or more coins"
             (player-ai/make-decision (assoc player-a :coins 7) game-state) => :coup
             (player-ai/make-decision (assoc player-a :coins 8) game-state) => :coup))

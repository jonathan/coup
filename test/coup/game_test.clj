(ns coup.game_test
  (:use midje.sweet)               ;; <<==
  (:require [coup.game :as game]))

(fact "gen-game returns a collection of player-count size"
      (count (game/gen-game {:player-count 2})) => 2)

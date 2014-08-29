(ns coup.player-ai
  (:require [coup.rules :as rules]))

(defn read-input
  []
  (println "Player Actions:")
  (println "1. Income")
  (println "2. Foreign Aid")
  (print "Action (1-2): ")
  (read-line))

(defn choose-player
  [player game-state]
  (first (filter #(not= (get player :player-name) (get % :player-name)) (get game-state :players))))

(defn make-decision
  [player game-state]
  (if (>= (get player :coins) 7)
    [:coup player (choose-player player game-state)]
    [:income player]))

(ns coup.player-ai
  (:require [coup.rules :as rules]))

(defn read-input
  []
  (println "Player Actions:")
  (println "1. Income")
  (println "2. Foreign Aid")
  (print "Action (1-2): ")
  (read-line))

(defn make-decision
  [player game-state]
  (if (>= (get player :coins) 7)
    :coup
    :income))

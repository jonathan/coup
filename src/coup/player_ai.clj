(ns coup.player-ai
  (:require [coup.rules :refer :all]))

(defn read-input
  []
  (println "Player Actions:")
  (println "1. Income")
  (println "2. Foreign Aid")
  (print "Action (1-2): ")
  (read-line))

(defn- choose-player
  [game-state player]
  (first (filter #(not= (get player :player-name) (get % :player-name)) (get game-state :players))))

(defn- choose-influence
  [{:keys [influence]}]
  (first influence))

(defn- assassinate?
  [{:keys [influence coins]}]
  (if (and (> coins 2) (some #(= % :assassin) influence)) true false))

(defn make-decision
  [game-state player]
  (if (>= (get player :coins) 7)
    (let [player-b (choose-player game-state player)]
      [coup player player-b (choose-influence player-b)])
    [income player]))

(defn execute-action
  [[action & players]]
  (apply action players))

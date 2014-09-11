(ns coup.player-input
  (:use coup.rules)
  (:require [coup.player-ai :as player-ai]))

(defn- choose-action-options
  [{:keys [influence]}]
  (println "Your influence" influence)
  (println "Player Actions:")
  (println "1. Income")
  (println "2. Foreign Aid")
  (println "3. Tax")
  (println "4. Steal")
  (println "5. Assassinate")
  (println "6. Coup")
  (println "7. Exchange")
  (println "Action (1-7): "))

(defn- get-player
  [players player-name]
  (cond
    (some #(= player-name (get % :player-name)) players)
      (first (filter #(= player-name (get % :player-name)) players))
    :default nil))

(defn- choose-player
  [{:keys [players]} player]
  (println "Pick Player: " (map #(get % :player-name) (filter #(not= (get % :player-name) (get player :player-name)) players)))
  (get-player players (read-line)))

(defn- choose-influence
  [{:keys [influence]}]
  (println "Pick Influence: " influence)
  (keyword (read-line)))

(defn- choose-action
  [game-state player]
  (choose-action-options player)
  (let [choice (read-line)]
    (case choice
      "1" [income player]
      "2" [foreign-aid player]
      "3" [tax player]
      "4" [steal player (choose-player game-state player)]
      "5" [assassinate player (choose-player game-state player)]
      "6" [coup player (choose-player game-state player)]
      "7" [exchange player (choose-influence player) (get game-state :deck)]
      "Couldn't read input")))

(defn make-decision
  [game-state player]
  (if (get player :human)
    (choose-action player)
    (player-ai/make-decision game-state player)))

(defn execute-action
  [[action & players]]
  (apply action players))

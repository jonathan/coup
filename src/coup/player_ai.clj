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

(defn- coup?
  [{:keys [coins]}]
  (> coins 6))

(defn- assassinate?
  [{:keys [influence coins]}]
  (if (and (> coins 2) (some #(= % :assassin) influence)) true false))

(defn- tax?
  [{:keys [influence]}]
  (some #(= % :duke) influence))

(defn- exchange?
  [{:keys [influence]}]
  (some #(= % :ambassador) influence))

(defn- steal?
  [{:keys [influence]}]
  (some #(= % :captain) influence))

(defn- income? [] true)
(defn- foreign-aid? [] true)

(defn make-decision
  [game-state player]
  (if (coup? player)
    (let [player-b (choose-player game-state player)]
      [coup player player-b (choose-influence player-b)])
    [income player]))

(defn execute-action
  [[action & players]]
  (apply action players))

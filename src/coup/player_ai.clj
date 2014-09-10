(ns coup.player-ai
  (:use coup.rules))

(defn- choose-player
  [game-state player action]
  (first (filter #(not= (get player :player-name) (get % :player-name)) (get game-state :players))))

(defn- choose-influence
  [{:keys [influence]}]
  (first influence))

(defn- coup?
  [{:keys [coins]}]
  (if (> coins 6) #'coup nil))

(defn- assassinate?
  [{:keys [influence coins]}]
  (if (and (> coins 2) (some #(= % :assassin) influence)) #'assassinate nil))

(defn- tax?
  [{:keys [influence]}]
  (if (some #(= % :duke) influence) #'tax nil))

(defn- exchange?
  [{:keys [influence]}]
  (if (some #(= % :ambassador) influence) #'exchange nil))

(defn- steal?
  [{:keys [influence]}]
  (if (some #(= % :captain) influence) #'steal nil))

(defn- income? [player] #'income)
(defn- foreign-aid? [player] #'foreign-aid)

(defn- choose-action
  [player]
  (first (remove nil? (map #(% player) [coup? assassinate? tax? steal? exchange? foreign-aid? income?]))))

(defn make-decision
  [game-state player]
  (let [action (choose-action player)]
    (cond
      (some #(= % action) [#'assassinate #'coup])
        (let [player-b (choose-player game-state player action)]
             [action player player-b (choose-influence player-b)])
      (some #(= % action) [#'steal])
        (let [player-b (choose-player game-state player action)]
             [action player player-b])
      (some #(= % action) [#'exchange])
        [action player (get game-state :deck)]
      :default [action player])))



(ns coup.game
  (:require [coup.rules :as rules]
            [coup.player-ai :as player-ai]))

(defn gen-player
  [player-name]
  {:influence [(rand-nth rules/roles) (rand-nth rules/roles)]
   :coins 2
   :player-name (str player-name)})

(defn gen-players
  [names]
  (map gen-player names))

(defn gen-game
  [{:keys [player-names] :or {player-names ["player-a" "player-b"]} :as game-options}]
  (if-not (nil? (get :game-state game-options))
    (get :game-state game-options)
    {:players (vec (gen-players player-names))
     :bank (- 50 (* 2 (count player-names)))}))

(defn update-player
  "Updates a player hash-map in the game-state's players vec"
  [game-state player]
  (let [player-name (get player :player-name)
        player-idx (first (keep-indexed #(if (= player-name (get %2 :player-name)) %1) (get game-state :players)))]
  (assoc-in game-state [:players player-idx] player)))

(defn remove-players
  "Given the game-state, players without any influence are removed."
  [game-state]
  (assoc game-state :players (filter #(not= 0 (count (get % :influence))) (get game-state :players))))

(defn game-over?
  "Returns 'true' if there is only one player left. 'false' otherwise."
  [game-state]
  (if (= 1 (count (get-in game-state [:players]))) true false))

(defn next-player
  "Gets the next player in the lineup"
  [{:keys [players]} player]
    (let [player-name (get player :player-name)
          player-idx (.indexOf players player)]
    (if (= (count players) (inc player-idx))
      (first players)
      (get players (inc player-idx)))))

;(defn run-game
;  [{:keys [testing] :or {testing false} :as game-options}]
;  (loop [game-state (gen-game game-options)]
;    (if-not (game-over? game-state)
;      (if-not testing
;        (case (player-ai/read-input)
;          "1" "looks like you want to do Income"
;          "2" "Foreign Aid, eh?"
;          "Can't figure you out."))
;      (get-in game-state [:players 0 :player-name]))))

(ns coup.game
  (:require [coup.rules :as rules]
            [coup.player-ai :as player-ai]))

(def game-state (atom {}))

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
  (if (contains? game-options :game-state)
    (reset! game-state (get :game-state game-options))
    (reset! game-state {:players (vec (gen-players player-names))
                        :bank (- 50 (* 2 (count player-names)))})))

(defn player-index
  [{:keys [player-name]}]
  (first (keep-indexed #(if (= player-name (get %2 :player-name)) %1) (get @game-state :players))))

(defn update-player
  "Updates a player hash-map in the game-state's players vec"
  [player]
  (println "(from update-player) player:" player)
  (swap! game-state assoc-in [:players (player-index player)] player))

(defn update-players
  [players]
  (println "(from update-players) player:" players)
  (doseq [player players] (update-player player))
  @game-state)

(defn remove-players
  "Given the game-state, players without any influence are removed."
  []
  (swap! game-state assoc-in [:players] (filter #(not= 0 (count (get % :influence))) (get @game-state :players))))

(defn game-over?
  "Returns 'true' if there is only one player left. 'false' otherwise."
  []
  (if (= 1 (count (get-in @game-state [:players]))) true false))

(defn next-player
  "Gets the next player in the lineup"
  [player]
    (let [players (get @game-state :players)
          player-name (get player :player-name)
          player-idx (.indexOf players player)]
    (if (= (count players) (inc player-idx))
      (first players)
      (get players (inc player-idx)))))

(defn player-input
  [player]
  ;(case (player-ai/read-input)
  ;  "1" "looks like you want to do Income"
  ;  "2" "Foreign Aid, eh?"
  ;  "Can't figure you out.")
  (update-players (player-ai/execute-action (player-ai/make-decision player @game-state)))
  ; Remove any stale players from the game-state
  (remove-players)
  (next-player player))

(defn run-game
  [{:keys [testing] :or {testing false} :as game-options}]
  ; Need to initialize the game-state
  (gen-game game-options)

  ; Start the game loop with the first player
  (loop [player (first (get @game-state :players))]
    ; Remove any stale players from the game-state
    ;(remove-players)
    (if (game-over?)
      (first (get @game-state :players))
      ; Get the player's choice
      (recur (player-input player)))))

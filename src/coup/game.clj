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
    (reset! game-state (get game-options :game-state))
    (reset! game-state {:players (vec (gen-players player-names))
                        :deck    [:contessa :duke :ambassador :captain :assassin
                                  :duke :assassin :contessa :captain :ambassador]
                        :bank (- 50 (* 2 (count player-names)))})))

(defn- player-index
  [{:keys [player-name]}]
  (first (keep-indexed #(if (= player-name (get %2 :player-name)) %1) (get @game-state :players))))

(defn update-player
  "Updates a player hash-map in the game-state's players vec"
  [player]
  (swap! game-state assoc-in [:players (player-index player)] player))

(defn update-players
  [players]
  (doseq [player players] (update-player player))
  @game-state)

(defn remove-players
  "Given the game-state, players without any influence are removed."
  []
  (swap! game-state assoc-in [:players] (vec (filter #(not= 0 (count (get % :influence))) (get @game-state :players))))
  @game-state)

(defn game-over?
  "Returns 'true' if there is only one player left. 'false' otherwise."
  []
  (if (= 1 (count (get-in @game-state [:players]))) true false))

(defn next-player
  "Gets the next player in the lineup"
  [player]
    (let [players (get @game-state :players)]
    (if (= (count players) (inc (player-index player)))
      (first players)
      (get players (inc (player-index player))))))

(defn player-input
  [player]
  ;(case (player-ai/read-input)
  ;  "1" "looks like you want to do Income"
  ;  "2" "Foreign Aid, eh?"
  ;  "Can't figure you out.")
  (update-players (player-ai/execute-action (player-ai/make-decision @game-state player)))
  (next-player player))

(defn run-game
  [{:keys [testing] :or {testing false} :as game-options}]
  ; Need to initialize the game-state
  (gen-game game-options)

  ; Start the game loop with the first player
  (loop [player (first (get @game-state :players))]
    ; Remove any stale players from the game-state
    (remove-players)
    (if (game-over?)
      (first (get @game-state :players))
      ; Get the player's choice
      (recur (player-input player)))))

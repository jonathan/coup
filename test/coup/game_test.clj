(ns coup.game_test
  (:require [midje.sweet :refer :all]
            [coup.game :refer :all]))

(def test-game-state {:players [{:player-name "player-a"
                                 :coins 2
                                 :influence   [:contessa :assassin]}
                                {:player-name "player-b"
                                 :coins 2
                                 :influence   [:duke :captain]}]
                      :bank 48
                      :deck [:contessa :duke :ambassador :captain :assassin
                                  :duke :assassin :contessa :captain :ambassador]})


(fact "gen-game returns a collection of player-count size"
    (against-background (before :checks (reset! game-state {}) :after (reset! game-state {})))
    (count (:players (gen-game test-game-state))) => 2
    (get-in (gen-game {:player-names ["player-a" "player-b"]}) [:players 0 :player-name]) => "player-a")

(fact "player-index returns the index of the player object in the game-state"
      (against-background (before :checks (reset! game-state test-game-state)))
      (#'coup.game/player-index (first (:players @game-state))) => 0
      (#'coup.game/player-index (second (:players @game-state))) => 1)

(fact "run-game returns the winner of the game"
      (run-game {:game-state test-game-state :testing true}) => (contains {:player-name "player-b"}))

(fact "remove-players should remove a player from the game-state"
      (against-background (before :checks (reset! game-state (assoc-in test-game-state [:players 0 :influence] []))))
      (remove-players) => (assoc test-game-state :players (subvec (get test-game-state :players) 1)))

(fact "update-player updates a player object in the players hash"
      (against-background (before :checks (reset! game-state test-game-state)))
      (update-player {:player-name "player-b" :coins 2 :influence []}) => (assoc-in test-game-state [:players 1 :influence] []))

(fact "update-players updates all players"
      (against-background (before :facts (reset! game-state test-game-state)))
      (update-players [{:player-name "player-a" :influence [:assassin]}
                       {:player-name "player-b" :influence []}]) => {:players [{:player-name "player-a" :influence   [:assassin]}
                                                                               {:player-name "player-b" :influence   []}]
                                                                     :bank 48
                                                                     :deck [:contessa :duke :ambassador :captain :assassin
                                                                            :duke :assassin :contessa :captain :ambassador]})

(facts "about game-over?"
  (fact "the game is over when there is only one player left"
      (against-background (before :checks (reset! game-state {:players [:first]})))
      (game-over?) => true)
  (fact "the game is not over when there is more than one player left"
      (against-background (before :checks (reset! game-state {:players [:first :second]})))
      (game-over?) => false))

(facts "about next-player"
      (against-background (before :checks (reset! game-state test-game-state)))
      (fact "returns player-b for a 2 player game and given player-a"
            (next-player (first (get @game-state :players))) => (second (@game-state :players)))
       (fact "returns player-a for a 2 player game and given player-b"
             (next-player (second (get @game-state :players))) => (first (@game-state :players))))

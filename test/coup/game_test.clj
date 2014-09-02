(ns coup.game_test
  (:use midje.sweet)               ;; <<==
  (:require [coup.game :as game]))

(def game-state {:players [{:player-name "player-a"
                            :influence   [:contessa :assassin]}
                           {:player-name "player-b"
                            :influence   [:duke :captain]}]})

(fact "gen-game returns a collection of player-count size"
      (count (:players (game/gen-game {:player-names ["player-a" "player-b"]}))) => 2
      (get-in (game/gen-game {:player-names ["player-a" "player-b"]}) [:players 0 :player-name]) => "player-a")

(fact "run-game returns the winner of the game"
      (game/run-game {:testing true}) => (first (get game-state :players)))

(fact "remove-players should remove a player from the game-state"
      (game/remove-players (assoc-in game-state [:players 0 :influence] [])) => (assoc game-state :players (subvec (get game-state :players) 1))
      (game/remove-players (assoc-in game-state [:players 1 :influence] [])) => (assoc game-state :players (pop (get game-state :players))))

(fact "update-player updates a player object in the players hash"
      (game/update-player game-state {:player-name "player-b" :influence []}) => (assoc-in game-state [:players 1 :influence] []))

(fact "the game is over when there is only one player left"
      (game/game-over? {:players [:first]}) => true
      (game/game-over? {:players [:first :second]}) => false)

(facts "about next-player"
       (fact "returns player-b for a 2 player game and given player-a"
             (game/next-player game-state (first (get game-state :players))) => (second (game-state :players)))
       (fact "returns player-a for a 2 player game and given player-b"
             (game/next-player game-state (second (get game-state :players))) => (first (game-state :players))))

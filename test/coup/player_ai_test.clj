(ns coup.player-ai_test
  (:use midje.sweet coup.rules coup.player-ai))

(def player-a {:player-name "player-a"
               :coins 0
               :influence [:duke :captain]})
(def player-b {:player-name "player-b"
               :coins 0
               :influence [:assassin :ambassador]})
(def game-state {:players [player-a player-b]
                 :deck    [:contessa :duke :ambassador :captain :assassin
                           :duke :assassin :contessa :captain :ambassador]
                 :bank    50})

(facts "about make-decision"
       (fact "takes income by default"
             (make-decision game-state (assoc player-a :coins 0)) => [income (assoc player-a :coins 0)])
       (fact "coups when the player has 7 or more coins"
             (make-decision game-state (assoc player-a :coins 7)) => [coup (assoc player-a :coins 7) player-b (first (get player-b :influence))]
             (make-decision game-state (assoc player-a :coins 8)) => [coup (assoc player-a :coins 8) player-b (first (get player-b :influence))]))

(facts "about choose-player"
       (fact "the chosen player isn't the one passed in"
             (#'coup.player-ai/choose-player game-state player-a) => player-b))

(facts "about choose-influence"
       (fact "the chosen player picks an influence to discard"
             (#'coup.player-ai/choose-influence player-a) => :duke))

(facts "about execute-action"
      (fact "the income action and player-a should run the income action on them"
            (execute-action [income (assoc player-a :coins 0)]) => [(assoc player-a :coins 1)]))

(facts "about assassinate?"
       (fact "players shouldn't assassinate without the assassin role"
             (#'coup.player-ai/assassinate? player-a) =not=> true)
       (fact "players shouldn't assassinate with fewer than 3 coins"
             (#'coup.player-ai/assassinate? (assoc player-b :coins 0)) =not=> true
             (#'coup.player-ai/assassinate? (assoc player-b :coins 2)) =not=> true)
       (fact "player-b should return 'true' for assassinating someone"
             (#'coup.player-ai/assassinate? (assoc player-b :coins 3)) => true
             (#'coup.player-ai/assassinate? (assoc player-b :coins 4)) => true))

(facts "about coup?"
       (fact "players shouldn't coup without 7 coins"
             (#'coup.player-ai/coup? (assoc player-b :coins 0)) =not=> true)
       (fact "player-a should return 'true' for couping"
             (#'coup.player-ai/coup? (assoc player-a :coins 7)) => true))

(facts "about tax?"
       (fact "players shouldn't tax without the duke role"
             (#'coup.player-ai/tax? player-b) =not=> true)
       (fact "player-a should return 'true' for taxing"
             (#'coup.player-ai/tax? player-a) => true))

(facts "about exchange?"
       (fact "players shouldn't exchange without the ambassador role"
             (#'coup.player-ai/exchange? player-a) =not=> true)
       (fact "player-b should return 'true' for exchanging"
             (#'coup.player-ai/exchange? player-b) => true))

(facts "about steal?"
       (fact "players shouldn't steal without the captain role"
             (#'coup.player-ai/steal? player-b) =not=> true)
       (fact "player-b should return 'true' for stealing"
             (#'coup.player-ai/steal? player-a) => true))

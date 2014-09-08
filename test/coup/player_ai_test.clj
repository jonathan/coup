(ns coup.player-ai_test
  (:use midje.sweet coup.rules coup.player-ai))

(def player-a {:player-name "player-a"
               :coins 0
               :influence []})
(def player-b {:player-name "player-b"
               :coins 0
               :influence []})
(def game-state {:players [player-a player-b]
                 :deck    [:contessa :duke :ambassador :captain :assassin
                           :duke :assassin :contessa :captain :ambassador]
                 :bank    50})

(facts "about make-decision"
       (background (around :facts (let [test-player-a (assoc player-a :influence [:contessa])
                                        test-player-b (assoc player-b :influence [:contessa])
                                        test-game-state (assoc-in game-state [:players] [test-player-a test-player-b])] ?form)))
       (fact "takes foreign-aid by default"
             (make-decision test-game-state (assoc test-player-a :coins 0)) => [#'foreign-aid (assoc test-player-a :coins 0)])
       (fact "coups when the player has 7 or more coins"
             (make-decision test-game-state (assoc test-player-a :coins 7)) => [#'coup (assoc test-player-a :coins 7) test-player-b (first (get test-player-b :influence))]
             (make-decision test-game-state (assoc test-player-a :coins 8)) => [#'coup (assoc test-player-a :coins 8) test-player-b (first (get test-player-b :influence))]))

(facts "about choose-player"
       (fact "the chosen player isn't the one passed in"
             (#'coup.player-ai/choose-player game-state player-a #'coup) => player-b))

(facts "about choose-influence"
       (background (around :facts (let [test-player-a (assoc player-a :influence [:duke])] ?form)))
       (fact "the chosen player picks an influence to discard"
             (#'coup.player-ai/choose-influence test-player-a) => :duke))

(facts "about execute-action"
       (background (around :facts (let [test-player-a (assoc player-a :coins 0)] ?form)))
       (fact "the income action and player-a should run the income action on them"
             (execute-action [income test-player-a]) => [(assoc test-player-a :coins 1)]))

(facts "about assassinate?"
       (background (around :facts (let [test-player-a (assoc player-a :influence [:duke])
                                        test-player-b (assoc player-b :influence [:assassin])] ?form)))
       (fact "players shouldn't assassinate without the assassin role"
             (#'coup.player-ai/assassinate? test-player-a) => nil)
       (fact "players shouldn't assassinate with fewer than 3 coins"
             (#'coup.player-ai/assassinate? (assoc test-player-b :coins 0)) => nil
             (#'coup.player-ai/assassinate? (assoc test-player-b :coins 2)) => nil)
       (fact "player-b should return 'true' for assassinating someone"
             (#'coup.player-ai/assassinate? (assoc test-player-b :coins 3)) => #'assassinate
             (#'coup.player-ai/assassinate? (assoc test-player-b :coins 4)) => #'assassinate))

(facts "about coup?"
       (fact "players shouldn't coup without 7 coins"
             (#'coup.player-ai/coup? (assoc player-b :coins 0)) => nil)
       (fact "player-a should return #'coup for couping"
             (#'coup.player-ai/coup? (assoc player-a :coins 7)) => #'coup))

(facts "about tax?"
       (background (around :facts (let [test-player-a (assoc player-a :influence [:duke])
                                        test-player-b (assoc player-b :influence [:assassin])] ?form)))
       (fact "players shouldn't tax without the duke role"
             (#'coup.player-ai/tax? test-player-b) => nil)
       (fact "player-a should return #'tax for taxing"
             (#'coup.player-ai/tax? test-player-a) => #'tax))

(facts "about exchange?"
       (background (around :facts (let [test-player-a (assoc player-a :influence [:duke])
                                        test-player-b (assoc player-b :influence [:ambassador])] ?form)))
       (fact "players shouldn't exchange without the ambassador role"
             (#'coup.player-ai/exchange? test-player-a) => nil)
       (fact "player-b should return #'exchange for exchanging"
             (#'coup.player-ai/exchange? test-player-b) => #'exchange))

(facts "about steal?"
       (background (around :facts (let [test-player-a (assoc player-a :influence [:captain])
                                        test-player-b (assoc player-b :influence [])] ?form)))
       (fact "players shouldn't steal without the captain role"
             (#'coup.player-ai/steal? test-player-b) => nil)
       (fact "player-b should return #'steal for stealing"
             (#'coup.player-ai/steal? test-player-a) => #'steal))

(facts "about choose-action"
       (fact "should return 'assassinate' if the player has the assassin card and 3 coins"
             (#'coup.player-ai/choose-action (assoc player-b :influence [:assassin] :coins 3)) => #'assassinate)
       (fact "should tax if the player has the duke card"
             (#'coup.player-ai/choose-action (assoc player-a :influence [:duke])) => #'tax)
       (fact "should steal if the player has the captain card"
             (#'coup.player-ai/choose-action (assoc player-a :influence [:captain])) => #'steal)
       (fact "should exchange if the player has the ambassador card"
             (#'coup.player-ai/choose-action (assoc player-a :influence [:ambassador])) => #'exchange))

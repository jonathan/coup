(ns coup.player-ai_test
  (:use midje.sweet coup.rules coup.player-ai))

(def player-a {:player-name "player-a"
               :influence [:duke :captain]})
(def player-b {:player-name "player-b"
               :influence [:assassin :ambassador]})
(def game-state {:players [player-a player-b]
                 :deck    [:contessa :duke :ambassador :captain :assassin
                           :duke :assassin :contessa :captain :ambassador]
                 :bank    50})

(facts "about make-decision"
       (fact "takes income by default"
             (make-decision (assoc player-a :coins 0) game-state) => [income (assoc player-a :coins 0)])
       (fact "coups when the player has 7 or more coins"
             (make-decision (assoc player-a :coins 7) game-state) => [coup (assoc player-a :coins 7) player-b (first (get player-b :influence))]
             (make-decision (assoc player-a :coins 8) game-state) => [coup (assoc player-a :coins 8) player-b (first (get player-b :influence))]))

(facts "about choose-player"
       (fact "the chosen player isn't the one passed in"
             (choose-player player-a game-state) => player-b))

(facts "about choose-influence"
       (fact "the chosen player picks an influence to discard"
             (choose-influence player-a) => :duke))

(fact "about execute-action"
      (fact "the income action and player-a should run the income action on them"
            (execute-action [income (assoc player-a :coins 0)]) => (assoc player-a :coins 1)))

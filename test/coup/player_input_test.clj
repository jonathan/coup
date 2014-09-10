(ns coup.player-input_test
  (:use midje.sweet coup.rules coup.player-input))

(def player-a {:player-name "player-a"
               :coins 0
               :influence []})

(facts "about execute-action"
       (background (around :facts (let [test-player-a (assoc player-a :coins 0)] ?form)))
       (fact "the income action and player-a should run the income action on them"
             (execute-action [income test-player-a]) => [(assoc test-player-a :coins 1)]))

;(facts "about header"
;       (background (around :facts (let [test-player-a (assoc player-a :influence [:duke :captain])] ?form)))
;       (fact "the header should show the player's influence"
;             (#'coup.player-input/header test-player-a) => (contains (get test-player-a :influence))))


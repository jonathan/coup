(ns coup.rules_test
  (:require [midje.sweet :refer :all]
            [coup.rules :refer :all]))

(fact "income increases coins by 1"
      (income {:coins 0}) => [{:coins 1}])

(fact "foreign-aid increases coins by 2"
      (foreign-aid {:coins 0}) => [{:coins 2}])

(facts "about tax"
  (fact "decreases coins by 3"
        (tax {:coins 4}) => [{:coins 1}])
  (fact "reduces coins to 0 if less than 3"
        (tax {:coins 2}) => [{:coins 0}]))

(facts "about coup"
       (fact "decreases the first players coins by 7"
             (coup {:coins 7} {:influence [:duke]} :duke) => [{:coins 0} {:influence []}])
       (fact "decreases the second players influence by 1"
             (coup {:coins 7} {:influence [:duke :captain]} :duke) => [{:coins 0} {:influence [:captain]}]))

(facts "about influence"
       (fact "removes the given role"
             (#'coup.rules/remove-influence {:influence [:duke :captain]} :captain) => {:influence [:duke]}
             (#'coup.rules/remove-influence {:influence [:duke :captain]} :duke) => {:influence [:captain]}
             (#'coup.rules/remove-influence {:influence [:duke]} :duke) => {:influence []})
       (fact "adds the given role"
             (#'coup.rules/add-influence {:influence []} :duke) => {:influence [:duke]}
             (#'coup.rules/add-influence {:influence [:captain]} :duke) => {:influence [:captain :duke]}))

(facts "about assassinate"
       (fact "decreases the first players coins by 3"
             (assassinate {:coins 7} {:influence [:duke]} :duke) => [{:coins 4} {:influence []}])
       (fact "decreases the second players influence by 1"
             (assassinate {:coins 7} {:influence [:duke :captain]} :duke) => [{:coins 4} {:influence [:captain]}]))

(facts "about exchange"
       (fact "replaces an influence from a player with one from the deck"
             (exchange {:influence [:duke :captain]} :duke [:contessa]) => [{:influence [:captain :contessa]} [:duke]]
             (exchange {:influence [:duke :captain]} :duke [:contessa :contessa]) => [{:influence [:captain :contessa]} [:contessa :duke]]
             (exchange {:influence [:duke :duke]} :duke [:contessa :contessa]) => [{:influence [:duke :contessa]} [:contessa :duke]]))

(facts "about steal"
       (fact "inc the first players coins by 2 and dec the second players coins by 2"
             (steal {:coins 2} {:coins 2}) => [{:coins 4} {:coins 0}])
       (fact "if the second player only has one coin, only inc/dec by one"
             (steal {:coins 2} {:coins 1}) => [{:coins 3} {:coins 0}]))

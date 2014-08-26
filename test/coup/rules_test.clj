(ns coup.rules_test
  (:use midje.sweet)               ;; <<==
  (:require [coup.rules :as rules]))

(fact "income increases coins by 1"
      (rules/income {:coins 0}) => {:coins 1})

(fact "foreign-aid increases coins by 2"
      (rules/foreign-aid {:coins 0}) => {:coins 2})

(facts "about tax"
  (fact "decreases coins by 3"
        (rules/tax {:coins 4}) => {:coins 1})
  (fact "reduces coins to 0 if less than 3"
        (rules/tax {:coins 2}) => {:coins 0}))

(facts "about coup"
       (fact "decreases the first players coins by 7"
             (rules/coup {:coins 7} {:influence [:duke]} :duke) => [{:coins 0} {:influence []}])
       (fact "decreases the second players influence by 1"
             (rules/coup {:coins 7} {:influence [:duke :captain]} :duke) => [{:coins 0} {:influence [:captain]}]))

(facts "about influence"
       (fact "removes the given role"
             (rules/remove-influence {:influence [:duke :captain]} :captain) => {:influence [:duke]}
             (rules/remove-influence {:influence [:duke :captain]} :duke) => {:influence [:captain]}
             (rules/remove-influence {:influence [:duke]} :duke) => {:influence []}))

(facts "about assassinate"
       (fact "decreases the first players coins by 3"
             (rules/assassinate {:coins 7} {:influence [:duke]} :duke) => [{:coins 4} {:influence []}])
       (fact "decreases the second players influence by 1"
             (rules/assassinate {:coins 7} {:influence [:duke :captain]} :duke) => [{:coins 4} {:influence [:captain]}]))

(facts "about steal"
       (fact "inc the first players coins by 2 and dec the second players coins by 2"
             (rules/steal {:coins 2} {:coins 2}) => [{:coins 4} {:coins 0}]))

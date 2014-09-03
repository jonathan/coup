(ns coup.rules)

(def roles [:duke :assassin :ambassador :captain :contessa])
(def actions [:income :foreign-aid :coup :tax :assassinate :exchange :steal])

(defn- add-influence
  [player role]
  (update-in player [:influence] conj role))

(defn- remove-influence
  [player role]
  (update-in player [:influence] #(if (= (first %) role) (subvec % 1) [(first %)])))

(defn income
  [player]
  [(update-in player [:coins] inc)])

(defn foreign-aid
  [player]
  [(update-in player [:coins] + 2)])

(defn tax
  [player]
  [(update-in player [:coins] #(if (< % 3) 0 (- % 3)))])

(defn coup
  [player-a player-b role]
  [(update-in player-a [:coins] - 7)
   (remove-influence player-b role)])

(defn assassinate
  [player-a player-b role]
  [(update-in player-a [:coins] - 3)
   (remove-influence player-b role)])

(defn exchange
  [player role deck]
  [(remove-influence (add-influence player (peek deck)) role) (conj (pop deck) role)])

(defn steal
  [player-a player-b]
  (if (= 1 (:coins player-b))
    [(update-in player-a [:coins] inc)
      (update-in player-b [:coins] dec)]
    [(update-in player-a [:coins] + 2)
      (update-in player-b [:coins] - 2)]))


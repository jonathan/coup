(ns coup.rules)

(def roles [:duke :assassin :ambassador :captain :contessa])

(defn gen-player
  []
  {:influence [(rand-nth roles) (rand-nth roles)]
   :coins 0})

(defn gen-players
  [player-count]
  (repeatedly player-count gen-player))

(defn remove-influence
  [player role]
  (update-in player [:influence] #(if (= (first %) role) (subvec % 1) [(first %)])))

(defn income
  [player]
  (update-in player [:coins] inc))

(defn foreign-aid
  [player]
  (update-in player [:coins] + 2))

(defn tax
  [player]
  (update-in player [:coins] #(if (< % 3) 0 (- % 3))))

(defn coup
  [player-a player-b role]
  [(update-in player-a [:coins] - 7)
   (remove-influence player-b role)])

(defn assassinate
  [player-a player-b role]
  [(update-in player-a [:coins] - 3)
   (remove-influence player-b role)])

(defn steal
  [player-a player-b]
  [(update-in player-a [:coins] + 2)
   (update-in player-b [:coins] - 2)])


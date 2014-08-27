(ns coup.game
  (:require [coup.rules :as rules]))

(defn gen-player
  []
  {:influence [(rand-nth rules/roles) (rand-nth rules/roles)]
   :coins 0})

(defn gen-players
  [player-count]
  (repeatedly player-count gen-player))

(defn gen-game
  [{:keys [player-count] :as game-options}]
  (gen-players player-count))

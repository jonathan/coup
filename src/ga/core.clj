(ns ga.core)

;; Variables for evolving a solution
(def population-size 300)
(def tourney-size 6)
(def winner-size 2)
(def crossover-pct 90)
(def mutate-pct 10)
(def ideal
  '(\a \b \c \d \e \f \g \h \i \j \k \l \m \n \o \p \q \r \s \t \u \v \w \x \y \z))

(defn fitness
  "This function compares a chromosome seq to an ideal seq and returns a score
  of how close those two sequences are."
  [chromosome]
  (apply + (map #(if (= %1 %2) 1 0) ideal chromosome)))

(defn gen-individual
  "Create a hash-map that represents an individual. The keys are
  :chromosome and :fitness."
  []
  (let [chromosome (repeatedly (count ideal) #(rand-nth ideal))
        fit (fitness chromosome)]
    {:chromosome chromosome}))

(defn gen-population
  "Creates a sequence of individuals based on the passed in size."
  [size]
  (repeatedly size gen-individual))

(defn gen-tournaments
  "Creates a sequence of sequences where each sub-seq is a list of
  individuals. These individuals are 'competing' based on their fitness."
  [population]
  (concat
    (partition tourney-size population)
    (repeatedly (/ population-size winner-size) (fn [] (repeatedly tourney-size (fn [] (rand-nth population)))))
    (repeatedly (/ population-size winner-size) (fn [] (repeatedly tourney-size (fn [] (rand-nth population)))))))

(defn top-winners
  "Returns the a number of winners from the given tournament seq."
  [tournament]
  (let [tourney-results (map #(assoc % :fitness (fitness (:chromosome %))) tournament)]
    (take winner-size (sort-by :fitness > tourney-results))))

(defn crossover
  "Takes two individuals and determines if crossover should be performed
  on them based off of the crossover-pct. Returnes new individals if
  crossover should be performed or returns the original individuals otherwise"
  [{ind1 :chromosome} {ind2 :chromosome}]
  (if (> crossover-pct (rand-int 100))
    (let [cross-point (rand-int (count ideal))]
      (list {:chromosome (concat (take cross-point ind1) (drop cross-point ind2))}
            {:chromosome (concat (take cross-point ind2) (drop cross-point ind1))}))
    (list {:chromosome ind1}
          {:chromosome ind2})))

(defn mutate
  "Takes a population sequence and determines if the any of the individuals
  should be mutated. Returnes a new population with any mutated individuals."
  [population]
  (map (fn [ind]
            (if (> mutate-pct (rand-int 100))
            (let [chrom (seq (assoc (vec (:chromosome ind)) (rand-int (count ideal)) (rand-nth ideal)))
                  fitness (fitness chrom)]
              {:chromosome chrom
               :fitness fitness})
            ind)) population))

(defn found-max
  "Checks if any individual in the population matches the ideal."
  [population]
  (some #(= (count ideal) (:fitness %)) population))

(defn stopping-criteria
  "Checks if evolution for a given population and generation should continue."
  [population gen-num]
  (and (<= gen-num 200) (not (found-max population))))

(defn evolve
  "Evolves a solution closest to the ideal chromosome."
  []
  (loop [population (gen-population population-size)
         gen-num 0]
    (if (stopping-criteria population gen-num)
      (let [tourneys (gen-tournaments population)
            winners (map top-winners tourneys)
            new-pop (flatten (map #(crossover (first %) (second %)) winners))]
        (recur (mutate new-pop) (inc gen-num)))
      (first (filter #(= (count ideal) (:fitness %)) population)))))


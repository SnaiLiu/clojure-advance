(ns adventofcode.adventofcode-day6-10
  (:require [adventofcode.utils :as u]))

(defn reallocation-deltas
  "Find the reallocation solution: the delta of the positions, which
  should be reallocated. Return a list:
  '([position delta] ...)"
  [max-pos max-val banks-count]
  (let [last-pos         (dec banks-count)
        remain-len       (- last-pos max-pos)
        other-remain-len (- max-val remain-len)]
    (if (<= other-remain-len 0)
      (map #(do [% 1]) (range (inc max-pos)
                              (+ max-pos max-val 1)))
      (let [m            (mod other-remain-len banks-count)
            circle       (int (/ other-remain-len banks-count))
            after-concat (concat (range (inc max-pos) banks-count)
                                 (range 0 m))
            pos-count    (group-by identity after-concat)]
        (if (> circle 0)
          (map #(do [% (+ circle (count (get pos-count %)))])
               (range 0 banks-count))
          (map #(do [(first %) (count (last %))]) pos-count))))))

(defn reallocation-builder
  "The builder to build the process of the reallocation.
   Save the step number and its reallocation result."
  [banks]
  (let [banks-count   (count banks)
        indexed-banks (vec (map-indexed #(do [%1 %2]) banks))
        find-max      (fn [indexed-banks] (first (sort-by second > indexed-banks)))]
    (iterate (fn [{:keys [step indexed-banks histories] :as recorder}]
               (let [[max-pos max-val] (find-max indexed-banks)
                     banks             (assoc indexed-banks max-pos [max-pos 0])
                     reallocate-deltas (reallocation-deltas max-pos max-val banks-count)
                     new-banks         (reduce (fn [b [pos delta]]
                                                 (update b pos (fn [[p v]]
                                                                 [p (+ v delta)])))
                                               banks reallocate-deltas)]
                 (-> (update-in recorder [:histories indexed-banks] (fnil conj []) step)
                     (assoc :step (inc step) :indexed-banks new-banks))))
             {:histories     {}
              :step          0
              :indexed-banks indexed-banks})))

(defn reallocate-steps
  "--- Day 6: Memory Reallocation (Part One) ---
  In this area, there are sixteen memory banks; each memory bank can hold any number of blocks.
  The goal of the reallocation routine is to balance the blocks between the memory banks.
  The reallocation routine operates in cycles. In each cycle, it finds the memory bank with the
  most blocks (ties won by the lowest-numbered memory bank) and redistributes those blocks among
  the banks. To do this, it removes all of the blocks from the selected bank, then moves to the
  next (by index) memory bank and inserts one of the blocks. It continues doing this until it
  runs out of blocks; if it reaches the last memory bank, it wraps around to the first one.

  The debugger would like to know how many redistributions can be done before a blocks-in-banks
  configuration is produced that has been seen before.
  For example, imagine a scenario with only four memory banks:

  - The banks start with 0, 2, 7, and 0 blocks. The third bank has the most blocks,
    so it is chosen for redistribution.
  - Starting with the next bank (the fourth bank) and then continuing to the first bank,
    the second bank, and so on, the 7 blocks are spread out over the memory banks. The fourth,
    first, and second banks get two blocks each, and the third bank gets one back. The final
    result looks like this: 2 4 1 2.
  - Next, the second bank is chosen because it contains the most blocks (four).
    Because there are four memory banks, each gets one block. The result is: 3 1 2 3.
  - Now, there is a tie between the first and fourth memory banks, both of which have three blocks.
    The first bank wins the tie, and its three blocks are distributed evenly over the other three banks,
    leaving it with none: 0 2 3 4.
  - The fourth bank is chosen, and its four blocks are distributed such that each of the four banks
    receives one: 1 3 4 1.
  - The third bank is chosen, and the same thing happens: 2 4 1 2.

  At this point, we've reached a state we've seen before: 2 4 1 2 was already seen.
  The infinite loop is detected after the fifth block redistribution cycle,
  and so the answer in this example is 5.

  Given the initial block counts in your puzzle input, how many redistribution cycles must be
  completed before a configuration is produced that has been seen before?
  ------------------------------------------------"
  [banks]
  (-> (u/find-some #(identity (get (:histories %) (:indexed-banks %)))
                   (reallocation-builder banks))
      :step))

(comment
  (reallocate-steps [0 2 7 0]) ;;=> 5
  (reallocate-steps [4 10 4 1 8 4 9 14 5 1 14 15 0 15 3 5]) ;;=> 12841
  )

(defn see-again-steps
  "--- Day 6: Memory Reallocation (Part Two) ---
   Out of curiosity, the debugger would also like to know the size of the loop:
   starting from a state that has already been seen, how many block redistribution
   cycles must be performed before that same state is seen again?
   In the example above, 2 4 1 2 is seen again after four cycles, and so the answer
   in that example would be 4.

   How many cycles are in the infinite loop that arises from the configuration
   in your puzzle input?
   ----------------------------------"
  [banks]
  (let [{:keys [step indexed-banks histories]}
        (u/find-some #(identity (get (:histories %) (:indexed-banks %)))
                     (reallocation-builder banks))]
    (- step (first (get histories indexed-banks)))))

(comment
  (see-again-steps [4 10 4 1 8 4 9 14 5 1 14 15 0 15 3 5]) ;;=> 8038
  )

(defn tower-paths-builder
  "Build the path of each name.
  The return likes below:
  {\"name\" [\"parent-name\" \"name\"]}"
  [programs]
  (reduce (fn [p {:keys [name sub-programs] :as program}]
            (let [parent-path (get p name)
                  set-self    (if (get p name) p (assoc p name [name]))
                  set-subs    (reduce (fn [m sub-name]
                                        (assoc m sub-name
                                                 (concat (get set-self name) [sub-name])))
                                      set-self sub-programs)]
              set-subs))
          {} programs))

(defn tower-builder
  "The builder to build the tower of the programs, from the program."
  [programs root-program]
  (if-let [subs-p (:sub-programs root-program)]
    (assoc root-program :sub-programs
                        (->> (map #(do [% (tower-builder programs (get programs %))]) subs-p)
                             (into {})))
    root-program))

(defn tower-weight
  "The builder to build the tower of the programs, from the root program.
   The final tower(return) is like below:

   {:name 'name' ;; the name of this root-program
    :weight int ;; program-self-weight
    :sum-weight int ;; the total weight of the tower, whose root is this root-program
    :sub-programs [{:name 'sub-name1'
                    :weight int
                    :sum-weight int
                    :sub-programs ...}
                   {...}}}"
  [programs root-program]
  (if-let [sub-names (:sub-programs root-program)]
    (let [sub-tower (reduce (fn [r sub-name]
                              (let [sub-p (tower-weight programs (get programs sub-name))]
                                (-> (update r :sum-weight + (:sum-weight sub-p))
                                    (update :sub-programs conj sub-p))))
                            {:sum-weight   (:weight root-program)
                             :sub-programs []} sub-names)]
      (assoc root-program :sub-programs (:sub-programs sub-tower)
                          :sum-weight (:sum-weight sub-tower)))
    (assoc root-program :sum-weight (:weight root-program))))

(defn possible-programs
  "Find the possible unbalance programs from the sub-programs"
  [sub-programs]
  (let [convert-need-w (fn [{:keys [weight-set possible-programs] :as result}]
                         (if (= (count weight-set) 1)
                           possible-programs
                           (->> (map (fn [[w p]]
                                       (let [another-w (first (disj weight-set w))]
                                         [another-w p]))
                                     possible-programs)
                                (into {}))))]
    (->> (reduce (fn [{:keys [weight-set] :as r} {:keys [name weight sum-weight] :as sub-p}]
                   (if (weight-set sum-weight)
                     (update r :possible-programs dissoc sum-weight)
                     (-> (update r :weight-set conj sum-weight)
                         (assoc-in [:possible-programs sum-weight] sub-p))))
                 {:weight-set        #{}
                  :possible-programs {}} sub-programs)
         convert-need-w)))

(comment
  (possible-programs [{:sum-weight 1 :name :1}]) ;;=> {1 {:sum-weight 1 :name :1}}
  (possible-programs [{:sum-weight 1 :name :1} {:sum-weight 1 :name :2}]) ;;=> {}
  (possible-programs [{:sum-weight 1 :name :1} {:sum-weight 2 :name :2}])
  ;;=> {2 {:sum-weight 1 :name :1} 1 {:sum-weight 2 :name :2}}
  (possible-programs [{:sum-weight 1 :name :1} {:sum-weight 1 :name :2} {:sum-weight 2 :name :3}])
  ;;=> {1 {:sum-weight 2 :name :3}}
  (possible-programs nil) ;;=> {}
  )

(defn find-unbalance-program
  "Finde the unbalance-program，return the name and need-weight."
  [need-weight {:keys [name sum-weight weight sub-programs] :as tower}]
  (when (not= 0 (count sub-programs))
    (let [ps (possible-programs sub-programs)]
      (if (empty? ps)
        {:name name :need-weight (+ weight (- need-weight sum-weight))}
        (->> ps
             (mapv #(apply find-unbalance-program %))
             (filter identity)
             first)))))


(defn bottom-program
  "---- Day 7: Recursive Circus (Part One)---
   first you need to understand the structure of these towers. You ask each program
   to yell out their name, their weight, and (if they're holding a disc) the names of
   the programs immediately above them balancing on that disc. You write this information
   down (your puzzle input). Unfortunately, in their panic, they don't do this in an orderly
    fashion; by the time you're done, you're not sure which program gave which information.
    For example, if your list is the following:
    pbga (66)
    xhth (57)
    ebii (61)
    havc (66)
    ktlj (57)
    fwft (72) -> ktlj, cntj, xhth
    qoyq (66)
    padx (45) -> pbga, havc, qoyq
    tknk (41) -> ugml, padx, fwft
    jptl (61)
    ugml (68) -> gyxo, ebii, jptl
    gyxo (61)
    cntj (57)
    ...then you would be able to recreate the structure of the towers that looks like this:
                    gyxo
                  /
             ugml - ebii
           /      \\
          |        jptl
          |
          |        pbga
         /        /
    tknk --- padx - havc
         \\        \\
          |         qoyq
          |
          |         ktlj
           \\      /
              fwft - cntj
                   \\
                    xhth
    In this example, tknk is at the bottom of the tower (the bottom program), and is holding
    up ugml, padx, and fwft. Those programs are, in turn, holding up other programs; in this
    example, none of those programs are holding up any other programs, and are all the tops
    of their own towers. (The actual tower balancing in front of you is much larger.)

    Before you're ready to help them, you need to make sure your information is correct. What
    is the name of the bottom program?
    ----------------------------------
    each program structure like below:

    {:name \"fwft\"
     :weight 72
     :sub-programs #{ktlj cntj xhth}
     }

    the path of each program:
    {\"ktlj\" [\"fwft\" \"ktlj\"]
     \"cntj\" [\"fwft\" \"cntj\"]
     ...}
    "
  [programs]
  (->> (tower-paths-builder (vals programs))
       (filter #(= (first %) (first (last %))))
       ffirst))

(defn weight-for-balance
  "---- Day 7: Recursive Circus (Part Two)---
   For any program holding a disc, each program standing on that disc forms a sub-tower.
   Each of those sub-towers are supposed to be the same weight, or the disc itself isn't
   balanced. The weight of a tower is the sum of the weights of the programs in that tower.

   In the example above, this means that for ugml's disc to be balanced, gyxo, ebii, and
   jptl must all have the same weight, and they do: 61.
   However, for tknk to be balanced, each of the programs standing on its disc and all
   programs above it must each match. This means that the following sums must all be the same:
   ugml + (gyxo + ebii + jptl) = 68 + (61 + 61 + 61) = 251
   padx + (pbga + havc + qoyq) = 45 + (66 + 66 + 66) = 243
   fwft + (ktlj + cntj + xhth) = 72 + (57 + 57 + 57) = 243
   As you can see, tknk's disc is unbalanced: ugml's stack is heavier than the other two.
   Even though the nodes above ugml are balanced, ugml itself is too heavy: it needs to
   be 8 units lighter for its stack to weigh 243 and keep the towers balanced. If this change
   were made, its weight would be 60.
   Given that exactly one program is the wrong weight, what would its weight need to be to
   balance the entire tower?
   -------------------------------------------
   input programs:
   {\"name\" {:name \"name\"
              :weight 90
              :sub-programs [\"name1\" \"name2\"]}
    ...}"
  [programs]
  (let [root-name (bottom-program programs)
        root-program (get programs root-name)
        tower     (tower-weight programs root-program)]
    (find-unbalance-program (:sum-weight tower) tower)))

(comment
  (defn input-converse
    [handler]
    (fn [lines]
      (let [programs
            (->> (mapv (fn [line]
                         (let [arr          (clojure.string/split line #" ")
                               name         (first arr)
                               weight       (when-let [w-str (second arr)]
                                              (Long/parseLong (second (re-find #"\((\d+)\)$" w-str))))
                               sub-programs (when-let [sub-strs (next (nnext arr))]
                                              (->> (map (fn [sub-name]
                                                          (if-let [n (second (re-find #"^(.+),$" sub-name))]
                                                            n
                                                            sub-name)) sub-strs)
                                                   (filter identity)))]
                           [name {:name name :weight weight :sub-programs sub-programs}]))
                       lines)
                 (into {}))]
        (handler programs))))

  (u/handle-from-file "src/adventofcode/adventofcodeinputs/day7.txt"
                      (input-converse bottom-program))
  (u/handle-from-file "src/adventofcode/adventofcodeinputs/day7.txt"
                      (input-converse weight-for-balance))
  )
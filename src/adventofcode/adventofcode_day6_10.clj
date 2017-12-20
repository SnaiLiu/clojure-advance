(ns adventofcode.adventofcode-day6-10
  (:require [adventofcode.utils :as u]))

(defn reallocation-deltas
  "Find the reallocation solution: the delta of the positions, which
  should be reallocated. Return a list:
  '([position delta] ...)"
  [max-pos max-val banks-count]
  (let [last-pos (dec banks-count)
        remain-len (- last-pos max-pos)
        other-remain-len (- max-val remain-len)]
    (if (<= other-remain-len 0)
      (map #(do [% 1]) (range (inc max-pos)
                              (+ max-pos max-val 1)))
      (let [m (mod other-remain-len banks-count)
            circle (int (/ other-remain-len banks-count))
            after-concat (concat (range (inc max-pos) banks-count)
                                 (range 0 m))
            pos-count (group-by identity after-concat)]
        (if (> circle 0)
          (map #(do [% (+ circle (count (get pos-count %)))])
               (range 0 banks-count))
          (map #(do [(first %) (count (last %))]) pos-count))))))

(defn reallocation-builder
  "The builder to build the process of the reallocation.
   Save the step number and its reallocation result."
  [banks]
  (let [banks-count (count banks)
        indexed-banks (vec (map-indexed #(do [%1 %2]) banks))
        find-max (fn [indexed-banks] (first (sort-by second > indexed-banks)))]
    (iterate (fn [{:keys [step indexed-banks histories] :as recorder}]
               (let [[max-pos max-val] (find-max indexed-banks)
                     banks (assoc indexed-banks max-pos [max-pos 0])
                     reallocate-deltas (reallocation-deltas max-pos max-val banks-count)
                     new-banks (reduce (fn [b [pos delta]]
                                         (update b pos (fn [[p v]]
                                                         [p (+ v delta)])))
                                       banks reallocate-deltas)]
                 (-> (update-in recorder [:histories indexed-banks] (fnil conj []) step)
                     (assoc :step (inc step) :indexed-banks new-banks))))
             {:histories {}
              :step      0
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
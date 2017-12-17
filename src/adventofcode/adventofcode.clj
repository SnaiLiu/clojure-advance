(ns adventofcode.adventofcode
  "网站 http://adventofcode.com 的练习题")

(defn inverse-captcha
  "--- Day 1: Inverse Captcha ---
   The captcha requires you to review a sequence of digits (your puzzle input)
   and find the sum of all digits that match the next digit in the list. The
   list is circular, so the digit after the last digit is the first digit in the list.
   For example:
   1122 produces a sum of 3 (1 + 2) because the first digit (1) matches the
   second digit and the third digit (2) matches the fourth digit.
   1111 produces 4 because each digit (all 1) matches the next.
   1234 produces 0 because no digit matches the next.
   91212129 produces 9 because the only digit that matches the next one is the last digit, 9.
   ------------------------------------------------"
  [captcha]
  (let [captcha-seq (seq captcha)
        captcha-circular (concat captcha-seq (take 1 captcha-seq))]
    (loop [sum 0
           s captcha-circular]
      (if (<= (count s) 1)
        sum
        (recur (if (= (first s) (second s))
                    (+ sum (Long/parseLong (str (first s))))
                    sum)
               (rest s))))))

(comment
  (inverse-captcha "") ;; => 0
  (inverse-captcha nil) ;; => 0
  (inverse-captcha "1122") ;; => 3
  (inverse-captcha "1111") ;; => 4
  (inverse-captcha "1234") ;; => 0
  (inverse-captcha "91212129") ;; => 9
  )

(defn row-difference-checksum
  "--- Day 2: Corruption Checksum (Part One)---
   The spreadsheet consists of rows of apparently-random numbers. To make sure the
   recovery process is on the right track, they need you to calculate the
   spreadsheet's checksum. For each row, determine the difference between the largest
   value and the smallest value; the checksum is the sum of all of these differences.
   For example, given the following spreadsheet:
   5 1 9 5
   7 5 3
   2 4 6 8
   The first row's largest and smallest values are 9 and 1, and their difference is 8.
   The second row's largest and smallest values are 7 and 3, and their difference is 4.
   The third row's difference is 6.
   In this example, the spreadsheet's checksum would be 8 + 4 + 6 = 18.
   --------------------------------------------"
  [data]
  (reduce #(+ %1 (- (apply max %2) (apply min %2))) 0 data))

(comment
  (row-difference-checksum [[5 1 9 5] [7 5 3] [2 4 6 8]]) ;; => 18
  )

(defn find-some
  "find the first item in the coll, which match the pred."
  [pred coll]
  (when (seq coll)
    (if (pred (first coll))
      (first coll)
      (recur pred (next coll)))))

(defn row-divide-checksum
  "--- Day 2: Corruption Checksum (Part Two)---
   the goal is to find the only two numbers in each row where one evenly divides the
   other - that is, where the result of the division operation is a whole number. They
   would like you to find those numbers on each line, divide them, and add up each line's result.
   For example, given the following spreadsheet:
   5 9 2 8
   9 4 7 3
   3 8 6 5
   In the first row, the only two numbers that evenly divide are 8 and 2; the result of this division is 4.
   In the second row, the two numbers are 9 and 3; the result is 3.
   In the third row, the result is 2.
   In this example, the sum of the results would be 4 + 3 + 2 = 9.
   -------------------------------------------

   In this function, if there aren't such two numbers, the result of the row's division is 0."
  [data]
  (let [row-result (fn [sorted-row]
                     (loop [row sorted-row]
                       (if (seq row)
                         (if-let [hit (find-some #(int? (/ % (first row))) (next row))]
                           (/ hit (first row))
                           (recur (next row)))
                         0)))]
    (reduce #(+ %1 (row-result (sort %2))) 0 data)))

(comment
  (row-divide-checksum [[5 9 2 8] [9 4 7 3] [3 8 6 5]]) ;;=> 9
  )

(defn steps-checksum
  "--- Day 3: Spiral Memory (Part One)---
   Each square on the grid is allocated in a spiral pattern starting at a location
   marked 1 and then counting up while spiraling outward. For example, the first
   few squares are allocated like this:
   17  16  15  14  13
   18   5   4   3  12
   19   6   1   2  11
   20   7   8   9  10
   21  22  23---> ...
   While this is very space-efficient (no squares are skipped), requested data must
   be carried back to square 1 (the location of the only access port for this memory
   system) by programs that can only move up, down, left, or right. They always take
   the shortest path: the Manhattan Distance between the location of the data and square 1.
   For example:
   Data from square 1 is carried 0 steps, since it's at the access port.
   Data from square 12 is carried 3 steps, such as: down, left, left.
   Data from square 23 is carried only 2 steps: up twice.
   Data from square 1024 must be carried 31 steps.

   How many steps are required to carry the data from the square identified in your
   puzzle input all the way to the access port?
   -------------------------------------------"
  [number]
  (if (= number 1)
    0
    (let [circles-range (iterate (fn [[circle-index [start end]]]
                                   [(inc circle-index) [(inc end) (+ end (* circle-index 8))]])
                                 [1 [1 1]])
          [circle [start end]] (find-some #(<= (first (second %)) number (second (second %)))
                                          circles-range)
          half-side-len (- circle 1)
          offset-len    (mod (inc (- number start)) (* 2 half-side-len))
          step1 (Math/abs (- offset-len half-side-len))]
      (+ step1 (dec circle)))))

(comment
  (steps-checksum 1) ;;=> 0
  (steps-checksum 12) ;;=> 3
  (steps-checksum 23) ;;=> 2
  (steps-checksum 1024) ;;=> 31
  )

(defn first-larger-number
  "--- Day 3: Spiral Memory (Part Two)---
   the programs here clear the grid and then store the value 1 in square 1. Then, in the same
   allocation order as shown above, they store the sum of the values in all adjacent squares,
   including diagonals.
   So, the first few squares' values are chosen as follows:
   Square 1 starts with the value 1.
   Square 2 has only one adjacent filled square (with value 1), so it also stores 1.
   Square 3 has both of the above squares as neighbors and stores the sum of their values, 2.
   Square 4 has all three of the aforementioned squares as neighbors and stores the sum of their values, 4.
   Square 5 only has the first and fourth squares as neighbors, so it gets the value 5.
   Once a square is written, its value does not change. Therefore, the first few squares would receive the
   following values:
   147  142  133  122   59
   304    5    4    2   57
   330   10    1    1   54
   351   11   23   25   26
   362  747  806--->   ...
   What is the first value written that is larger than your puzzle input?
   -------------------------------------

   In this function, use the structure to build the whole squares:

     {:square [[0 0] 1] ;; the location and value of current square.
      :squares {[0 0] 1 ;; the location and value of all the squares.
                    [1 0] 1
                    ...
                    [x y] number}
      :circle [x y -x -y] ;; the current circle.
     }
   "
  [number]
  (let [find-next-direction (fn [[square-x square-y] [x y neg-x neg-y]]
                              (cond
                                (and (= square-y neg-y) (<= square-x x)) [1 0]
                                (and (= square-x x) (< square-y y)) [0 1]
                                (and (= square-y y) (> square-x neg-x)) [-1 0]
                                (and (= square-x neg-x) (> square-y neg-y)) [0 -1]
                                :default nil))
        checksum            (fn [all-squares [x y]]
                              (reduce #(+ %1 (all-squares [(+ x (first %2)) (+ y (last %2))] 0))
                                      0 #{[1 0] [1 1] [0 1] [-1 1] [-1 0] [-1 -1] [0 -1] [1 -1]}))
        all-squares         (iterate (fn [{:keys [square squares circle] :as spiral}]
                                       (let [[[c-x c-y] _] square
                                             [x y neg-x neg-y] circle
                                             inc-circle?     (and (= c-y neg-y) (= c-x x))
                                             curr-circle     (if inc-circle?
                                                               [(inc x) (inc y) (dec neg-x) (dec neg-y)]
                                                               circle)
                                             [delta-x delta-y] (find-next-direction (first square) circle)
                                             curr-square-loc [(+ delta-x c-x) (+ delta-y c-y)]
                                             curr-square-val (checksum squares curr-square-loc)]
                                         (-> (assoc spiral :square [curr-square-loc curr-square-val])
                                             (assoc :circle curr-circle)
                                             (assoc-in [:squares curr-square-loc] curr-square-val))))
                                     {:square  [[0 0] 1]
                                      :squares {[0 0] 1}
                                      :circle  [0 0 0 0]})]
    (-> (find-some #(> (last (:square %)) number) all-squares)
        :square
        last)))

(comment
  (first-larger-number 1) ;;=> 2
  (first-larger-number 2) ;;=> 4
  (first-larger-number 361527) ;;=> 363010
  )

(defn valid-passphrase-count
  "--- Day 4: High-Entropy Passphrases (Part One)---
   A new system policy has been put in place that requires all accounts to use a passphrase instead of
   simply a password. A passphrase consists of a series of words (lowercase letters) separated by spaces.

   To ensure security, a valid passphrase must contain no duplicate words.
   For example:
   aa bb cc dd ee is valid.
   aa bb cc dd aa is not valid - the word aa appears more than once.
   aa bb cc dd aaa is valid - aa and aaa count as different words.
   The system's full passphrase list is available as your puzzle input. How many passphrases are valid?
   -------------------------------------------------"
  [passphrases]
  (-> (filter #(apply distinct? (clojure.string/split % #" ")) passphrases)
      count))

(defn valid-passphrase-count2
  "--- Day 4: High-Entropy Passphrases (Part One)---
  --------------------------------------------------
  Another solution."
  [passphrases]
  (let [passphrase-valid? (fn [passphrase]
                            (loop [s (sort (clojure.string/split passphrase #" "))]
                              (if (= 1 (count s))
                                true
                                (if (= (first s) (second s))
                                  false
                                  (recur (next s))))))]
    (-> (filter #(passphrase-valid? %) passphrases)
        count)))

(comment
  (valid-passphrase-count ["1" "2"]) ;;=> 2
  (valid-passphrase-count2 ["1" "2"]) ;;=> 2
  (valid-passphrase-count ["1 1" "2"]) ;;=> 1
  (valid-passphrase-count2 ["1 1" "2"]) ;;=> 1

  (defn count-from-file
    "工具函数"
    [file-path count-fn]
    (with-open [^java.io.BufferedReader rdr (clojure.java.io/reader file-path :encoding "UTF-8")]
      (count-fn (line-seq rdr))))

  (count-from-file "src/adventofcode/adventofcodeinputs/high_entropy_passphrases.txt"
                   valid-passphrase-count)
  ;; => 466
  )

(defn advanced-valid-passphrase-count
  "--- Day 4: High-Entropy Passphrases (Part Two)---
   For added security, yet another system policy has been put in place. Now,
   a valid passphrase must contain no two words that are anagrams of each
   other - that is, a passphrase is invalid if any word's letters can be rearranged
   to form any other word in the passphrase.
   For example:
   abcde fghij is a valid passphrase.
   abcde xyz ecdab is not valid - the letters from the third word can be rearranged
                                  to form the first word.
   a ab abc abd abf abj is a valid passphrase, because all letters need to be used
                                  when forming another word.
   iiii oiii ooii oooi oooo is valid.
   oiii ioii iioi iiio is not valid - any of these words can be rearranged to form
                                      any other word.
   Under this new system policy, how many passphrases are valid?
   ---------------------------------------------------"
  [passphrases]
  (let [valid? (fn [passphrase]
                 (let [words (clojure.string/split passphrase #" ")
                       sorted-words (map #(apply str (sort %)) words)]
                   (apply distinct? sorted-words)))]
    (-> (filter #(valid? %) passphrases)
        count)))

(comment
  (advanced-valid-passphrase-count ["abcde fghij" "abcde xyz ecdab"]) ;;=> 1
  (count-from-file "src/adventofcode/adventofcodeinputs/high_entropy_passphrases.txt"
                   advanced-valid-passphrase-count) ;;=> 251
  )
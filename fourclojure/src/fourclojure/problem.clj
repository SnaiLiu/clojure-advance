(ns fourclojure.problem)

(defn last-n-element
  "倒数第n个元素"
  [coll n]
  (if (= n (count coll))
    (first coll)
    (last-n-element (rest coll) n)))

(defn last-n-element2
  [coll n]
  (loop [i 1 r coll]
    (if (= n (count r))
      (first r)
      (recur (inc i) (rest r)))))

(defn last-element1
  "倒数第一个元素"
  [coll]
  (last-n-element coll 1))

(defn penultimate1
  "倒数第二个元素"
  [coll]
  (last-n-element coll 2))

(defn penultimate2
  [coll]
  (last-n-element2 coll 2))

(defn sum-multiples-3or5
  "n以内，3或5的倍数之和"
  [n]
  (->> (range 1 n)
       (filter #(or (= 0 (mod % 3)) (= 0 (mod % 5))))
       (apply +)))

(defn nth-element1
  "获取序列中第n个元素"
  [coll n]
  (loop [i 0 r coll]
    (if (= n i)
      (first r)
      (recur (inc i) (rest r)))))

(defn count-sequence
  "获取一个队列的长度"
  [coll]
  (loop [i 1 r coll]
    (if (next r)
      (recur (inc i) (next r))
      i)))

(defn reverse-sequence
  "反转队列"
  [coll]
  (loop [i (count coll) tmp-coll coll r '()]
    (if (first tmp-coll)
      (recur (dec i) (rest tmp-coll) (conj r (first tmp-coll)))
      r)))

(defn sum-up
  "序列求和"
  [coll]
  (apply + coll))
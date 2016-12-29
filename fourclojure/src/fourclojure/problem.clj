(ns fourclojure.problem)

(defn last-n-element
  "倒数第n个元素"
  [coll n]
  (if (= n (count coll))
    (first coll)
    (last-n-element (rest coll) n)))

(defn last-element1
  "倒数第一个元素"
  [coll]
  (last-n-element coll 1))

(defn penultimate1
  "倒数第二个元素"
  [coll]
  (last-n-element coll 2))

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


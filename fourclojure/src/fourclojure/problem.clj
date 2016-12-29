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

(defn odd-numbers
  "找出序列中的所有奇数的数， 没有则返回空序列'()"
  [coll]
  (filter odd? coll))

;;F(1)=F(2)=1,F(n)=F(n-1)+F(n-2) (n≥3)


(defn fibonacci
  "求斐波那契数"
  [n]
  (if (or (= 1 n) (= 2 n))
    1
    (+ (fibonacci (dec n)) (fibonacci (- n 2)))))

(defn fibonacci-sequence
  "求斐波那切数列"
  [n]
  (let [ir (range 1 (+ 1 n))]
    (map fibonacci ir)))

(defn palindrome?
  "监测一个序列是否是回文结构
  #(= (vec %) (reverse %))"
  [coll]
  (if (string? coll)
    (= (apply str (reverse coll)) coll)
    (= (reverse coll) coll)))

(defn max-val
  "求序列中最大值
  简洁答案：#(last (sort %&))"
  [& coll]
  (let [tmp-max-fn (fn [tmp-max val]
                    (if (> val tmp-max)
                      val
                      tmp-max))]
    (loop [i (count coll) tmp-max (first coll) r coll]
      (if (= 1 i)
        tmp-max
        (recur (dec i) (tmp-max-fn tmp-max (first r)) (rest r))))))

(defn caps
  "取大写字母
  #(.replaceAll % \"[^A-Z]\" \"\")"
  [chars]
  (let [upper-letter #{\A \B \C \D \E \F \G \H \I \J \K \L \M \N \O \P \Q
                       \R \S \T \U \V \W \X \Y \Z}]
    (->> (filter #(contains? upper-letter %) (vec chars))
         (apply str))))
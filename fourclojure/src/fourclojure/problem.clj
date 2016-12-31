(ns fourclojure.problem)

(defn last-n-element
  "倒数第n个元素"
  [coll n]
  (if (= n (count coll))
    (first coll)
    (last-n-element (rest coll) n)))

;; 点评：
;; 带函数名称的递归，一定会有堆栈溢出的问题，而使用clojure的recur函数，则不会存在这种问题；
;; recur是clojure中处理递归的利器，效率非常之高，而且不会存在堆栈溢出问题，因为它不会记录
;; 堆栈，而是将上一次的全部送给下一次。recur表示递归到函数入口或loop入口，因此这里可以直接
;; 使用递归。


(defn last-n-element2
  [coll n]
  (loop [i 1 r coll]
    (if (= n (count r))
      (first r)
      (recur (inc i) (rest r)))))

;; 点评：
;; 函数的参数顺序，和下面i r的参数顺序不一致。这是一个不好的习惯。
;; 程序中一旦出现1这种魔数，就会亮起红灯，因为魔数总是一个可怕的东西。
;; 用1作为一个边界检验的值，也是不好的，因为自然边界的值应该是0才对。看到1这种边界，必须必须仔细
;; 进行边界测试。

(defn last-element1
  "倒数第一个元素"
  [coll]
  (last-n-element coll 1))

;; 更好的答案：(comp peek vec)
;; peek函数，从一个栈中，拿出最后一个元素
;; pop函数，返回出去最后一个元素的剩下的栈

(defn penultimate1
  "倒数第二个元素"
  [coll]
  (last-n-element coll 2))

;; 更好的答案：(comp peek pop vec)

(defn penultimate2
  [coll]
  (last-n-element2 coll 2))

(defn sum-multiples-3or5
  "n以内，3或5的倍数之和"
  [n]
  (->> (range 1 n)
       (filter #(or (= 0 (mod % 3)) (= 0 (mod % 5))))
       (apply +)))

;; 函数的通用性和灵活性抽象得不够；
;; (1) (range 1 n)的使用，直接限制了函数能够处理的范围，为何不直接传一个coll进来？
;; (2) n这种东西出现之后，都要提高警惕：它是一个限制，限制我们程序灵活性的东西，比如限制我们
;;     程序处理序列的上限。
;; (3) 如果要达到更高的通用性，3 5这种魔数是不应该出现的。

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

;; 这是一个错误的答案
;; 如上述所说，1这种非自然的边界数据的出现，就是红灯警告，必须仔细进行边界条件测试
;; 正确答案如下：
(defn count-sequence
  [coll]
  (loop [r coll i 0]
    (if (first r)
      (recur (next r) (inc i))
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

;; 最优美的实现：
(defn fibonacci-sequence
  [n]
  (->> (iterate (fn [[a b]] [b (+' a b)]) [1 1])
       (map first)
       (take n)))

;; 其中iterate是clojure实现无限序列的利器，使用iterate的时候，要注意，函数中的参数必须
;; 包含函数所需要的所有信息，并且它的第二个参数只能是一个值，也即是说，第一个参数（函数）的参数也是一个值，
;; 所以我们要构造一个数据结构。对于菲波那切数列，其实每一个斐波那切数都是需要两个数来表达的，抓住
;; 这个本质，我们将已知的两个数包在一个数组中[1 1]，通过iterate我们就得到了一个用[1 1] [1 2]...
;; 表达的斐波那契数列，只是前后元素重复，所以用map first取第一个，并返回一个序列。

;; iterate 和map是惰性函数，也就是说，虽然iterate返回的是一个无限的数列，但它并不是真正存在于内存中的 ，
;; 如果你的n为1000，它只会计算出前1000个数的值。
;; +'表示的是clojure中的安全加法，及可以计算bigint。但因为效率被人诟病，没有取代普通+.

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

(defn duplicate-sequence
  "32: duplicate a sequence
  参考答案：#(interleave % %)
  其中，interleave函数，返回各个coll的第一个元素，再第二个元素，再第三个元素..."
  [coll]
  (->> coll
       (map #(do [% %]))
       (apply concat '())))

(defn my-range
  "34：实现range函数"
  [start end]
  (->> (iterate inc start)
       (take (- end start))))

(defn compress-sequence
  "30：序列去重
  参考答案：#(map last (partition-by list %))"
  [coll]
  (let [v (vec coll)]
    (->> (map #(do [%1 %2]) (pop v) (rest v))
         (filter #(not= (first %) (last %)))
         (mapv first)
         (#(conj % (peek v))))))

(defn cal-factorials
  "42: Write a function which calculates factorials(阶乘)."
  [n]
  (->> (range 1 (+ 1 n))
       (apply *')))
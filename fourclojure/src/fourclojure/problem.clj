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
    (->> (map list (pop v) (rest v))
         (filter #(not= (first %) (last %)))
         (mapv first)
         (#(conj % (peek v))))))

(defn cal-factorials
  "42: Write a function which calculates factorials(阶乘)."
  [n]
  (->> (range 1 (+ 1 n))
       (apply *')))

(defn interleave-seqs
  "39: Write a function which takes two sequences and returns the first item from each,
  then the second item from each, then the third, etc.
  参考答案： mapcat list
  clojure自带函数：interleave"
  [coll1 coll2]
  (->> (map #(do [%1 %2]) coll1 coll2)
       (apply concat)))

;(defn __
;  "28: Write a function which flattens a sequence.
;  clojure自带函数：flatten"
;  [coll]
;  ()
;  )

(defn replicate-sequence
  "33: Write a function which replicates each element of a sequence a variable number of times.
  参考答案：(mapcat #(repeat n %) coll)"
  [coll n]
  (let [copy-fn (fn [x]
                  (loop [r [] i n]
                    (if (= 0 i)
                      r
                      (recur (conj r x) (dec i)))))]
    (mapcat copy-fn coll)))

(defn interpose-sequence
  "clojure自带函数：interpose
  40: Write a function which separates the items of a sequence by an arbitrary value."
  [item coll]
  (->> (repeat (count coll) item)
       (interleave coll)
       butlast))

(defn prime?
  "判断一个数是否是素数"
  [x]
  (let [sqrted-x (int (Math/sqrt x))
        target-numbers (range 2 (+ 1 sqrted-x))]
    (not (or (= 1 x)
             (some #(= 0 (mod x %)) target-numbers)))))

(defn prime-numbers1
  "求1到n的所有素数——常规方法"
  [n]
  (->> (range 1 (+ n 1))
       (filter prime?)))

(defn prime-numbers
  "筛法求1到n的所有哦素数
  3w以内0.6s
  5w就堆栈溢出了"
  [n]
  (let [src-seq (range 2 (+ 1 n))]
    (loop [i-prime (first src-seq) r src-seq out-seq []]
      (if (empty? r)
        out-seq
        (recur (second r) (filter #(not= 0 (mod % i-prime)) r) (conj out-seq i-prime))))))

;(defn pcs [n sm]
;  (reduce #(update % (+ %2 n) conj %2) (dissoc sm n) (sm n)))
;
;(pcs 4 {4 [2] 6 [3]})
;
;(defn sieve
;  [[n sm]]
;  (let [sm (assoc sm (+ n n) conj n)]
;    (if (sm (inc n))
;      [(inc n) (pcs (inc n) sm)]
;      [(inc n) sm])))

(defn seive  [[n sm]]
  (if-let  [factors  (sm n)]
    [(inc n) (reduce #(update %1 (+ n %2) conj %2) (dissoc sm n) factors)]
    [(inc n) (assoc sm  (+ n n) #{n}) true]))

(defn primes []
  (sequence (comp (filter (fn [[_ _ p]] p))
                  (map (fn [[n]] (dec n))))
            (iterate seive [2 {}])))

(defn pack-sequence
  "31: pack a sequence"
  [coll]
  (reduce (fn [r x]
            (let [last-coll (last r)]
              (if (= x (last last-coll))
                (conj (vec (butlast r)) (concat last-coll (list x)))
                (conj (vec r) (list x)))))
          '()
          coll))

(defn drop-nth-item
  "41: Write a function which drops every Nth item from a sequence.
  参考答案：#(flatten (partition (- %2 1) %2 nil %1))"
  [coll n]
  (loop [f [] r coll]
    (if (< (count r) n )
      (concat (reduce #(concat %1 (butlast %2)) '() f) r)
      (recur (conj f (first (split-at n r))) (second (split-at n r))))))

(defn split-sequence
  "49: Write a function which will split a sequence into two parts.
  参考答案：(juxt take drop)
  juxt 用法：((juxt a b c) x) => [(a x) (b x) (c x)]"
  [n coll]
  (loop [f [] r coll]
    (if (= n (count f))
      [f r]
      (recur (conj f (first r)) (rest r)))))

(defn map-construction
  "61: Write a function which takes a vector of keys and a vector of values and constructs a map from them.
  clojure自带函数：zipmap
  参考答案：#(into {} (map vector % %2))
  赞，因为map的每一个key-val,其实就是一个[k v]
  "
  [keys vals]
  (->> (interleave keys vals)
       (partition 2)
       (reduce #(assoc %1 (first %2) (last %2)) {})))

(defn greatest-common-divisor
  "66: Given two integers, write a function which returns the greatest common divisor.
  参考答案：#(if (= 0 %2) % (recur %2 (mod % %2)))
  辗转相除法基于如下原理：两个整数的最大公约数等于其中较小的数和两数的差的最大公约数。"
  [x y]
  (->> (min x y)
       (iterate dec)
       (filter #(= 0 (mod x %) (mod y %)))
       first))

(defn my-iterate
  "62: Given a side-effect free function f and an initial value x write a function which returns an infinite
  lazy sequence of x, (f x), (f (f x)), (f (f (f x))), etc.
  clojure自带函数：iterate
  参考答案：(fn i [f x] (lazy-cat [x] (i f (f x))))"
  [f val]
  (lazy-seq
    (cons val (my-iterate f (f val)))))

(defn simple-clourse
  "107: 闭包"
  [x]
  (fn [n]
    (apply * (repeat x n))))

(defn cartesian-product
  "90: cartesian-product
  参考答案：#(set (for [x % y %2] [x y]))"
  [set1 set2]
  (->> (map (fn [val]
              (map #(do [% val]) set1))
            set2)
       (reduce into #{})))

(defn recognize-cards
  "128
  参考答案，基本一致，但直接用zipmap就能实现rank-m了"
  [[s r]]
  (let [suits {\D :diamond
              \H :heart
              \C :club
              \S :spade}
        original-ranks "23456789TJQKA"
        rank-m (->> (range (count original-ranks))
                    (map vector original-ranks)
                    (into {}))]
    {:suit (suits s) :rank (rank-m r)}))

(defn product-digits
  "99: Write a function which multiplies two numbers and returns the result as a sequence of its digits."
  [x y]
  (->> (str (* x y))
       (map #(Integer/parseInt (str %)))))

(defn group-seq
  "参考答案：#(apply merge-with into (for [v %2] {(% v) [v]}))
  clojure自带函数：group-by"
  [f seqs]
  (reduce (fn [r v]
            (let [result (f v)]
              (if-let [val (r result)]
                (assoc r result (conj val v))
                (assoc r result [v]))))
          {}
          seqs))

(defn dot-product
  "143: dot product"
  [vec1 vec2]
  (->> (map * vec1 vec2)
       (apply +)))

(defn binary->number
  "122: Convert a binary number, provided in the form of a string, to its numerical value.
  参考答案：(Long/praseLong % 2)"
  [binary-str]
  (->> (zipmap (range (count binary-str)) (reverse binary-str))
       (map (fn [[k v]]
              (if (= \1 v)
                (apply * (repeat k 2))
                0)))
       (apply +)))

(defn pairwise-disjoint
  "153#
  参考答案：#(apply distinct? (mapcat seq %))
  ???疑问：distinct用法(distinct? 1 2 3 3)
          (mapcat seq #{#{1 2 3} #{2 3 4}}) => '(1 2 3 2 3)
          (apply distinct? '(1 2 3 2 3)) 等价于 (distinct? 1 2 3 2 3)??
          "
  [ss]
  (let [ss-seq (seq ss)
        c1 (reduce #(+ % (count %2)) 0 ss-seq)
        c2 (count (reduce clojure.set/union #{} ss-seq))]
    (= c1 c2)))

(defn trees-into-tables
  "146#
  参考答案：#(into {} (for [[k v] % [x y] v] [[k x] y]))"
  [m]
  (->> (for [[k v] m]
         (for [[k2 v2] v]
           {[k k2] v2}))
       (apply concat)
       (apply merge-with into)))

(defn symmetry-tree?
  "96: 是否为对称树
  参考答案：#(= % ((fn m [[v l r]] (if v [v (m r) (m l)])) %))"
  [[root left right]]
  (let [symmetry? (fn symmetry? [l r]
                    (cond
                      (not= (sequential? l) (sequential? r)) false
                      (sequential? l) (let [[lroot ll lr] l
                                     [rroot rl rr] r]
                                 (and (= lroot rroot)
                                      (symmetry? ll rr)
                                      (symmetry? lr rl)))
                      :else (= l r)))]
    (symmetry? left right)))
(ns fourclojure.dzpk)

(defn convert-pais
  "将牌面数值有keyword形式转换为数值形式"
  [pais]
  (let [k-v {:2 2 :3 3 :4 4 :5 5 :6 6 :7 7 :8 8 :9 9 :t 10 :j 11 :q 12 :k 13 :a 14}]
    (mapv #(assoc % 1 (k-v (second %))) pais)))

(defn sort-by-num
  "根据牌面数值大小排序"
  [pais compare-f]
  (sort #(compare-f (last %1) (last %2)) pais))

;;===============================
;; 牌型判断

(defn royal-flush?
  "判断一手牌是否为皇家同花顺"
  [pais]
  (and (= (sort (map last pais))
          [10 11 12 13 14])
       (apply = (map first pais))))

(defn straight-flush?
  "判断一手牌是否为同花顺"
  [pais]
  (let [count-pais (count pais)
        sorted-nums (sort (map last pais))
        target-nums (range (first sorted-nums) (+ 2 count-pais))]
    (and (> count-pais 4)
         (apply = (map first pais))
         (= sorted-nums target-nums))))

(defn four-of-a-kind?
  "判断一手牌是否为四炸"
  [pais]
  (let [sorted-nums (sort (map last pais))]
    (or (apply = (rest sorted-nums))
        (apply = (butlast sorted-nums)))))

(defn full-house?
  "判断一手牌是否为葫芦"
  [pais]
  (let [full-house-nums? (fn [s-nums partition-n]
                           (let [after-partition (split-at partition-n s-nums)]
                             (and (apply = (first after-partition))
                                  (apply = (last after-partition)))))
        sorted-nums (sort (map last pais))]
    (and (= 5 (count pais))
         (or (full-house-nums? sorted-nums 2)
             (full-house-nums? sorted-nums 3)))))

(defn flush?
  "判断一手牌是否为同花"
  [pais]
  (and (> (count pais) 4)
       (apply = (map first pais))))

(defn straight?
  "判断一手牌是否为顺子"
  [pais]
  (let [count-pais (count pais)
        sorted-nums (sort (map last pais))
        target-nums (range (first sorted-nums) (+ 2 count-pais))]
    (and (> count-pais 4)
         (= sorted-nums target-nums))))

(defn three-of-a-kind?
  "判断一手牌是否为三同"
  [pais]
  (->> (map last pais)
       (group-by identity)
       (some #(= 3 (count (last %))))
       (not= nil)))

(defn two-pairs?
  "判断一手牌是否为2对"
  [pais]
  (->> (map last pais)
       (group-by identity)
       (filter #(= 2 (count (last %))))
       (#(= (count %) 2))))

(defn one-pair?
  "判断一手牌是否为对子"
  [pais]
  (->> (map last pais)
       distinct
       count
       (= (dec (count pais)))))

(defn high-card?
  "判断一手牌是否是高张"
  [pais]
  (let [nums (map last pais)]
    (and (not (straight? pais))
         (= nums (distinct nums)))))

(defn mk-paixing-filter
  "构造牌型过滤器"
  [paixing-name check-fn]
  (fn [[curr-paixing pais]]
    (if curr-paixing
      [curr-paixing pais]
      (if (check-fn pais)
        [paixing-name pais]
        [curr-paixing pais]))))

(defn paixing-filters
  "牌型过滤器，必须依照牌型从大到小的顺序组装各个过滤器。"
  [pais]
  (-> [nil pais]
      ((mk-paixing-filter :royal-flush royal-flush?))
      ((mk-paixing-filter :straight-flush straight-flush?))
      ((mk-paixing-filter :4-of-a-kind four-of-a-kind?))
      ((mk-paixing-filter :full-house full-house?))
      ((mk-paixing-filter :flush flush?))
      ((mk-paixing-filter :straight straight?))
      ((mk-paixing-filter :three-of-a-kind three-of-a-kind?))
      ((mk-paixing-filter :two-pairs two-pairs?))
      ((mk-paixing-filter :one-pair one-pair?))
      ((mk-paixing-filter :high-card high-card?))))




;;=============================
;(defn paixing
;  [pais]
;  )
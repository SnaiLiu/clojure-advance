(ns fourclojure.dzpk)

(defn convert-pais
  "牌型显示形式与程序内部表现形式转换"
  [k-v pais]
  (mapv #(assoc % 1 (k-v (second %))) pais))

(defn sort-by-num
  "根据牌面数值大小排序"
  [pais compare-f]
  (sort #(compare-f (last %1) (last %2)) pais))

;;===============================
;; 牌型判断

(defn straight-flush?
  "判断一手牌是否为同花顺"
  [pais]
  (let [count-pais (count pais)
        sorted-nums (sort (map last pais))
        target-nums (range (first sorted-nums) (+ count-pais (first sorted-nums)))]
    (and (> count-pais 4)
         (apply = (map first pais))
         (= sorted-nums target-nums))))

;; ============================
(defn guess-flush
  "获取可能组成同花类的牌型"
  [group-by-color]
  (->> (filter #(> (count (last %)) 4) group-by-color)
       first
       last
       (#(sort-by-num % >))))

(defn royal-flush-filter
  "过滤出皇家同花顺"
  [pais]
  (let [group-by-color (group-by first pais)
        guess-flush (guess-flush group-by-color)
        royal-flush? (when-not (empty? guess-flush)
                       (= (sort (map last guess-flush))
                          [10 11 12 13 14]))]
    (when royal-flush?
      [:royal-flush guess-flush])))

(defn longest-straight
  "取得最长的'顺子'"
  [guess-straight]
  (reduce (fn [r val]
            (cond
              (= 5 (count r)) r
              (= (last (last r)) (inc (last val))) (conj r val)
              (not= (last (last r)) (inc (last val))) [val]))
          [] guess-straight))

(defn straight-flush-filter
  "过滤出最大的同花顺"
  [pais]
  (let [group-by-color (group-by first pais)
        guess-straight-flush (longest-straight (guess-flush group-by-color))]
    (when (and (not-empty guess-straight-flush) (straight-flush? guess-straight-flush))
      [:straight-flush guess-straight-flush])))

(defn four-of-a-kind-filter
  "过滤出最大的四带一"
  [pais]
  (let [group-by-num (group-by last pais)
        guess-4 (->> (filter #(= (count (last %)) 4) group-by-num)
                     first)
        guess-4-1 (when guess-4
                    (->> (filter #(not= (first guess-4) (last %)) pais)
                         (#(sort-by-num % >))
                         first))]
    (when-not (empty? guess-4-1)
      [:4-of-a-kind (conj (last guess-4) guess-4-1)])))

(defn full-house-filter
  "过滤出最大的葫芦"
  [pais]
  (let [group-by-num (group-by last pais)
        guess-three (->> (filter #(= (count (last %)) 3) group-by-num)
                         (sort #(> (first %1) (first %2)))
                         first)
        guess-two (when-not (empty? guess-three)
                    (->> (dissoc group-by-num (first guess-three))
                         (filter #(>= (count (last %)) 2))
                         (sort #(> (first %1) (first %2)))
                         first
                         (take 2)))]
    (when-not (empty? guess-two)
      [:full-house (into (last guess-three) (last guess-two))])))

(defn flush-filter
  "过滤出最大的同花"
  [pais]
  (let [group-by-color (group-by first pais)
        guess-flush (guess-flush group-by-color)]
    (when-not (empty? guess-flush)
      [:flush (vec (take 5 guess-flush))])))

(defn straight-filter
  "过滤出最大的顺子"
  [pais]
  (let [group-by-num (group-by last pais)
        guess-straight (->> (mapv #(first (last %)) group-by-num)
                            (#(sort-by-num % >))
                            (longest-straight))]
    (when (>= (count guess-straight) 5)
      [:straight (vec (take 5 guess-straight))])))

(defn three-of-a-kind-filter
  "过滤出最大的三同"
  [pais]
  (let [group-by-num (group-by last pais)
        guess-three (->> (filter #(= (count (last %)) 3) group-by-num)
                         (sort #(> (first %1) (first %2)))
                         first)
        guess-two (when guess-three
                    (->> (filter #(not= (first guess-three) (last %)) pais)
                         (#(sort-by-num % >))
                         (take 2)))]
    (when-not (empty? guess-two)
      [:three-of-a-kind (into (last guess-three) guess-two)])))

(defn two-pairs-filter
  "过滤出最大的两对"
  [pais]
  (let [group-by-num (group-by last pais)
        guess-two (->> (filter #(= (count (last %)) 2) group-by-num)
                       (sort #(> (first %1) (first %2)))
                       (take 2)
                       (map last)
                       (#(when-not (empty? %)
                           (apply into [] %))))
        not-include-fn (fn [vs v]
                     (every? #(not= v %) vs))
        guess-single (when (= (count guess-two) 4)
                      (->> (filter #(not-include-fn guess-two %) pais)
                           (#(sort-by-num % >))
                           first))]
    (when-not (empty? guess-single)
      [:two-pairs (conj guess-two guess-single)])))

(defn one-pair-filter
  "过滤出最大的一对"
  [pais]
  (let [group-by-num (group-by last pais)
        guess-pair (->> (filter #(= (count (last %)) 2) group-by-num)
                        (sort #(> (first %1) (first %2)))
                        first)

        guess-single (when guess-pair
                       (->> (filter #(not= (first guess-pair) (last %)) pais)
                            (#(sort-by-num % >))
                            (take 4)))]
    (when guess-single
      [:one-pair (into (last guess-pair) guess-single)])))

(defn high-card-filter
  "过滤出最大的高张"
  [pais]
  [:high-card (->> (sort-by-num pais >)
                   (take 5)
                   vec)])

(defn mk-filter
  "构建一个filter"
  [filter-fn]
  (fn [[type pais]]
    (if type
      [type pais]
      (if-let [type-pai (filter-fn pais)]
        type-pai
        [nil pais]))))

(defn comp-filters
  "构建大筛子，从大到小牌型进行组装"
  [pais]
  (->> [nil pais]
       ((mk-filter royal-flush-filter))
       ((mk-filter straight-flush-filter))
       ((mk-filter four-of-a-kind-filter))
       ((mk-filter full-house-filter))
       ((mk-filter flush-filter))
       ((mk-filter straight-filter))
       ((mk-filter three-of-a-kind-filter))
       ((mk-filter two-pairs-filter))
       ((mk-filter one-pair-filter))
       ((mk-filter high-card-filter))))

;;=============================
(defn paixing
  "入口，找出德州扑克7中牌中最大的牌型"
  [pais]
  (let [converted-pais (convert-pais {:2 2 :3 3 :4 4 :5 5 :6 6 :7 7 :8 8 :9 9 :t 10 :j 11 :q 12 :k 13 :a 14} pais)
        [type paixing-pais] (comp-filters converted-pais)]
    [type (convert-pais {2 :2 3 :3 4 :4 5 :5 6 :6 7 :7 8 :8 9 :9 10 :t 11 :j 12 :q 13 :k 14 :a} paixing-pais)]))
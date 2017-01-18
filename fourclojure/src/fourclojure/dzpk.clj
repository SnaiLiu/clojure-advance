(ns fourclojure.dzpk)
;
;(defn convert-pais
;  "牌型显示形式与程序内部表现形式转换"
;  [k-v pais]
;  (mapv #(assoc % 1 (k-v (second %))) pais))
;
;(defn sort-by-num
;  "根据牌面数值大小排序"
;  [pais compare-f]
;  (sort #(compare-f (last %1) (last %2)) pais))
;
;;;===============================
;;; 牌型判断
;
;(defn straight-flush?
;  "判断一手牌是否为同花顺"
;  [pais]
;  (let [count-pais (count pais)
;        sorted-nums (sort (map last pais))
;        target-nums (range (first sorted-nums) (+ count-pais (first sorted-nums)))]
;    (and (> count-pais 4)
;         (apply = (map first pais))
;         (= sorted-nums target-nums))))
;
;;; ============================
;(defn guess-flush
;  "获取可能组成同花类的牌型"
;  [group-by-color]
;  (->> (filter #(> (count (last %)) 4) group-by-color)
;       first
;       last
;       (#(sort-by-num % >))))
;
;(defn royal-flush-filter
;  "过滤出皇家同花顺"
;  [pais]
;  (let [group-by-color (group-by first pais)
;        guess-flush (guess-flush group-by-color)
;        royal-flush? (when-not (empty? guess-flush)
;                       (= (sort (map last guess-flush))
;                          [10 11 12 13 14]))]
;    (when royal-flush?
;      [:royal-flush guess-flush])))
;
;(defn longest-straight
;  "取得最长的'顺子'"
;  [guess-straight]
;  (reduce (fn [r val]
;            (cond
;              (= 5 (count r)) r
;              (= (last (last r)) (inc (last val))) (conj r val)
;              (not= (last (last r)) (inc (last val))) [val]))
;          [] guess-straight))
;
;(defn straight-flush-filter
;  "过滤出最大的同花顺"
;  [pais]
;  (let [group-by-color (group-by first pais)
;        guess-straight-flush (longest-straight (guess-flush group-by-color))]
;    (when (and (not-empty guess-straight-flush) (straight-flush? guess-straight-flush))
;      [:straight-flush guess-straight-flush])))
;
;(defn four-of-a-kind-filter
;  "过滤出最大的四带一"
;  [pais]
;  (let [group-by-num (group-by last pais)
;        guess-4 (->> (filter #(= (count (last %)) 4) group-by-num)
;                     first)
;        guess-4-1 (when guess-4
;                    (->> (filter #(not= (first guess-4) (last %)) pais)
;                         (#(sort-by-num % >))
;                         first))]
;    (when-not (empty? guess-4-1)
;      [:4-of-a-kind (conj (last guess-4) guess-4-1)])))
;
;(defn full-house-filter
;  "过滤出最大的葫芦"
;  [pais]
;  (let [group-by-num (group-by last pais)
;        guess-three (->> (filter #(= (count (last %)) 3) group-by-num)
;                         (sort #(> (first %1) (first %2)))
;                         first)
;        guess-two (when-not (empty? guess-three)
;                    (->> (dissoc group-by-num (first guess-three))
;                         (filter #(>= (count (last %)) 2))
;                         (sort #(> (first %1) (first %2)))
;                         first
;                         (take 2)))]
;    (when-not (empty? guess-two)
;      [:full-house (into (last guess-three) (last guess-two))])))
;
;(defn flush-filter
;  "过滤出最大的同花"
;  [pais]
;  (let [group-by-color (group-by first pais)
;        guess-flush (guess-flush group-by-color)]
;    (when-not (empty? guess-flush)
;      [:flush (vec (take 5 guess-flush))])))
;
;(defn straight-filter
;  "过滤出最大的顺子"
;  [pais]
;  (let [group-by-num (group-by last pais)
;        guess-straight (->> (mapv #(first (last %)) group-by-num)
;                            (#(sort-by-num % >))
;                            (longest-straight))]
;    (when (>= (count guess-straight) 5)
;      [:straight (vec (take 5 guess-straight))])))
;
;(defn three-of-a-kind-filter
;  "过滤出最大的三同"
;  [pais]
;  (let [group-by-num (group-by last pais)
;        guess-three (->> (filter #(= (count (last %)) 3) group-by-num)
;                         (sort #(> (first %1) (first %2)))
;                         first)
;        guess-two (when guess-three
;                    (->> (filter #(not= (first guess-three) (last %)) pais)
;                         (#(sort-by-num % >))
;                         (take 2)))]
;    (when-not (empty? guess-two)
;      [:three-of-a-kind (into (last guess-three) guess-two)])))
;
;(defn two-pairs-filter
;  "过滤出最大的两对"
;  [pais]
;  (let [group-by-num (group-by last pais)
;        guess-two (->> (filter #(= (count (last %)) 2) group-by-num)
;                       (sort #(> (first %1) (first %2)))
;                       (take 2)
;                       (map last)
;                       (#(when-not (empty? %)
;                           (apply into [] %))))
;        not-include-fn (fn [vs v]
;                     (every? #(not= v %) vs))
;        guess-single (when (= (count guess-two) 4)
;                      (->> (filter #(not-include-fn guess-two %) pais)
;                           (#(sort-by-num % >))
;                           first))]
;    (when-not (empty? guess-single)
;      [:two-pairs (conj guess-two guess-single)])))
;
;(defn one-pair-filter
;  "过滤出最大的一对"
;  [pais]
;  (let [group-by-num (group-by last pais)
;        guess-pair (->> (filter #(= (count (last %)) 2) group-by-num)
;                        (sort #(> (first %1) (first %2)))
;                        first)
;
;        guess-single (when guess-pair
;                       (->> (filter #(not= (first guess-pair) (last %)) pais)
;                            (#(sort-by-num % >))
;                            (take 4)))]
;    (when guess-single
;      [:one-pair (into (last guess-pair) guess-single)])))
;
;(defn high-card-filter
;  "过滤出最大的高张"
;  [pais]
;  [:high-card (->> (sort-by-num pais >)
;                   (take 5)
;                   vec)])
;
;(defn mk-filter
;  "构建一个filter"
;  [filter-fn]
;  (fn [[type pais]]
;    (if type
;      [type pais]
;      (if-let [type-pai (filter-fn pais)]
;        type-pai
;        [nil pais]))))
;
;(defn comp-filters
;  "构建大筛子，从大到小牌型进行组装"
;  [pais]
;  (->> [nil pais]
;       ((mk-filter royal-flush-filter))
;       ((mk-filter straight-flush-filter))
;       ((mk-filter four-of-a-kind-filter))
;       ((mk-filter full-house-filter))
;       ((mk-filter flush-filter))
;       ((mk-filter straight-filter))
;       ((mk-filter three-of-a-kind-filter))
;       ((mk-filter two-pairs-filter))
;       ((mk-filter one-pair-filter))
;       ((mk-filter high-card-filter))))
;
;;;=============================
;(defn paixing
;  "入口，找出德州扑克7中牌中最大的牌型"
;  [pais]
;  (let [converted-pais (convert-pais {:2 2 :3 3 :4 4 :5 5 :6 6 :7 7 :8 8 :9 9 :t 10 :j 11 :q 12 :k 13 :a 14} pais)
;        [type paixing-pais] (comp-filters converted-pais)]
;    [type (convert-pais {2 :2 3 :3 4 :4 5 :5 6 :6 7 :7 8 :8 9 :9 10 :t 11 :j 12 :q 13 :k 14 :a} paixing-pais)]))
;
;
;;===============================================================

(comment
  (def cards-classify
    {:original [[:s 14] [:s 13] [:h 12] [:h 11] [:c 9] [:c 8]] ;; 原始牌 从大到小排序
     ;;按花色分类， 牌从大到小排序
     :s [[:s 5] [:s 4] [:s 3]]                              ;;黑
     :h [[:h 9] [:h 8]]                                     ;;红
     :c [[:c 3] [:c 2]]                                     ;;梅
     :d [[:d 14] [:d 13]]                                   ;;方
     ;; 按牌的张数分类， 牌从大到小排序
     :4 [[:s 4] [:h 4] [:c 4] [:d 4]]                       ;;4同
     :3 [[:s 3] [:h 3] [:c 3] [:s 2] [:s 2] [:s 2]]         ;;3同
     :2 [[:s 7] [:h 7] [:s 6] [:h 6]]                       ;;对子
     :1 [[:s 10] [:h 9] [:c 8] [:d 7] [:d 6]]               ;;单张
     }))

(comment
  (def cards-classify2
    {:original [[:s 14] [:s 13] [:h 12] [:h 11] [:c 9] [:c 8]] ;; 原始牌 从大到小排序
     ;;按花色分类， 牌从大到小排序
     :s [5 4 3 2]                              ;;黑
     :h [9 8]                                     ;;红
     :c [3 2]                                     ;;梅
     :d [14 13]                                   ;;方
     ;; 按牌的张数分类， 牌从大到小排序
     :4 [4]                       ;;4同
     :3 [3 2]         ;;3同
     :2 [7 6]                       ;;对子
     :1 [10 9 8 7 6 5 4 3]               ;;单张
     }))

(defn sort-by-num
  "根据牌面数值大小排序"
  [compare-f pais]
  (sort #(compare-f (last %1) (last %2)) pais))

(defn filter-cards-by-count
  "根据牌的张数过滤出牌"
  [grouped-by-number t-count]
  (let [result (->> (filter #(= t-count (count (last %))) grouped-by-number)
                    (mapcat #(distinct (last %))))]
    (when-not (empty? result)
      {(keyword (str t-count)) result})))

(defn cards-group
  "根据花色和张数分类"
  [original-cards type-fn]
  (->> original-cards
       (sort-by-num >)
       (group-by type-fn)
       (map (fn [[k v]] {k (map last v)}))
       (apply merge)))

(defn cards-classify
  "牌分类，根据花色和牌面数值对牌进行大体分类"
  [original-cards]
  (let [grouped-by-color (cards-group original-cards first)
        grouped-by-number (cards-group original-cards last)
        count-four (filter-cards-by-count grouped-by-number 4)
        count-three (filter-cards-by-count grouped-by-number 3)
        count-two (filter-cards-by-count grouped-by-number 2)
        count-one (filter-cards-by-count grouped-by-number 1)]
    (merge {:original (sort-by-num > original-cards)} grouped-by-color count-four count-three count-two count-one)))

(defn convert-cards
  "牌型显示形式与程序内部表现形式转换"
  [cards-dict cards]
  (mapv #(assoc % 1 (cards-dict (second %))) cards))

(defn cards-add-color
  "为牌添上花色"
  [nums color]
  (mapv #(do [color %]) nums))

(defn cards-by-num
  "根据牌面数值和所需张数过滤出牌列表"
  [origianl-pais num count]
  (->> (filter #(= num (last %)) origianl-pais)
       (take count)
       vec))

;;===============================
;; 牌型判断

(defn max-straight-nums
  "取得最大的连牌, sorted-nums为从大到小排列的牌面值
  n为要获取的连牌张数
  没有符合要求的，则返回nil"
  [sorted-nums n]
  (let [result (reduce (fn [r val]
                         (cond
                           (= (count r) n) r
                           (= (last r) (inc val)) (conj r val)
                           :default [val]))
                       [] sorted-nums)]
    (when (= n (count result))
      result)))

(defn royal-flush-filter
  "过滤出皇家同花顺"
  [classified-cards]
  (let [[color nums] (->> (map #(when-let [max-straight
                                        (max-straight-nums (get classified-cards %) 5)]
                                 (when (= [14 13 12 11 10] max-straight)
                                   [% max-straight]))
                              [:s :h :c :d])
                          (filter #(not (empty? %)))
                          first)]
    (when color
      [:royal-flush (cards-add-color nums color)])))



(defn straight-flush-filter
  "过滤出最大的同花顺"
  [classified-cards]
  (let [[color nums] (->> (map #(when-let [straight-cards (max-straight-nums (get classified-cards %) 5)]
                                  [% straight-cards])
                               [:s :h :c :d])
                          (filter #(not (empty? %)))
                          first)]
    (when color
      [:straight-flush (cards-add-color nums color)])))


(defn four-of-a-kind-filter
  "过滤出最大的四带一"
  [classified-cards]
  (let [max-four-num (first (:4 classified-cards))
        max-one-card (->> (:original classified-cards)
                     (filter #(not= max-four-num (last %)))
                     first)]
    (when max-four-num
      [:4-of-a-kind [[:s max-four-num] [:c max-four-num] [:h max-four-num] [:d max-four-num] max-one-card]])))

(defn full-house-filter
  "过滤出最大的葫芦"
  [classified-cards]
  (let [three (:3 classified-cards)
        max-three (first three)
        second-three (second three)
        first-two (first (:2 classified-cards))
        max-two (if (and second-three first-two)
                  (max second-three first-two)
                  (or second-three first-two))
        original-cards (:original classified-cards)]
    (when (and max-three max-two)
      [:full-house (into (cards-by-num original-cards max-three 3)
                         (cards-by-num original-cards max-two 2))])))

(defn flush-filter
  "过滤出最大的同花"
  [classified-cards]
  (let [[color nums] (->> (map #(when-let [flush-nums (take 5 (get classified-cards %))]
                                  (when (= 5 (count flush-nums))
                                    [% flush-nums]))
                               [:s :h :c :d])
                          (filter #(not (empty? %)))
                          first)]
    (when color
      [:flush (cards-add-color nums color)])))

(defn straight-filter
  "过滤出最大的顺子"
  [classified-cards]
  (let [distinct-nums (sort > (reduce #(into %1 (get classified-cards %2)) [] [:4 :3 :2 :1]))
        max-straight (max-straight-nums distinct-nums 5)
        original-cards (:original classified-cards)]
    (when max-straight
      [:straight (vec (mapcat #(cards-by-num original-cards % 1) max-straight))])))

(defn three-of-a-kind-filter
  "过滤出最大的三同"
  [classified-cards]
  (let [max-three (first (:3 classified-cards))
        max-two-nums (take 2 (:1 classified-cards))
        original-cards (:original classified-cards)]
    (when max-three
      [:three-of-a-kind (into (cards-by-num original-cards max-three 3)
                              (mapcat #(cards-by-num original-cards % 1) max-two-nums))])))

(defn two-pairs-filter
  "过滤出最大的两对"
  [classified-cards]
  (let [pairs (:2 classified-cards)
        max-two-pairs (take 2 pairs)
        ; 对子中（除去已被最大的两对），剩下最大对子
        next-two (nth pairs 2 nil)
        ; 单牌中，最大的单牌
        max-one (first (:1 classified-cards))
        ; 最大的牌
        max-single-num (if (and max-one next-two)
                         (max next-two max-one)
                         (or max-one next-two))
        original-cards (:original classified-cards)]
    (when (= 2 (count max-two-pairs))
      [:two-pairs (-> (mapcat #(cards-by-num original-cards % 2) max-two-pairs)
                      vec
                      (into (cards-by-num original-cards max-single-num 1)))])))

(defn one-pair-filter
  "过滤出最大的一对"
  [classified-cards]
  (let [max-pair (first (:2 classified-cards))
        max-three-singles (take 3 (:1 classified-cards))
        original-cards (:original classified-cards)]
    (when (and max-pair max-three-singles)
      [:one-pair (into (cards-by-num original-cards max-pair 2)
                       (mapcat #(cards-by-num original-cards % 1) max-three-singles))])))

(defn high-card-filter
  "过滤出最大的高张"
  [classified-cards]
  [:high-card (take 5 (:original classified-cards))])

(defn mk-filter
  "构建一个filter"
  [filter-fn]
  (fn [[type cards]]
    (if type
      [type cards]
      (if-let [type-card (filter-fn cards)]
        type-card
        [nil cards]))))

(defn comp-filters
  "构建大筛子，从大到小牌型进行组装"
  [classified-cards]
  (->> [nil classified-cards]
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
(defn type-cards
  "入口，找出德州扑克7中牌中最大的牌型"
  [cards]
  (let [converted-cards (convert-cards {:2 2 :3 3 :4 4 :5 5 :6 6 :7 7 :8 8 :9 9 :t 10 :j 11 :q 12 :k 13 :a 14} cards)
        classified-cards (cards-classify converted-cards)
        [type resp-cards] (comp-filters classified-cards)]
    [type (convert-cards {2 :2 3 :3 4 :4 5 :5 6 :6 7 :7 8 :8 9 :9 10 :t 11 :j 12 :q 13 :k 14 :a} resp-cards)]))


;===============================================================
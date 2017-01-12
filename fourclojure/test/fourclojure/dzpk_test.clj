(ns fourclojure.dzpk-test
  (:require [clojure.test :refer :all]
            [fourclojure.dzpk :refer :all]))

;;手上2张牌， 桌面5张公共牌 加起来7张，比大小的时候从7张中取5张，
;;组成最大的牌型，显示牌型和5张牌
;; 花色：0(黑) 1（红） 2（梅） 3（方）;;用数字表达牺牲可读性，容易出错，程序中数值加减导致错误
;; 牌面数值：0 1 2 3 4 5 6 7 8 9 10 11 12 => A23456789TJQK ;;

;; 花色：:s(黑) :h（红） :c（梅） :d（方）;;用数字表达牺牲可读性，容易出错，程序中数值加减导致错误
;; 牌面数值：:a :2 :3 :4 :5 :6 :7 :8 :9 :t :j :q :k => A23456789TJQK ;;

;; 花色、牌面数值 用keyword
;; 皇家同花顺，同花顺 四带一 葫芦 同花 顺子 三同 两对 对子 高张


(deftest convert-pais-test
  (are [pais result]
    (= (convert-pais pais) result)
    [[:s :2] [:s :3] [:s :4] [:s :5] [:s :6] [:d :7] [:d :8] [:d :9] [:d :t] [:d :j] [:d :q] [:d :k] [:d :a]]
    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6] [:d 7] [:d 8] [:d 9] [:d 10] [:d 11] [:d 12] [:d 13] [:d 14]]))

(deftest sort-by-num-test
  (are [pais result]
    (= (sort-by-num pais <) result)
    [[:s 2] [:s 8] [:s 7] [:d 6] [:d 4]]
    [[:s 2] [:d 4] [:d 6] [:s 7] [:s 8]])

  (are [pais result]
    (= (sort-by-num pais >) result)
    [[:s 2] [:s 8] [:s 7] [:d 6] [:d 4]]
    [[:s 8] [:s 7] [:d 6] [:d 4] [:s 2]]))

(deftest royal-flush?-test
  (are [pais result]
    (= (royal-flush? pais) result)
    [[:s 10] [:s 11] [:s 12] [:s 13] [:s 14]] true
    [[:s 10] [:s 11] [:s 12] [:s 13] [:d 14]] false
    [[:s 9] [:s 10] [:s 11] [:s 12] [:s 13] [:s 14]] false
    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6] [:s 8]] false
    [[:s 2] [:s 3] [:s 4] [:s 5]] false))

(deftest four-of-a-kind?-test
  (are [pais result]
    (= (four-of-a-kind? pais) result)
    [[:c 7] [:s 7] [:h 7] [:d 7] [:d 6]] true
    [[:c 7] [:s 7] [:h 7] [:d 7] [:d 10] [:d 11]] false
    [[:c 7] [:s 7] [:h 7] [:d 6] [:d 11]] false))

(deftest full-house?-test
  (are [pais result]
    (= (full-house? pais) result)
    [[:d 11] [:c 11] [:s 11] [:d 12] [:s 12]] true
    [[:d 11] [:c 11] [:s 11] [:d 12] [:s 13]] false
    [[:d 11] [:c 11] [:s 11] [:d 12]] false
    [[:d 11] [:c 11] [:s 11] [:d 12] [:s 12] [:s 13]] false))

(deftest straight-flush?-test
  (are [pais result]
    (= (straight-flush? pais) result)

    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6]] true
    [[:s 2] [:s 3] [:s 4] [:s 5] [:d 6]] false
    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6] [:s 7]] true
    [[:s 2] [:s 3] [:s 4] [:s 5]] false))

(deftest flush?-test
  (are [pais result]
    (= (flush? pais) result)

    [[:c 12] [:c 11] [:c 9] [:c 8] [:c 3]] true
    [[:c 12] [:c 11] [:c 9] [:c 8]] false
    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6]] true
    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6] [:s 10]] true))

(deftest straight?-test
  (are [pais result]
    (= (straight? pais) result)

    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6]] true
    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6] [:d 7]] true
    [[:s 2] [:s 3] [:s 4] [:s 5]] false
    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 7]] false))

(deftest three-of-a-kind?-test
  (are [pais result]
    (= (three-of-a-kind? pais) result)
    [[:s 2] [:d 2] [:c 2] [:h 2]] false
    [[:s 2] [:d 3] [:c 2] [:h 2] [:d 4]] true
    [[:s 2] [:d 4] [:c 5] [:h 2] [:d 4]] false
    ))

(deftest two-pairs?-test
  (are [pais result]
    (= (two-pairs? pais) result)

    [[:s 2] [:d 2] [:c 2] [:h 2] [:d 3]] false
    [[:s 2] [:d 2] [:c 4] [:h 4] [:d 3]] true
    [[:s 2] [:d 2] [:c 2] [:h 4] [:d 3]] false
    ))

(deftest pairs?-test
  (are [pais result]
    (= (one-pair? pais) result)

    [[:s 2] [:d 2] [:c 4] [:h 4] [:d 3]] false
    [[:s 2] [:d 2] [:c 5] [:h 4] [:d 3]] true))

(deftest single?-test
  (are [pais result]
    (= (high-card? pais) result)

    [[:s 2] [:d 6] [:c 5] [:h 7] [:d 3]] true
    [[:s 2] [:d 2] [:c 5] [:h 4] [:d 3]] false))

(deftest mk-filter-test
  (is (= ((mk-paixing-filter :royal-flush royal-flush?)
           [nil [[:s 10] [:s 11] [:s 12] [:s 13] [:s 14]]])
         [:royal-flush [[:s 10] [:s 11] [:s 12] [:s 13] [:s 14]]])))

(deftest paixing-filters-test
  (are [pais result]
    (= (paixing-filters pais) result)

    [[:s 10] [:s 11] [:s 12] [:s 13] [:s 14]] [:royal-flush [[:s 10] [:s 11] [:s 12] [:s 13] [:s 14]]]
    [[:c 7] [:s 7] [:h 7] [:d 7] [:d 13]] [:4-of-a-kind [[:c 7] [:s 7] [:h 7] [:d 7] [:d 13]]]
    [[:d 11] [:c 11] [:s 11] [:d 12] [:s 12]] [:full-house [[:d 11] [:c 11] [:s 11] [:d 12] [:s 12]]]
    [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6]] [:straight-flush [[:s 2] [:s 3] [:s 4] [:s 5] [:s 6]]]
    [[:c 12] [:c 11] [:c 9] [:c 8] [:c 3]] [:flush [[:c 12] [:c 11] [:c 9] [:c 8] [:c 3]]]
    [[:d 2] [:s 3] [:s 4] [:s 5] [:s 6]] [:straight [[:d 2] [:s 3] [:s 4] [:s 5] [:s 6]]]
    [[:s 2] [:d 2] [:c 4] [:h 4] [:d 3]] [:two-pairs [[:s 2] [:d 2] [:c 4] [:h 4] [:d 3]]]
    [[:s 2] [:d 3] [:c 2] [:h 2] [:d 4]] [:three-of-a-kind [[:s 2] [:d 3] [:c 2] [:h 2] [:d 4]]]
    [[:s 2] [:d 2] [:c 5] [:h 4] [:d 3]] [:one-pair [[:s 2] [:d 2] [:c 5] [:h 4] [:d 3]]]
    [[:s 2] [:d 7] [:c 5] [:h 4] [:d 3]] [:high-card [[:s 2] [:d 7] [:c 5] [:h 4] [:d 3]]]))

;(deftest 测试德州扑克最优牌型
;         (are [pais result]
;              (= (paixing pais) result)
;              [[:s :2] [:s :3] [:s :4] [:s :5] [:s :6] [:d :3] [:d :4]]
;              [:royal-flush [[:s :2] [:s :3] [:s :4] [:s :5] [:s :6]]]
;
;              [[:c :7] [:d :9] [:s :7] [:h :7] [:d :k] [:d :7] [:d :a]]
;              [:4-of-a-kind [[:c :7] [:s :7] [:h :7] [:d :7] [:d :a]]]
;
;              [[:d :q] [:d :j] [:c :j] [:s :j] [:s :q] [:d :8] [:d :t]]
;              [:full-house [[:d :j] [:c :j] [:s :j] [:d :q] [:s :q]]]
;
;              [[:c :3] [:c :9] [:d :t] [:c :j] [:c :8] [:c :q] [:s :t]]
;              [:flush  [[:c :q] [:c :j] [:c :9] [:c :8] [:c :3]]]
;
;              [[:s :7] [:c :6] [:d :8] [:h :5] [:c :4] [:s :8] [:h :8]]
;              [:straight [[:h :8] [:s :7] [:c :6] [:h :5] [:c :4]]]))

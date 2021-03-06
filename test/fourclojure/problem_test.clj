(ns fourclojure.problem-test
  (:require [clojure.test :refer :all]
            [fourclojure.problem :refer :all]))

(deftest last-e
  (testing "test last element for vec"
    (is (= (last-element1 [1 2 3 4 5]) 5))
    (is (= (last-element1 '(5 4 3)) 3))
    (is (= (last-element1 ["b" "c" "d"]) "d"))))

(deftest 题目20测试
  (is (= (penultimate1 (list 1 2 3 4 5)) 4))
  (is (= (penultimate1 ["a" "b" "c"]) "b"))
  (is (= (penultimate1 [[1 2] [3 4]]) [1 2])))

(deftest 求是3或5倍数的数之和
  (is (= (sum-multiples-3or5 1000) 233168)))

(deftest 取出数列中第N个数-test1
  (is (= (nth-element1 '(4 5 6 7) 2) 6))
  (is (= (nth-element1 [:a :b :c] 0) :a))
  (is (= (nth-element1 [1 2 3 4] 1) 2))
  (is (= (nth-element1 '([1 2] [3 4] [5 6]) 2) [5 6])))

(deftest 题目20测试-test2
  (is (= (penultimate2 (list 1 2 3 4 5)) 4))
  (is (= (penultimate2 ["a" "b" "c"]) "b"))
  (is (= (penultimate2 [[1 2] [3 4]]) [1 2])))

(deftest count-a-sequence-22
  (is (= (count-sequence '(1 2 3 3 1)) 5))
  (is (= (count-sequence "Hello World") 11))
  (is (= (count-sequence [[1 2] [3 4] [5 6]]) 3))
  (is (= (count-sequence '(13)) 1))
  (is (= (count-sequence '(:a :b :c)) 3)))

(deftest reverse-a-sequence-23
  (is (= (reverse-sequence [1 2 3 4 5]) [5 4 3 2 1]))
  (is (= (reverse-sequence (sorted-set 5 7 2 7)) '(7 5 2)))
  (is (= (reverse-sequence [[1 2][3 4][5 6]]) [[5 6][3 4][1 2]])))

(deftest sum-it-all-up-24
  (is (= (sum-up [1 2 3]) 6))
  (is (= (sum-up (list 0 -2 5 5)) 8))
  (is (= (sum-up #{4 2 1}) 7))
  (is (= (sum-up '(0 0 -1)) -1))
  (is (= (sum-up '(1 10 3)) 14)))

(deftest odd-numbers-of-a-sequence-25
  (is (= (odd-numbers #{1 2 3 4 5}) '(1 3 5)))
  (is (= (odd-numbers [4 2 1 6]) '(1)))
  (is (= (odd-numbers [2 2 4 6]) '()))
  (is (= (odd-numbers [1 1 1 3]) '(1 1 1 3))))

(deftest fibonacci-sequence-26
  (is (= (fibonacci-sequence 3) '(1 1 2)))
  (is (= (fibonacci-sequence 6) '(1 1 2 3 5 8)))
  (is (= (fibonacci-sequence 8) '(1 1 2 3 5 8 13 21))))

(deftest ﻿palindrome-Detector-27
  (is (false? (palindrome? '(1 2 3 4 5))))
  (is (true? (palindrome? "racecar")))
  (is (true? (palindrome? [:foo :bar :foo])))
  (is (true? (palindrome? '(1 1 3 3 1 1))))
  (is (false? (palindrome? '(:a :b :c)))))

(deftest ﻿maximum-value-38
  (is (= (max-val 1 8 3 4) 8))
  (is (= (max-val 30 20 40) 30))
  (is (= (max-val 45 67 11) 67)))

(deftest ﻿Get-the-Caps-29
  (is (= (caps "HeLlO, WoRlD!") "HLOWRD"))
  (is (empty? (caps "nothing")))
  (is (= (caps "$#A(*&987Zf") "AZ")))

(deftest Duplicate-a-Sequence-32
  (is (= (duplicate-sequence [1 2 3]) '(1 1 2 2 3 3)))
  (is (= (duplicate-sequence [:a :a :b :b]) '(:a :a :a :a :b :b :b :b)))
  (is (= (duplicate-sequence [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4])))
  (is (= (duplicate-sequence [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4]))))

(deftest Implement-range-34
  (is (= (my-range 1 4) '(1 2 3)))
  (is (= (my-range -2 2) '(-2 -1 0 1)))
  (is (= (my-range 5 8) '(5 6 7))))

(deftest ﻿Compress-a-Sequence-30
  (is (= (apply str (compress-sequence "Leeeeeerrroyyy")) "Leroy"))
  (is (= (compress-sequence [1 1 2 3 3 2 2 3]) '(1 2 3 2 3)))
  (is (= (compress-sequence [[1 2] [1 2] [3 4] [1 2]]) '([1 2] [3 4] [1 2]))))


(deftest Factorial-Fun
  (is (= (cal-factorials 1) 1))
  (is (= (cal-factorials 3) 6))
  (is (= (cal-factorials 5) 120))
  (is (= (cal-factorials 8) 40320)))

(deftest Interleave-Two-Seqs
  (is (= (interleave-seqs [1 2 3] [:a :b :c]) '(1 :a 2 :b 3 :c)))
  (is (= (interleave-seqs [1 2] [3 4 5 6]) '(1 3 2 4)))
  (is (= (interleave-seqs [1 2 3 4] [5]) [1 5]))
  (is (= (interleave-seqs [30 20] [25 15]) [30 25 20 15])))

;(deftest Flatten-a-Sequence
;  (is (= (__ '((1 2) 3 [4 [5 6]])) '(1 2 3 4 5 6)))
;  (is (= (__ ["a" ["b"] "c"]) '("a" "b" "c")))
;  (is (= (__ '((((:a))))) '(:a))))

(deftest Replicate-a-Sequence
  (is (= (replicate-sequence [1 2 3] 2) '(1 1 2 2 3 3)))
  (is (= (replicate-sequence [:a :b] 4) '(:a :a :a :a :b :b :b :b)))
  (is (= (replicate-sequence [4 5 6] 1) '(4 5 6)))
  (is (= (replicate-sequence [[1 2] [3 4]] 2) '([1 2] [1 2] [3 4] [3 4])))
  (is (= (replicate-sequence [44 33] 2) [44 44 33 33])))

(deftest Interpose-a-Seq
  (is (= (interpose-sequence 0 [1 2 3]) [1 0 2 0 3]))
  (is (= (apply str (interpose-sequence ", " ["one" "two" "three"])) "one, two, three"))
  (is (= (interpose-sequence :z [:a :b :c :d]) [:a :z :b :z :c :z :d])))

(deftest prime-numbers-test
  (is (= (prime-numbers 2) '(2)))
  (is (= (prime-numbers 10) '(2 3 5 7)))
  (is (= (prime-numbers 20) '(2 3 5 7 11 13 17 19))))

(deftest primes-test
  (is (= (take 3 (primes)) '(2 3 5)))
  (is (= (take 5 (primes)) '(2 3 5 7 11)))
  (is (= (take 1 (primes)) '(2))))

(deftest Pack-a-Sequence
  (is (= (pack-sequence [1 1 2 1 1 1 3 3]) '((1 1) (2) (1 1 1) (3 3))))
  (is (= (pack-sequence [:a :a :b :b :c]) '((:a :a) (:b :b) (:c))))
  (is (= (pack-sequence [[1 2] [1 2] [3 4]]) '(([1 2] [1 2]) ([3 4])))))

(deftest Drop-Every-Nth-Item
  (is (= (drop-nth-item [1 2 3 4 5 6 7 8 9 10] 3) [1 2 4 5 7 8 10]))
  (is (= (drop-nth-item [:a :b :c :d :e :f] 2) [:a :c :e]))
  (is (= (drop-nth-item [1 2 3 4 5 6] 4) [1 2 3 5 6])))

(deftest Split-a-sequence
  (is (= (split-sequence 3 [1 2 3 4 5 6]) [[1 2 3] [4 5 6]]))
  (is (= (split-sequence 1 [:a :b :c :d]) [[:a] [:b :c :d]]))
  (is (= (split-sequence 2 [[1 2] [3 4] [5 6]]) [[[1 2] [3 4]] [[5 6]]])))

(deftest Map-Construction
  (is (= (map-construction [:a :b :c] [1 2 3]) {:a 1, :b 2, :c 3}))
  (is (= (map-construction [1 2 3 4] ["one" "two" "three"]) {1 "one", 2 "two", 3 "three"}))
  (is (= (map-construction [:foo :bar] ["foo" "bar" "baz"]) {:foo "foo", :bar "bar"})))

(deftest Greatest-Common-Divisor
  (is (= (greatest-common-divisor 2 4) 2))
  (is (= (greatest-common-divisor 1023 858) 33)))

(deftest Re-implement-Iterate
  (is (= (take 5 (my-iterate #(* 2 %) 1)) [1 2 4 8 16]))
  (is (= (take 100 (my-iterate inc 0)) (take 100 (range))))
  (is (= (take 9 (my-iterate #(inc (mod % 3)) 1)) (take 9 (cycle [1 2 3])))))

(deftest clurese
  (is (= 256 ((simple-clourse 2) 16), ((simple-clourse 8) 2)))
  (is (= [1 8 27 64] (map (simple-clourse 3) [1 2 3 4])))
  (is (= [1 2 4 8 16] (map #((simple-clourse %) 2) [0 1 2 3 4]))))

(deftest Cartesian-Product
  (is (= (cartesian-product #{"ace" "king" "queen"} #{"♠" "♥" "♦" "♣"})
          #{["ace"   "♠"] ["ace"   "♥"] ["ace"   "♦"] ["ace"   "♣"]
            ["king"  "♠"] ["king"  "♥"] ["king"  "♦"] ["king"  "♣"]
            ["queen" "♠"] ["queen" "♥"] ["queen" "♦"] ["queen" "♣"]}))
  (is (= (cartesian-product #{1 2 3} #{4 5})
          #{[1 4] [2 4] [3 4] [1 5] [2 5] [3 5]}))
  (is (= 300 (count (cartesian-product (into #{} (range 10))
                         (into #{} (range 30)))))))

(deftest Recognize-Playing-Cards
  (is (= {:suit :diamond :rank 10} (recognize-cards "DQ")))
  (is (= {:suit :heart :rank 3} (recognize-cards "H5")))
  (is (= {:suit :club :rank 12} (recognize-cards "CA")))
  (is (= (range 13) (map (comp :rank recognize-cards str)
                          '[S2 S3 S4 S5 S6 S7
                            S8 S9 ST SJ SQ SK SA]))))

(deftest Product-Digits
  (is (= (product-digits 1 1) [1]))
  (is (= (product-digits 99 9) [8 9 1]))
  (is (= (product-digits 999 99) [9 8 9 0 1])))

(deftest Group-a-Sequence
  (is (= (group-seq #(> % 5) [1 3 6 8]) {false [1 3], true [6 8]}))
  (is (= (group-seq #(apply / %) [[1 2] [2 4] [4 6] [3 6]])
         {1/2 [[1 2] [2 4] [3 6]], 2/3 [[4 6]]}))
  (is (= (group-seq count [[1] [1 2] [3] [1 2 3] [2 3]])
         {1 [[1] [3]], 2 [[1 2] [2 3]], 3 [[1 2 3]]})))

(deftest Dot-Product
  (is (= 0 (dot-product [0 1 0] [1 0 0])))
  (is (= 3 (dot-product [1 1 1] [1 1 1])))
  (is (= 32 (dot-product [1 2 3] [4 5 6])))
  (is (= 256 (dot-product [2 5 6] [100 10 1]))))

(deftest Read-a-binary-number
  (is (= 0 (binary->number "0")))
  (is (= 7 (binary->number "111")))
  (is (= 8 (binary->number "1000")))
  (is (= 9 (binary->number "1001")))
  (is (= 255 (binary->number "11111111")))
  (is (= 1365 (binary->number "10101010101")))
  (is (= 65535 (binary->number "1111111111111111"))))

(deftest Pairwise-Disjoint-Sets
  (is (= (pairwise-disjoint #{#{\U} #{\s} #{\e \R \E} #{\P \L} #{\.}})
         true))
  (is (= (pairwise-disjoint #{#{:a :b :c :d :e}
                #{:a :b :c :d}
                #{:a :b :c}
                #{:a :b}
                #{:a}})
          false))
  (is (= (pairwise-disjoint #{#{[1 2 3] [4 5]}
                #{[1 2] [3 4 5]}
                #{[1] [2] 3 4 5}
                #{1 2 [3 4] [5]}})
          true))
  (is (= (pairwise-disjoint #{#{'a 'b}
                #{'c 'd 'e}
                #{'f 'g 'h 'i}
                #{''a ''c ''f}})
          true))
  (is (= (pairwise-disjoint #{#{'(:x :y :z) '(:x :y) '(:z) '()}
                #{#{:x :y :z} #{:x :y} #{:z} #{}}
                #{'[:x :y :z] [:x :y] [:z] [] {}}})
          false))
  (is (= (pairwise-disjoint #{#{(= "true") false}
                #{:yes :no}
                #{(class 1) 0}
                #{(symbol "true") 'false}
                #{(keyword "yes") ::no}
                #{(class '1) (int \0)}})
          false))
  (is (= (pairwise-disjoint #{#{distinct?}
                #{#(-> %) #(-> %)}
                #{#(-> %) #(-> %) #(-> %)}
                #{#(-> %) #(-> %) #(-> %)}})
          true))
  (is (= (pairwise-disjoint #{#{distinct?}
                #{#(-> %) #(-> %)}
                #{#(-> %) #(-> %) #(-> %)}
                #{#(-> %) #(-> %) #(-> %)}})
          true))
  (is (= (pairwise-disjoint #{#{(#(-> *)) + (quote mapcat) #_ nil}
                #{'+ '* mapcat (comment mapcat)}
                #{(do) set contains? nil?}
                #{, , , #_, , empty?}})
          false)))

(deftest Trees-into-tables
  (is (= (trees-into-tables '{a {p 1, q 2}
                b               {m 3, n 4}})
          '{[a p] 1, [a q] 2
            [b m] 3, [b n] 4}))
  (is (= (trees-into-tables '{[1] {a b c d}
                [2]               {q r s t u v w x}})
          '{[[1] a] b, [[1] c] d,
            [[2] q] r, [[2] s] t,
            [[2] u] v, [[2] w] x}))
  (is (= (trees-into-tables '{m {1 [a b c] 3 nil}})
          '{[m 1] [a b c], [m 3] nil})))

(deftest Beauty-Is-Symmetry
  (is (= (symmetry-tree? '(:a (:b nil nil) (:b nil nil))) true))
  (is (= (symmetry-tree? '(:a (:b nil nil) (:b nil nil))) true))
  (is (= (symmetry-tree? '(:a (:b nil nil) (:c nil nil))) false))
  (is (= (symmetry-tree? [1 [2 nil [3 [4 [5 nil nil] [6 nil nil]] nil]]
                [2 [3 nil [4 [6 nil nil] [5 nil nil]]] nil]])
          true))
  (is (= (symmetry-tree? [1 [2 nil [3 [4 [5 nil nil] [6 nil nil]] nil]]
               [2 [3 nil [4 [5 nil nil] [6 nil nil]]] nil]])
          false))
  (is (= (symmetry-tree? [1 [2 nil [3 [4 [5 nil nil] [6 nil nil]] nil]]
               [2 [3 nil [4 [6 nil nil] nil]] nil]])
          false)))

(deftest Pascal's-Triangle
  (is (= (pascal-triangle 1) [1]))
  (is (= (map pascal-triangle (range 1 6))
          [     [1]
               [1 1]
              [1 2 1]
             [1 3 3 1]
            [1 4 6 4 1]]))
  (is (= (pascal-triangle 11)
          [1 10 45 120 210 252 210 120 45 10 1])))

(deftest Pascal's-Triangle2
  (is (= (pascal-triangle2 1) [1]))
  (is (= (map pascal-triangle2 (range 1 6))
         [     [1]
          [1 1]
          [1 2 1]
          [1 3 3 1]
          [1 4 6 4 1]]))
  (is (= (pascal-triangle2 11)
         [1 10 45 120 210 252 210 120 45 10 1])))

(deftest Sum-of-square-of-digits
  (is (= 8 (sum-square-digits (range 10))))
  (is (= 19 (sum-square-digits (range 30))))
  (is (= 50 (sum-square-digits (range 100))))
  (is (= 50 (sum-square-digits (range 1000)))))

(deftest Re-implement-Map
  (is (= [3 4 5 6 7]
         (my-map inc [2 3 4 5 6])))
  (is (= (repeat 10 nil)
         (my-map (fn [_] nil) (range 10))))
  (is (= [1000000 1000001]
          (->> (my-map inc (range))
               (drop (dec 1000000))
               (take 2)))))


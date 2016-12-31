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
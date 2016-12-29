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
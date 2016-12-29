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
  (is (= (nth-element1[1 2 3 4] 1) 2))
  (is (= (nth-element1 '([1 2] [3 4] [5 6]) 2) [5 6])))
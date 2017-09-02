(ns clojure-study.core
  (:require [clojure.core.async :as async :refer :all]))

;;---------- 简单用法 -----------
(def a-chan "新建一个buff大小为10的通道（channel）" (chan 10))

(defn event-put!
  "将事件写入通道"
  [event]
  ()
  )

;; close!函数，关闭一个channel，使之不在接受输入（此时向channel中写入数据将返回false），
;; 但是channel中已经存在的数据还被保留着(也就是关闭了写端)。
;; 保留着的数据被取走之后，从channel中再取数据则返回nil。
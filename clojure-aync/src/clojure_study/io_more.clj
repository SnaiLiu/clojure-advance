(ns clojure-study.io-more
  "clojure更进一步处理文件"
  (:require [clj-mmap :as mmap]
            [clojure.java.io :as cio]))

;;----- 大文件处理——clj-mmap -----
;; 使用clj-mmap库内存映射库进行大文件（可超过2G）处理：对用户来说，访问文件内容就像是访问内存中的内容一样， 但其实并不是真正将全部的文件内容都拷贝到内存中。
;; 该库封装了java NIO(New I/O)库中一些常用接口，使用起来更加简单。
;; 该库通过操作操作系统的虚拟内存，将文件内容缓存到应用程序的内存中，减少硬盘与内存间数据拷贝，提升了I/O性能。

;; 打开一个文件并缓存到内存中，
;;clj-mmap/get-mmmap [^String file-name map-mode]
;; 其中map-mode可以是：:read-only只读，:read-write读写，默认为:read-only


;; 使用mmap写文件
(defn init-file
  "初始化一个文件"
  []
  (with-open [file (cio/writer "files/large-file.txt" :encoding "UTF-8")]
    (let [bytes-to-write1 (.getBytes "第一行\n" "UTF-8")
          bytes-to-write2 (.getBytes "第二行\n" "UTF-8")]
      (.write file bytes-to-write1)
      (.write file bytes-to-write2))))

(defn write-to
  "写入文件"
  []
  (with-open [file (mmap/get-mmap "files/large-file.txt" :read-write)]
    (let [
          ;; getBytes返回byte的数组
          bytes-to-write (.getBytes "新的一行" "UTF-8")
          file-size (.size file)]
      ;; alength返回数组长度
      #_(if (> file-size (alength bytes-to-write)))
      (mmap/put-bytes file bytes-to-write 0))

    ))

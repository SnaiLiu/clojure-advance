(ns clojure-study.io-more
  "clojure更进一步处理文件"
  (:require [clj-mmap :as mmap]
            [clojure-study.io :as csio]
            [clojure.java.io :as cio])
  (:import (java.nio MappedByteBuffer)))

;;----- 大文件处理——clj-mmap -----
;; 使用clj-mmap库内存映射库进行大文件（可超过2G）处理：对用户来说，访问文件内容就像是访问内存中的内容一样， 但其实并不是真正将全部的文件内容都拷贝到内存中。
;; 该库封装了java NIO(New I/O)库中一些常用接口，使用起来更加简单。
;; 该库通过操作操作系统的虚拟内存，将文件内容缓存到应用程序的内存中，减少硬盘与内存间数据拷贝，提升了I/O性能。


;; clj-mmap/get-mmmap [^String file-name map-mode]
;; 打开一个文件并映射到内存中，其中map-mode可以是：:read-only只读，:read-write读写，默认为:read-only

;; clj-mmap/get-bytes [mmap pos n]
;; 从文件中的pos位置开始读取出n字节数据，返回字节数组byte-array

;; clj-mmap/put-bytes [mmap buf pos n]
;; 从字节缓冲区buf中取n长度到mmap中，会覆盖里面的内容，但是内容不会直接写入到文件，而是写到内存中。

;; 使用mmap读文件
(defn read-large-file
  [file-path pos n]
  (with-open [file (mmap/get-mmap file-path)]
    (let [file-size     (.size file)
          _ (prn "file-size = " file-size)
          n-bytes (mmap/get-bytes file pos n)]
      (String. n-bytes))))

(comment
  (read-large-file "files/binary-file.txt" 4 (alength (.getBytes "名字"))))

;; 使用mmap写内容到文件
(defn write-large-file
  "写入文件"
  [file-path]
  (with-open [file (mmap/get-mmap file-path :read-write)]
    (let [
          ;; getBytes返回byte的数组
          bytes-to-write (.getBytes "行" "UTF-8")
          file-size (.size file)]
      ;; alength返回数组长度
      (when (> file-size (alength bytes-to-write))
        (prn "file0=== " (alength bytes-to-write))
        (mmap/put-bytes file bytes-to-write 0)
        ))))

(comment
  (write-large-file "files/binary-file.txt")
  (read-large-file "files/binary-file.txt" 0 5)
  )

;;

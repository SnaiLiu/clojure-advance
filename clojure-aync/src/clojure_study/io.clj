(ns clojure-study.io
  "clojure文件处理"
  (:require [clojure.java.io :as cio]
            [clojure.edn :as edn])
  (:import (java.nio ByteBuffer)))


;;---- 读文本文件 -----
;; slurp：读取一个文件的全部内容，并以字符串形式返回。
;; slurp函数会先打开一个文件，读出全部内容，然后再关闭文件。
(comment
  (slurp "files/file-test.txt" :encoding "UTF-8")
  ;; => "a txt file line1\na txt file line2\n这是中文字符"
  )

;;---- 逐行读取文件内容 ----------
;; line-seq将逐行读取文件内容，并返回一个字符串的lazy序列，只有真正使用到的时候，才执行读取操作
(comment
  (defn read-by-line
    "line-fn：处理一行数据的函数"
    [file-path line-fn]
    (with-open [^java.io.BufferedReader rdr (cio/reader file-path :encoding "UTF-8")]
      (doseq [a (line-seq rdr)]
        (line-fn a))))
  (read-by-line "files/file-test.txt" (partial println "a line ===== "))
  ;; => ["a txt file line1" "a txt file line2" "这是中文字符"]
  )

;;---- 写文本文件 ----
;; clojure.core/spit [file-path content & options]
;; file-path：为文件路径；
;; content：为要写入文件的内容，所有的内容都会被转为String类型写入。
;; options：一些设置参数，常见的有：
;;    - :append true/false；当为true是，内容会从文件后面写入，如果要换行写入，需要在内容后面加入\n换行符，当为false时，会擦掉文件原有的内容。
;;    - :encoding "UTF-8"/"GBK"等。 默认是UTF-8.
;; spit函数会先打开文件，再将内容写入，再关闭文件。
;; spit都是以字符串形式写入。

(comment
  (spit "files/file-test.txt" "新增文件内容\n" :append false :encoding "GBK")
  (slurp "files/file-test.txt" :encoding "GBK")
  ;; => "新增文件内容\n"
  (slurp "files/file-test.txt" :encoding "UTF-8")
  ;; => "�����ļ�����\n"
  (spit "files/file-test.txt" "新增文件内容\n" :append false)
  (slurp "files/file-test.txt" :encoding "GBK")
  ;; => "鏂板鏂囦欢鍐呭\n"
  (slurp "files/file-test.txt" :encoding "UTF-8")
  ;; => "新增文件内容\n"

  ;; 逐行写大量数据到文件时，用spit会降低效率，因为它每写一次都会执行打开、关闭文件。
  ;; 此时可以用java.io.writer.
  (with-open [w (cio/writer "files/file-test.txt")]
    (doseq [l  ["第一行" "第二行" "第三行"]]
      (.write w l)
      ;; 新起一行
      (.newLine w)))
  )

;;---- 复制文件 ------
;; clojure.java.io/copy [input output]
;; 其中，input可以是文件，字符串、字节数组、或者是一个Reader、InputStream、字符数组等。
;;      output可以使文件，Writer、OutputStream
;; 如果目标文件已经存在，则会覆盖，需小心。
;; 默认每次复制1024字节数据，直到整个文件复制完毕。可通过:buffer-size来指定每次复制数据的大小。
(comment
  ;; input为文件，output为文件
  (cio/copy
    (cio/file "files/file-test.txt")
    (cio/file "files/file-test-bk.txt" :encoding "UTF-8"))
  ;; input为字符串，output为
  (cio/copy "a string\nhhh" *out*)
  ;; input为Rearder，output为Writer
  (with-open [reader (cio/reader "files/file-test.txt")
              writer (cio/writer "files/file-test-writer.txt")]
    (cio/copy reader writer :buffer-size 2048))
  )

;;--- 写二进制文件 ----
;; clojure.java.io/output-stream [x & opts]
;; 建立一个输出流。
;; x可以为String、File、URI、URL、Socket等。当它为String时，会先尝试自动转为URI，不行则转为File，所以最好调用时就指定。

;; clojure.core/byte-array
;; 创建一个字节数组，作为缓冲区，如果不提供初始数据，则数组内所有元素都是0
;; 它是可变的？？？？？？？

(defn prepare-bytes
  "准备好数据，假设我们要将人的姓名写入到二进制文件中
  其中姓名为字符串，当然我们还需要存一个标定一条记录长度的数据。
  |记录长度|姓名|...
  借用ByteBuffer类来组织我们要写的数据，最后返回一个字节数组"
  [name]
  (let [name-bytes (.getBytes name)
        ;; 因为name可能为中文，所以不能以(count name)来计算其所占字节数
        ;; buf的长度（字节），记录长度为int（4字节）
        buflen (+ 4 (count name-bytes))
        ;; 新建一个大小为buflen的ByteBuffer类
        bb (ByteBuffer/allocate buflen)
        ;; 最终要返回的字节数组
        buf (byte-array buflen)]
    (doto bb
      (.putInt (.intValue buflen))
      (.put name-bytes)
      (.flip)
      ;; 将ByteBuffer中的数据读入到buf中
      (.get buf))
    ;; 返回Buffer，字节数组。因为最终write到文件中的是字节数组，而不是ByteBuffer类
    ;; 所以要将ByteBuffer中的数据写到buff中
    buf))

(defn write-binary-data
  [file-path]
  (with-open [out (cio/output-stream (cio/file file-path))]
    (.write out (prepare-bytes "名字"))))

(comment
  (write-binary-data "files/binary-file.txt"))

(defn write-binary-data-simple
  [file-path]
  (with-open [out (cio/output-stream (cio/file file-path))]
    (let [bytes-to-write  (.getBytes "第一行\n")
          bytes-to-write2 (.getBytes "第二行\n")]
      (.write out bytes-to-write)
      (.write out bytes-to-write2))))

(comment
  (write-binary-data-simple "files/binary-file-simple.txt"))

;;--- 读二进制文件 ----
;;
;; clojure.java.io/input-stream [x & opts]
;; x可以为String、File、URI、URL、Socket等。当它为String时，会先尝试自动转为URI，不行则转为File，所以最好调用时就指定。
;;
;; 从文件中读出二进制数据后，要根据存储时的格式"翻译"成原来的格式
(defn read-binary-data-simple
  "读二进制文件数据"
  [file-path]
  (with-open [in (cio/input-stream (cio/file file-path))]
    (let [buf (byte-array 1024)
          ;; 使用InputStream的read函数，从输入流中读出数据到缓存
          n (.read in buf)]
      ;; 因为之前存的是字符串，所以这里直接翻译成字符串
      (new String (byte-array (take n buf))))))

(comment
  (read-binary-data-simple "files/binary-file-simple.txt")
  ;; => "第一行\n第二行\n"
  )

(defn unpack-buf
  "将数据从缓存中解析出来，对应prepare-bytes
  n：buf中有效数据长度
  but：buf缓冲区
  也是利用ByteBuffer类来解"
  [n buf]
  (let [bb (ByteBuffer/allocate n)]
    ;; 将buf中0到n的字节填充到ByteBuffer中
    (.put bb buf 0 n)
    ;; ;;准备读：将读的起始位置置为0，limit置为当前position，即10
    (.flip bb)
    (let [_ (.getInt bb)
          ;; 读完之后，position为4，相当于读指针后移到4

          ;; 新建存储姓名的缓冲区
          buf-for-name  (byte-array (- n 4))]
      ;; 读取name到缓冲区
      (.get bb buf-for-name)
      ;; name转换为String
      (String. buf-for-name))))

(defn read-binary-data
  "读一个比较复杂的数据，对应write-binary-data"
  [file-path]
  (with-open [in (cio/input-stream file-path)]
    (let [buf (byte-array 1024)
          n (.read in buf)]
      (unpack-buf n buf))))

(comment
  (read-binary-data "files/binary-file.txt")
  ;; => "名字"
  )


;;---- 访问资源文件 ----
;; clojure.java.io/resource 文件名
;; 返回一个文件所在目录路径
;; 一般我们会告知工程构建工具资源文件存放目录，比如在Leiningen构建工具中，你可以在project.clj文件中配置
;; 资源文件所在目录
;;
;; :resource-paths ["my-resources" "src/other-resources"]
;;
;; 在打包的时候，可以选择将资源文件一起打包进去，或者不一起打包进去而是在启动服务时加载进去。
;; 工具会将指定的目录下的资源文件放到jar文件的根目录下。
(comment
  (->> (cio/resource "file-edn.edn")
       (#(do (prn "url === " %) %))
       slurp
       edn/read-string))

;;---- 列出目录中的文件列表
;; clojure.core/file-seq [^File dir]
;; 将列出该目录及其子目录下所有文件名称及目录名称，包括参数目录本身。
;; 返回值一个'(^File ...)
(comment
  (file-seq (cio/file "src"))
  ;; => 将列出src目录下所有子目录及文件，包括参数目录本身
  )

;;---- 删除文件或目录 -----
;; clojure.java.io/delete-file [f & [silently]]
;; 当silently设为true时，即使删除文件失败，也不会抛出异常，当设为false时，则会抛出异常。
;; 用该函数删除目录时，需要确保目录为空，否则删除失败。
(comment
  (cio/delete-file "files/file-test-writer1.txt")
  ;; => CompilerException java.io.IOException: Couldn't delete files/file-test-writer1.txt
  (cio/delete-file "files/file-test-writer1.txt" true)
  ;; => true 此时你也不知道到底有没有删除成功。
  (cio/delete-file "files/file-test-writer.txt")
  ;; => true 真的删除了

  ;; 你可以像下面这样安全地删除文件
  (defn safe-delete [file-path]
    (when (.exists (clojure.java.io/file file-path))
      (try
        (clojure.java.io/delete-file file-path)
        (catch Exception e (str "exception: " (.getMessage e))))))

  ;; 创建临时目录
  (cio/make-parents "files/temp/file.txt")
  (cio/delete-file "files/temp/")
  ;; => true
  ;; 创建临时目录
  (cio/make-parents "files/temp/file.txt")
  (with-open [writer (cio/writer "files/temp/file.txt")]
    (cio/copy "hhh" writer))
  (cio/delete-file "files/temp/")
  ;; => CompilerException java.io.IOException: Couldn't delete src/temp/, ...

  ;; 所以在删除目录之前，需要先删除里面的文件
  (cio/make-parents "files/temp/temp2/file.txt")
  (with-open [writer (cio/writer "files/temp/temp2/file.txt")]
    (cio/copy "hhh" writer))

  (defn delete-directory
    "删除文件夹"
    [dir-path]
    (let [dir-contents (file-seq (cio/file dir-path))
          files-to-del (filter #(.isFile %) dir-contents)
          ;; 必须先从最底层的目录开始删除
          dirs-to-del  (->> (filter #(.isDirectory %) dir-contents)
                            (map #(.getPath %))
                            (sort (fn [x y]
                                    (> (count x)
                                       (count y)))))]
      (prn dirs-to-del)
      ;; 先删除所有文件
      (doseq [file files-to-del]
        (safe-delete (.getPath file)))
      ;; 再删除所有目录
      (doseq [dir-path dirs-to-del]
        (safe-delete dir-path))))

  (delete-directory "files/temp")
  )

;; =========== 小结 ============
;; 以上是clojure操作文件的常见用法
;; 能够满足基本的文件处理。
;; 对于简单的文本文件处理，可以用spit和slurp。
;; 但对于非文本文件，大数据量的，还是需要更复杂的处理
;; =============================


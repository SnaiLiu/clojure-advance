(ns adventofcode.utils
  "Tool functions for adventofcode.")

(defn find-some
  "find the first item in the coll, which match the pred."
  [pred coll]
  (when (seq coll)
    (if (pred (first coll))
      (first coll)
      (recur pred (next coll)))))

(defn handle-from-file
  "工具函数"
  [file-path handle-fn]
  (with-open [^java.io.BufferedReader rdr (clojure.java.io/reader file-path :encoding "UTF-8")]
    (handle-fn (line-seq rdr))))

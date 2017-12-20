(ns adventofcode.utils
  "Tool functions for adventofcode.")

(defn find-some
  "find the first item in the coll, which match the pred."
  [pred coll]
  (when (seq coll)
    (if (pred (first coll))
      (first coll)
      (recur pred (next coll)))))

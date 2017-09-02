(ns clojure-study.micro
  (:require [clojure.walk :as walk]
            [clojure.string :as str]))

;; clojure宏在编译期间被求值，而不是文本替换（和C语言的预编译不同），宏的求值过程也叫做"宏展开"；
;; 宏求值的结果是返回一个clojure的数据结构，这个数据结构会代替宏原来的位置。
;; clojure的源代码会被clojure reader读入，将其以文本形式求值出一个clojure数据结构，如：
;;   (fn [a] :a 123)求值出来的数据结构是一个列表：包含一个符号，一个包含符号的vector，一个关键字和一个数字；而这个数据结构本身就是clojure语言的基本数据结构。
;; 同样，宏(reverse-it (nf [a] :a 123))也会返回一个同样的数据结构。
;; 一门语言的代码可以用语言自身的数据结构来描述，称为"同向性"

;; 一个翻转符号（symbol）的宏（必须将clojure的符号反着写，如prn必须写成nrp）
(defmacro reverse-it
  [form]
  (walk/postwalk #(if (symbol? %)
                    (symbol (str/reverse (name %)))
                    %)
                 form))

(comment
  (reverse-it (nrp "lz"))
  ;; => "lz" clojure编译期间会将其求值为：(prn "lz")
  (reverse-it (prn "lz"))
  ;; => CompilerException java.lang.RuntimeException: Unable to resolve symbol: nrp in this context
  )

;; ## 宏的调试
;; ### 小心使用宏
;; 宏是在编译期执行的，而在编译期间，宏并不知道某个符号是不是已经被定义。它看到的就是列表、符号以及其他数据结构，
;; 它返回的也是列表、符号和其他数据结构。

(comment
  ;; 可以使用macroexpand-1 查看宏产生的代码，扩展宏一次，如果宏里面调用其他宏，则其他宏不会被扩展
  (macroexpand-1 '(reverse-it (nrp "lz")))
  ;; => (prn "lz")

  ;; macroexpand：如果扩展完一次宏之后，返回的还是一个宏调用，则会再次扩展，直到顶级形式不再是个宏。注意这不是嵌套的宏！
  (macroexpand '(reverse-it (nrp "lz")))

  ;; clojure.walk/macroexpand-all：彻底扩展一个宏，包括所有的嵌套宏。但它对一些特殊情况处理不完全正确，不赘述，一般用不到。
  (walk/macroexpand-all '(reverse-it (nrp "lz"))))

;; ## 宏安全
;; 宏的风险是：宏产生的代码与外部代码发生不正常的交互！
;; 宏无法访问运行时的值，不能作为值进行组合或者传递。
;; 有点弄不明白，反正对于那些对于需要传递高阶函数的地方，避免用宏。
;; 太复杂了，看不下去了。。。休息一会儿。
(comment
  (defmacro unknow-symbol
    [form]
    `(str "unknow symbol " a ~form))
  (unknow-symbol "bbb")
  ;; => CompilerException java.lang.RuntimeException: No such var: clojure-study.micro/a,
  (def a "a")
  (unknow-symbol "bbb")
  ;; => "unknow symbol abbb"

  (map reverse-it '((+ 1 3) (+ 3 4)))
  ;; => CompilerException java.lang.RuntimeException: Can't take value of a macro: #'clojure-async.micro-symbols/reverse-it,
  ;; 编译时出错，宏不能作为一个值传递给map，
  ;; 虽然map的第一个参数是一个函数fn，但是clojure中函数也是数据，也是一个值。
  (map #(reverse-it %) [(+ 1 3) (+ 3 4)])
  ;; => NullPointerException
  ;; 运行时错误

  ;; 在语法引述形式中任何以#结尾的符号都会被自动扩展，并且对于前缀相同的符号，会被扩展为同一个符号的名字
  ;; 这样做是为了避免宏里面的符号与外部代码的相冲突。当在宏里面要绑定一个本地绑定时，就可以这样做。
  `(x# x#)
  ;; => (x__2284__auto__ x__2284__auto__)
  (defmacro println-mcro
    [y]
    (let [y# "macro"]
      `(println ~y# ~y)))
  (println-mcro 1)
  ;; => macro 1

  )


;;============== 小结 =============
;; 应该尽量少用宏，只有在函数满足不了的情况下，才用宏。
;; 即使要用宏，应该只是用它做一些简单的组织工作，真正的逻辑都要放在真正的函数中。
;; 宏的使用场景：
;;   - 需要特殊的求职语义；
;;   - 需要自定义语法——特别是一些领域特定表示法。
;;   - 需要在编译器提前计算一些中间值。
;; 用之前，始终问问自己，用函数不能解决吗？！
;;=================================
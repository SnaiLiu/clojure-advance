(ns clojure-study.symbols)

;;-------- ' ----------
;; '：普通引述。
;; ' 等同于 quote，是它的简写版；返回参数的不求值的形式。
;; 普通引述不允许反引述
(comment
  (def a 1)
  (def b 2)
  '(+ a b)
  ;;=> 返回一个列表数据结构：(+ a b)， a、b不会被求值
  (+ a b)
  ;; => 3 ，a、b被求值，整个表达式被求值
  )

;;-------- ` ---------
;; `：语法引述
;; 语法引述将无命名空间的符号求值为其所在命名空间的符号(sysmbol)。
;; 如果符号本身有命名空间限定，则语法引述也会将其求值为对应命名空间.
;; 这种符号的默认空间化可以防止我们不会因为疏忽而重定义了一个已经定义过的值（宏卫生）。
;; 语法引述也返回参数的不求值形式，只是对参数中所有符号求值为其所在命名空间的符号。
;; 语法引述允许反引述
(comment
  (in-ns 'user)
  `foo
  ;; => user/foo
  ;; 新建一个命名空间，并在该命名空间进行以下操作
  (ns aa (:require [user :as u]))

  `map
  ;; => clojure.core/map
  `u/foo
  ;; => user/foo
  `foo
  ;; => aa/foo

  `(+ (* 1 2) 3)
  ;; => (clojure.core/+ (clojure.core/* 1 2) 3)
  )

;;------- ~ ---------
;; ~：反引述
;; 语法引述返回不求值的形式，反引述则相当于告诉语法引述"你不要对我进行语法引述，是被求值的。"
;; 在编写宏时，经常会对列表里的一些元素不进行求值，对另外一些元素进行求值，此时就可以结合语法引述与反引述写成简洁的代码：
;; 语法引述整个列表，而对要进行求值的符号使用反引述。
;; 反引述只能用于被语法引述的符号或列表中
;; 反引述一个列表或vector会把整个形势都反引述。
;;
(comment
  (ns user)
  (def foo 123)
  (def foo1 234)
  `~foo
  ;; => 123
  ;; ~foo 和 ~`foo均错误
  `(map println [~foo foo1])
  ;; => (clojure.core/map clojure.core/println [123 clojure-async.symbols/foo1])
  ;; 其中map和println都被语法引述了，
  `(map println ~[foo foo1])
  ;; => (clojure.core/map clojure.core/println [123 234])
  ;; 其中[]中的两个符号都被反引述了，因此会被求值。因为整个列表被语法引述，所以返回不被求值的列表结构
  )

;;------- ~@ ---------
;; ~@：编接反引述
;; 编接反引述的作用是：自动进行列表的链接，即将一个列表的内容解开，加入到第一个列表中去。
;; 在编写宏时很常见。
;; 编接反引述用于引述的的列表中。
;; 编接反引述不会像反引述那样告诉编译要被求值。
(comment
  (let [defs '((def x 123) (def y 234))]
    `(do ~@defs))
  ;; => (do (def x 123) (def y 234))
  ;; 其中，defs本身是一个列表，(do ...)也是一个列表，~@defs将defs列表中的两个元素解开，加入到(do ...) 这个列表中。
  (defmacro plus
    [& nums]
    `(+ ~@nums))

  (def a 1)
  (def b 2)
  (def c 3)

  (macroexpand-1 '(plus a b c))
  ;; => (clojure.core/+ a b c)
  (plus a b c)
  ;; => 6
  ;; 当你在repl中执行这一句时，编译器先对宏进行求值，返回数据结构：(clojure.core/+ a b c)其中a、b、c都没被求值，
  ;; 在执行期间在对a、b/c求值，然后对个数据结构求值，返回6
  )

;;============ 小结 ==============
;; `， ~， ~@， 都是clojure标准库中的函数，
;; 只是以这种符号的形式暴露出来，便于使用。
;;===============================

;;------ & -----------
;; &：保持"剩下的元素"
;; 在解构中，可以用&符号保持解构剩下来的元素，有点像java中的可变参数

(comment
  (defn &test
    [a & rest]
    rest)

  (&test 1 2 3 4)
  ;; => '(2 3 4)
  ;; 剩下的元素被放入到rest中，rest是一个列表，而不是vector。

  (let [[a & b] [1 2 3]]
    b)
  ;; => (2 3)

  (defn &test2
    [a & [rest]]
    rest)
  (&test2 1 2 3 4)
  ;; => 2
  ;; 有趣的现象：剩下的元素为'(2 3 4)，它被'[rest]解构，并将r绑定到第一个元素2
  ;; 类似于：(let [[r] '(2 3 4)] r) => 2

  (defn &test3
    [a & [r h a]]
    (conj [] r h a))
  (&test3 1 2 3 4)
  ;; => [2 3 4]
  )

;;------ @ ------------
;; @：解引用
;; @等同于deref函数
;; clojure中有很多实体都是可以解引用的，如delay、future、promise以及所有引用类型：atom、ref、agent、var。
(comment
  (def atom-a (atom 10))
  @atom-a
  ;; => 10

  (def a-var 10)
  @(var a-var)
  ;; => 10
  )

;;------ #' ----------
;; #'：用于获得一个符号（Sysmbol）所指向的Var。
;; clojure中，Var有def来创建
;; 等用于var函数，返回值都是[Var](https://clojure.org/reference/vars)
;; clojure中，使用了两层"指针"来绑定"Sysmbol->Var-Function"
;; 也就是说，我们通过一个符号来调用一个函数其实还经历了Var这一层：
;; 先找到符号指向的Var，在找到Var指向的函数。
;; 这样做的好处是，我们可以动态地绑定Var指向某个具体的函数（有点多态的感觉）。

(comment
  #'+
  ;; => #'clojure.core/+
  (var +)
  ;; => #'clojure.core/+
  ;; 表明符号+指向的Var是#'clojure.core/+

  ;;查看#'和var的返回值类型
  (class (var +))
  (class #'+)
  ;; => clojure.lang.Var
  ;; 说明返回值类型是一个Var

  ;; 那#'clojure.core/+指向的函数是什么？可以对Var解引用
  @#'+
  ;; => #object[clojure.core$_PLUS_ 0x77730aa7 "clojure.core$_PLUS_@77730aa7"]
  ;; 说明指向的函数是 #object[clojure.core$_PLUS_ 0x77730aa7 "clojure.core$_PLUS_@77730aa7"]

  ;; Var默认是静态的（static），不可被动态绑定
  (def var-x 1)
  (binding [var-x 1]
    var-x)
  ;; => CompilerException java.lang.IllegalStateException: Can't dynamically bind non-dynamic var: clojure-async.symbols/var-x,

  ;; 在创建一个Var时，可以指定它是可动态绑定的。
  ;; 下面建立了一个可动态绑定的Var，并绑定一个根值为:default-value
  (def ^:dynamic var-y :default-value)
  (binding [var-y 2]
    var-y)
  ;; => 2
  ;; 被动态绑定为2，但是它只作用于binding的作用域内。
  var-y
  ;; => :default-value
  )


;;----加减乘除——+'-'*'----
;; -'：加强版的减。
;;-'函数可以接受一个参数，当只有一个参数时，默认被减数为0；当有多个参数时，和普通减号一样。
;;支持任意精度，不会像普通-那样出现越界异常。


(comment
  (-' 3)
  ;; => -3 默认被减数为0
  (-' 5 2 1)
  ;; => 2 和普通符号一样

  (- 0 9000000000000000000 1000000000000000000)
  ;; => ArithmeticException integer overflow  clojure.lang.Numbers.throwIntOverflow (Numbers.java:1521)
  (-' 0 9000000000000000000 1000000000000000000)
  ;; => => -10000000000000000000N
  )


;;----+'-----
;;+'：加强版的+
;; 接受0到N个数值的参数
;; 支持任意精度

(comment
  (+)
  (+')
  ;; => 0
  (+ 1 Long/MAX_VALUE)
  ;; => ArithmeticException integer overflow  clojure.lang.Numbers.throwIntOverflow (Numbers.java:1521)
  (+' 1 Long/MAX_VALUE)
  ;; => 9223372036854775808N
  )

;;----*'-----
;;*'：加强版的*
;; 接受0到N个数值的参数
;; 支持任意精度

(comment
  (*)
  (*')
  ;; => 1
  (* 2 Long/MAX_VALUE)
  ;; => CompilerException java.lang.ArithmeticException: integer overflow, compiling
  (*' 2 Long/MAX_VALUE)
  ;; => 18446744073709551614N
  )

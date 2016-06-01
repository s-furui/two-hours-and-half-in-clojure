# Perl5ユーザ向けClojure入門

この文書の構成は、
2時間半で学ぶPerl(https://qntm.org/files/perl/perl_jp.html)
を参考に作られています。サンプルコードもこれを元にしています。

## Hello world

Clojureスクリプトは `.clj` もしくは `cljc` という拡張子のテキストファイルです。

`helloworld.clj` は以下のようになります。

```clj
;;; helloworld.clj
(println "Hello world")
```

ClojureはJavaで実装されている

公式ウェブページ
https://clojure.org/
から現行バージョンであるclojure-1.8.0.zipをダウンロードし、解凍してclojure-1.8.0.jarを取り出します。

以下のコマンドでスクリプトを実行できます。

```
java -cp "clojure-1.8.0.jar;." clojure.main helloworld.clj
```

これを覚えるのは大変なので、通常は以下のようなバッチファイルを書いて使います。

```bat
REM clojure.bat
@java -cp "clojure-1.8.0.jar;." clojure.main %1 %2 %3 %4 %5 %6 %7 %8 %9
```

これで以下のように実行できるようになりました。

```
clojure helloworld.clj
```

Clojureでは全てのオペレータは `(オペレータ 引数1 引数2 引数3 ...)` の形式で書かれます。  
ステートメントの区切りの `;` や 引数の区切りの `,` は必要ありません。  
これは算術演算子でも例外ではありません。Perlでは

```pl
1 + 2
2 - 3
3 * 4
4 / 5
```

と書くところを、Clojureでは

```clj
(+ 1 2)
(- 2 3)
(* 3 4)
(/ 4 5)
```

と書きます。

`;` は行コメントの開始です。行末までコメントアウトされます。

## 変数

Clojureの変数には種類は無く、全て型の値を格納することができます。

### リテラル

Clojureで使えるプリミティブなデータ型には以下があります。

- nil - `nil` (Perlにおける `undef` 、Javaにおける `null`
- 数値
  - 整数 - `1`
  - 浮動小数 - `2.3`
  - 任意精度整数 - `123456789N`
  - 任意精度浮動小数 - `1.23456789M`
  - 有理数 - `2/3`
- 文字列 - `"hello"`
- 文字
  - `\a`
  - スペース - `\space`
  - タブ - `\tab`
- 真偽値
  - `true`
  - `false`
- キーワード
  - `:red`
  - `:spade`
  - `:square`

値を変数に格納するには `let` を使います。

```clj
(let [null nil]
  (println null)) ; nil

(let [n 4040.5]
  (println n)) ; 4040.5

(let [s "world"]
  (println s)) ; world
```

`let` は常に新しいスコープを作成します。  
Clojureは、同じスコープ内で変数の中身を書き換えることはできません。  
つまり、「全ての変数は定数」です。

文字列の連結には `str` を使います。

```clj
(let [s "world"]
  (println (str "Hello " s)))
```

### 真偽値

Perlでは偽として評価される値は

- `undef`
- `0`
- `""`
- `"0"`

ですが、Clojureでは

- `nil`
- `false`

が偽として評価されます。

### 強い型付け

Clojureでは演算子によって型が勝手に変わったりしません。
容赦なくエラーで例外を投げます。

```clj
(let [n 10
      s "20"]
  (println (+ n s))) ; java.lang.ClassCastException: java.lang.String cannot be cast to java.lang.Number
```

数値と文字列の比較も当然のように偽を返します。

```clj
(let [n 2
      s "2"]
  (println (= n s))) ; false
```

### コレクション

コレクションとは、任意の型の値を格納できるデータ構造の総称です。  
Clojureには以下のコレクションが存在します。

- リスト
- ベクタ
- マップ
- セット

#### リスト

`list` に引数を渡すと作成できる、順序を持ったデータ構造です。

- 「始めの要素」と「それ以外」
- 「末尾の要素」と「それ以外」

を取り出すことに特価したデータ構造で、

- `first`
- `rest`
- `last`
- `butlast`

だけで全てのデータにアクセスできます。

```clj
(let [lst (list "hoge" "fuga" "piyo")]
  (println (first lst))                 ; hoge
  (println (first (rest (rest lst))))   ; fuga
  (println (last lst))                  ; piyo
  (println (last (butlast lst)))        ; fuga
```

リストが長いと `rest` を何度も書かなければいけないので、 `nth` という組込関数が用意されています。  
注意しないといけないのは、この `nth` は実際に `rest` を複数回適用しているということ。  
インデックスを使ってランダムアクセスするときには後述のベクタを使いましょう。

また、 `count` は要素数を返します。

```clj
(let [lst (list "hoge" "fuga" "piyo")]
  (println (nth lst 1))  ; fuga
  (println (count lst))) ; 3
```

#### ベクタ

ベクタは格納された各要素に整数のインデックスが付くデータ構造で、いわゆる配列です。

ベクタは要素を `[]` で囲むことで作成できます。

Clojureでは、(リスト以外の)データ構造から要素を取り出すときに関数のように記述できます。

```clj
(let [v ["print"
         "these"
         "strings"
         "out"
         "for"
         "me"]]
  (println (v 0))  ; print
  (println (v 1))  ; thene
  (println (v 2))  ; strings
  (println (v 3))  ; out
  (println (v 4))  ; for
  (println (v 5))) ; me
```

ベクタの範囲外の要素にアクセスしようとすると `IndexOutOfBoundsException` を投げます。  
`(ベクタ インデックス)` の代わりに `(get ベクタ インデックス)` でアクセスすると、 `nil` を返します。

```clj
(let [v ["print" "these" "strings" "out" "for"  "me"]]
  (println (get v 5)) ; print
  (println (get v 6)) ; nil
```

#### マップ

マップは格納された要素に任意の型のインデックスを付けられるデータ構造で、
Perlのハッシュ、PHPの連想配列、Pythonの辞書に相当します。

マップは要素の組を `{}` で囲むことで作成できます。

ベクタと同じく、要素にアクセスするには関数のように記述します。

```clj
(let [h {:Newton   "Isaac"
         :Einstein "Albert"
         :Darwin   "Charles"}]
  (println (h :Newton))   ; Isaac
  (println (h :Einstein)) ; Albert
  (println (h :Darwin)))  ; Charles
```

マップは存在しない要素にアクセスすると `nil` を返します。

```clj
(let [h {:Newton   "Isaac"
         :Einstein "Albert"
         :Darwin   "Charles"}]
  (println (h :Galilei))) ; nil
```

#### セット

セットは順序と重複のないデータ構造です。
Perlでは1/0の値を持つハッシュを使って実現していましたが、Clojureには組込でサポートされます。

セットは要素を `#{}` で囲むことで作成できます。

ベクタ、マップと同じく、要素にアクセスするには関数のように記述します。  
存在しない要素にアクセスすると `nil` を返します。

```clj
(let [s #{:red :green :blue}]
  (println (s :red))     ; :red
  (println (s :orange))) ; nil
```

## コンテキスト

Clojureでは、関数は `()` の先頭にあるときと、先頭以外にあるときで扱いが異なります。

`()` の先頭にあるときは「関数適用」とみなされます。

`()` の先頭以外にあるときは「関数オブジェクト」とみなされます。

```clj
(println (+ 2 3)) ; 5

((partial + 2) 3) ; 5
```

## ネストされたデータ構造

```clj
(let [inner [:Earth :Moon]
      outer [:Sun :Mercury :Venus inner :Mars]]
  (println outer)
  (println (outer 3))
  (println ((outer 3) 1)))

(let [colour "Indigo"]
  (prn colour))

(let [colours ["Red" "Orange" "Yellow" "Green" "Blue"]]
  (prn (colours 0)))

(let [atomic-weights {:Hydrogen 1.008
                      :Helium 4.003
                      :Manganese 54.94}]
  (prn (atomic-weights :Helium)))
```

### データ構造を宣言する

```clj
(let [owner1 {:name "Santa Claus"
             :DOB "1882-12-25"}
      owner2 {:name "Mickey Mouse"
              :DOB "1928-11-18"}
      owners [owner1 owner2]
      account {:number "12345678"
               :opened "2000-01-01"
               :owners owners}]
  (println account))

(let [account {:number "12345678"
               :opened "2000-01-01"
               :owners [{:name "Santa Claus"
                         :DOB "1882-12-25"}
                        {:name "Mickey Mouse"
                         :DOB "1928-11-18"}]}]
  (println account))
```

### データ構造から情報を取り出す

```clj
(let [account {:number "12345678"
               :opened "2000-01-01"
               :owners [{:name "Santa Claus"
                         :DOB "1882-12-25"}
                        {:name "Mickey Mouse"
                         :DOB "1928-11-18"}]}]
  (println (account :owners))
  (println ((account :owners) 0))
  (println (((account :owners) 0) :DOB))
  (println (get-in account [:owners 0 :DOB]))
  (println (str "Account #" (account :number)))
  (println (str "Opened on " (account :opened)))
  (println "Joint owners:")
  (println (str "\t"
                (get-in account [:owners 0 :name])
                " (born "
                (get-in account [:owners 0 :DOB])
                ")"))
  (println (str "\t"
                (get-in account [:owners 1 :name])
                " (born "
                (get-in account [:owners 1 :DOB])
                ")")))
```

## 条件

### if / when

```clj
(let [word "antidisestablishmentarianism"
      strlen (count word)]
  (if (>= strlen 15)
    (println (str "'" word "' is a very long word"))
    (if (and (>= strlen 10) (< strlen 15))
      (println (str "'" word "' is a medium-length word"))
      (println (str "'" word "' is a short word"))))
  (when (>= strlen 20) (println (str "'" word "' is actually enormous"))))
```
### if not / when-not

```clj
(let [temperature 20]
  (if (not (> temperature 30))
    (println (str temperature " degree Celsius is not very hot"))
    (println (str temperature " degree Celsius is actually pretty hot")))
  (if ((complement >) temperature 30)
    (println (str temperature " degree Celsius is not very hot"))
    (println (str temperature " degree Celsius is actually pretty hot")))
  (when-not (> temperature 15)
    (println "Oh no it's too cold")))
```

### if式 / cond式

```clj
(let [gain 48]
  (println (str "You gained " gain " " (if (= gain 1) "experience point" "experience points") "!")))

(let [lost 1]
  (println (str "You lost " lost " t" (if (= lost 1) "oo" "ee") "th!")))

(let [eggs 5]
  (println (str "You hove " (cond (= eggs 0) "no eggs"
                                  (= eggs 1) "an egg"
                                  :default "some eggs"))))
```

## ループ

### dotimes / doseq

```clj
(let [v ["print"
         "these"
         "strings"
         "out"
         "for"
         "me"]]
  (dotimes [i (count v)]
    (println (str i ": " (v i))))
  (doseq [s v]
    (println s))
  (doseq [[i s] (map-indexed vector v)]
    (println (str i ": " s)))
  (doseq [i (range (count v))]
    (println (str i ": " (v i)))))

(let [scientists {:Newton "Isaac"
                  :Einstein "Albert"
                  :Darwin "Charles"}]
  (doseq [k (keys scientists)]
    (println (str k ": " (scientists k))))
  (doseq [k (sort (keys scientists))]
    (println (str k ": " (scientists k))))
  (doseq [[k v] scientists]
    (println (str k ": " v))))
```
### loop / recur

```clj

(loop [candidate 2
       divisor 2]
  (cond
    (> candidate 100) nil
    (> divisor (Math/sqrt candidate)) (do
                                        (println (str candidate " is prime"))
                                        (recur (inc candidate) 2))
    (zero? (mod candidate divisor)) (recur (inc candidate) 2)
    :default (recur candidate (inc divisor))))

(letfn [(prime? [n] (empty? (filter #(zero? (mod n %))
                                    (range 2 (Math/sqrt n)))))]
  (doseq [i (filter prime? (range 2 100))]
    (println (str i " is prime"))))
```

## シーケンス関数

```clj
(let [stack ["Fred" "Eileen" "Denise" "Charlie"]]
  (println (pop stack))
  (println (peek stack))
  (println (rest stack))
  (println (conj stack "Bob" "Alice"))
  (println (cons "Hank" (cons "Grace" stack))))

(let [elements ["Antimony" "Arsenic" "Alminum" "Selenium"]]
  (println elements)
  (println (str elements))
  (println (interpose ", " elements))
  (println (apply str (interpose ", " elements)))
  (println (clojure.string/join ", " elements)))

(require '[clojure.string :as str])

(let [capitals ["Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"]]
  (println (str/join ", " (map #(str/upper-case %) capitals)))
  (println (str/join ", " (map str/upper-case capitals))))

(let [capitals ["Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"]]
  (println (str/join ", " (filter #(= (count %) 6) capitals))))

(let [capitals #{"Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"}]
  (println (capitals "Columbus")))

(let [elevations [19 1 2 100 3 98 100 1056]]
  (println (str/join ", " (sort elevations)))
  (println (str/join ", " (sort (map str elevations)))))
```

## ユーザー定義の関数

```clj
(defn hyphenate [word]
  (str/join "-" word))

(hyphenate "exterminate")
```

### 引数を取り出す

```clj
(defn left-pad [old-string width pad-char]
  (str (str/join (repeat (- width (count old-string)) pad-char))
       old-string))

(left-pad "hello" 10 "+")
(defn left-pad [{old-string :old-string
                 width :width
                 pad-char :pad-char}]
  (str (str/join (repeat (- width (count old-string)) pad-char))
       old-string))

(left-pad {:old-string "hello"
           :width 10
           :pad-char "+"})
```

## システムコール

```clj
(require '[clojure.java.shell :refer [sh]])

(println (sh "perl" "anotherscript.pl" "foo" "bar" "baz"))
```

## ファイルとファイルリーダー

```clj
(require '[clojure.java.io :as io])

(with-open [r (io/reader "text.txt" :encoding "JISAutoDetect")]
  (println (line-seq r)))
```

### ファイルテスト

```clj
(.exists (io/file "C:" "Windows"))
(.isFile (io/file "C:" "Windows"))
(.isDirectory (io/file "C:" "Windows"))
```

## 正規表現

```clj
(let [s "Hello world"]
  (println (re-find #"(\w+)\s+(\w+)" s)))

(let [s "colourless green ideas sleep furiously"]
  (println (str/join ", " (rest (re-find #"(\w+)\s+((\w+)\s+(\w+))\s+(\w+)\s+(\w+)" s)))))

(let [s "Goo morning world"]
  (println (str/replace s #"world" "Vietnam")))

(let [s "a tonne of feathers or a tonne of bricks"]
  (doseq [m (re-seq #"(\w+)" s)]
    (println (str "'" (first m) "'")))
  (println (str/replace-first s #"[aeiou]" "r"))
  (println (str/replace-first (str/replace-first s #"[aeiou]" "r") #"[aeiou]" "r"))
  (println (str/replace s #"[aeiou]" "r")))

(println (re-find #"(?i)hello" "Hello world"))
```

## ライブラリと名前空間

### ライブラリ

```clj
;;; demo/string_utils.clj
(require '[clojure.string :as str])

(defn zombify [word]
  (str/replace word #"[aeiou]" "r"))
```

```clj
(require 'demo.string-utils)

(println (zombify "i want brains"))
```

### 名前空間

```clj
;;; food/subroutine.clj
(defn subroutine []
  (prn "universe"))

(ns food.potatoes)

(defn subroutine []
  (prn "kingedward"))
```

```clj
(require '[food.subroutine])

(subroutine)
(user/subroutine)
(food.potatoes/subroutine)
```

```clj
;;; demo/string_utils.clj
(ns demo.string-utils)

(require '[clojure.string :as str])

(defn zombify [word]
  (str/replace word #"[aeiou]" "r"))
```

```clj
(require 'demo.string-utils)

(println (demo.string-utils/zombify "i want brains"))
```

## マルチメソッドとヒエラルキー

### 型宣言
 
```clj
(defprotocol Animal
  (can-eat [this food]))

(defrecord Koala []
  Animal
  (can-eat [this food] (= food "eucalyptus")))

(defn eat [animal & foods]
  (doseq [food foods]
    (println (str (if (can-eat animal food)
                "Eating "
                "Can't eat ")
              food))))

(let [animal (assoc (Koala.)
               :legs 4
               :color "blown")]
  (println animal)
  (println (eat animal "insects" "curry" "eucalyptus")))
```

## プライベート関数

```clj
;;; caterpillar.clj
(ns caterpillar)

(defn crawl []
  (prn "inch inch"))

(defn eat []
  (prn "chomp chomp"))

(defn- pupate []
  (prn "bloop bloop"))
```

```clj
(require 'caterpillar)

(caterpillar/crawl)
(caterpillar/eat)

(shutdown-agents)
```

## その他注意

- `println` の代わりに `prn` を使うとClojureが再度読み込める形式でデータ構造を出力します。これはPerlの `Data::Dumper`の挙動と似ています。
- Clojureにはヒアドキュメント構文はありません

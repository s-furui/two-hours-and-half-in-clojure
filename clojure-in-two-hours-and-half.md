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

公式ウェブページ
https://clojure.org/
から現行バージョンであるclojure-1.8.0.zipをダウンロードし、解凍してclojure-1.8.0.jarを取り出します。

以下のコマンドでスクリプトを実行できます。Javaがインストールされている必要があります。

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

これは算術演算子でも例外ではなく、Perlでは

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

Clojureには以下のようなデータ型があります。

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
  (println (+ n s))) ; String cannot be cast to Number
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
  (println (get v 5))  ; print
  (println (get v 6))) ; nil
```

ベクタに要素を追加することはできません。  
`conj` を使うと要素が追加された新しいベクタを作ることができます。

```clj
(let [v [:a :b :c]]
  (println (conj v :d))) ; [:a :b :c :d]
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

マップに要素を追加することはできません。  
`conj` を使うと要素が追加された新しいマップを作ることができます。

```clj
(let [m {:a 1
         :b 2
         :c 3}]
  (println (conj m [:d 4]))) ; {:a 1, :b 2, :c 3, :d 4}
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

セットに要素を追加することはできません。  
`conj` を使うと要素が追加された新しいセットを作ることができます。

```clj
(let [s #{:a :b :c}]
  (println (conj s :d))) ; #{:a :c :b :d}
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

前述のとおり、Clojureのデータ構造は任意の型を格納できるため、そのままネストできます。

ネストされたデータ構造からデータを取り出すときに楽に記述する方法は  
「データ構造から情報を取り出す」で述べます。

```clj
(let [inner [:Earth :Moon]
      outer [:Sun :Mercury :Venus inner :Mars]]
  (println (outer 3))      ; [:Earth :Moon]
  (println ((outer 3) 1))) ; :Moon
```

### データ構造を宣言する

複雑なデータ構造を作るためには、以下のようにネストされたデータ構造に1つ1つ名前を付けるか、

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
```

以下のようにデータ構造を直接入れ子にして書きます。  
同じデータ構造を異なる箇所から使わない場合は、こちらの書き方が一般的です。

```clj
(let [account {:number "12345678"
               :opened "2000-01-01"
               :owners [{:name "Santa Claus"
                         :DOB "1882-12-25"}
                        {:name "Mickey Mouse"
                         :DOB "1928-11-18"}]}]
  (println account))
```

### データ構造から情報を取り出す

ネストされたデータ構造から値を取り出すときに、関数呼び出しのようにアクセスすると括弧の数がすごいことになります。  
`get-in` を使うと、ネストしたデータ構造へのアクセスがシンプルに書けます。

```clj
(let [account {:number "12345678"
               :opened "2000-01-01"
               :owners [{:name "Santa Claus"
                         :DOB "1882-12-25"}
                        {:name "Mickey Mouse"
                         :DOB "1928-11-18"}]}]
  (println (((account :owners) 0) :DOB))       ; 1882-12-25
  (println (get-in account [:owners 0 :DOB])   ; 1882-12-25
  (println (((account :owners) 1) :name))      ; Mickey Mouse
  (println (get-in account [:owners 1 :name])) ; Mickey Mouse
```

## 条件

### if / when

条件分岐には `if` 式を使います。  
`(if 判別式 真のとき 偽のとき)`  
で、判別式によって評価される式を変えることができます。

これの派生に `when` 式があります。  
`(when 判別式 真のとき(結果は捨てる) ... 真のとき)`  
こちらは、「偽のとき」がなくて、真の時に複数の式を評価するときに使います。  
最後の式以外の結果は捨てられます。

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

Perlの `unless` に相当する組込オペレータはありませんが、 `when` の逆の `when-not` はあります。

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

Perlの後置 `if` に相当する構文はありません。  
全てのオペレータが前置なのであったらおかしいですね。

### if式 / cond式

`if` 式は評価した結果を返します。  
これは、三項演算子のように使えることを意味します。

```clj
(let [gain 48]
  (println (str "You gained " gain " " (if (= gain 1) "experience point" "experience points") "!")))

(let [lost 1]
  (println (str "You lost " lost " t" (if (= lost 1) "oo" "ee") "th!")))
```

`if` 式はネストする読み難いので、 `cond` 式を使います。  

```
(let [eggs 5]
  (println (str "You hove " (cond (= eggs 0) "no eggs"
                                  (= eggs 1) "an egg"
                                  :default "some eggs"))))
```

`cond` 式の最後の `:default` は真に評価される値ならなんでもいいです。

## ループ

### dotimes / doseq

単純なループには `dotimes`、`doseq` を使います。
`dotimes` は指定した回数だけ繰り返します。
ループの内側では0から始まるカウンター変数が見えます。

`doseq` はコレクションの各要素を取り出して要素数だけ繰り返します。

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
```

マップの要素でループするには `keys` や `vals` を使ってキーのシーケンス、
値のシーケンスを取り出してもいいですが、ループ変数を `[k v]` とすることでその両方が見えるようになります。

キーをソートしながら繰り返したい等の場合には `keys` を使う必要があります。

```clj
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

ループ条件が複雑な場合は `loop` と `recur` を使います。  

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
```

考えかたは関数を使った再帰に近いです。 `loop` の内側からだけ見える `recur` という関数があり、
更新された引数を渡しながら再帰するイメージです。

基本的に、繰り返しながらファイルに出力したりする用途以外には使われません。  
上記の例も、後述するシーケンス関数を使って以下のように書くことができ、こちらのほうが推奨されます。

```clj
(letfn [(prime? [n] (empty? (filter #(zero? (mod n %))
                                    (range 2 (Math/sqrt n)))))]
  (doseq [i (filter prime? (range 2 100))]
    (println (str i " is prime"))))
```

## シーケンス関数

Clojureの

- リスト
- ベクタ
- マップ
- セット

に共通して使える関数群がシーケンス関数です。
これらに限らず、Clojureでは引数に渡した値を書き換えることができません。  
なので、Perlの同じ名前の組込関数と挙動が異なります。

- `first` - 先頭の要素を返します
- `rest` - 先頭を除いた残りのシーケンスを返します
- `cons` - 先頭に要素を追加した新しいシーケンスを返します

```clj
(let [stack ["Fred" "Eileen" "Denise" "Charlie"]]
  (println (first stack))
  (println (rest stack))
  (println (cons "Hank" (cons "Grace" stack))))
```

ベクタに格納された複数の文字列を、 `,` で区切った1つの文字列に変換してみます。  
`interpose` は各要素の間に特定の要素を挟んだシーケンスを返します。  
文字列が格納されたシーケンスをまとめて1つの文字列にするには `apply str` を使います。

```clj
(let [elements ["Antimony" "Arsenic" "Alminum" "Selenium"]]
  (println elements)
  (println (str elements))
  (println (interpose ", " elements))
  (println (apply str (interpose ", " elements))))
```

上記は頻繁に使われる変換なので、 `clojure.string` ライブラリに関数として定義されています。  
`clojure.string/join` です。

```clj
(let [elements ["Antimony" "Arsenic" "Alminum" "Selenium"]]
  (println (clojure.string/join ", " elements))
```

毎回 `clojure.string` と書くのが面倒なので、以下のように別名を付けることができます。

```clj
(require '[clojure.string :as str])
```

### map

`map` 関数はシーケンスの各要素に関数を適用した結果のシーケンスを返します。  
`map` 関数は関数オブジェクトを引数に取る関数です。
関数オブジェクトを得るためには3つの方法があります。

- 関数に名前が付いている場合はその名前
- `(fn [引数] 定義)` で無名関数を作る
- `#(定義)` で無名関数を作る

`#(定義)` は `(fn [引数] 定義)` の略記法です。  
最初の引数は `%` もしくは `%1` という名前が付けられます。  
以下、引数が増えると `%2`、`%3` と名前が付けられていきます。

```clj
(let [capitals ["Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"]]
  (println (str/join ", " (map (fn [s] (str/upper-case s)) capitals)))
  (println (str/join ", " (map #(str/upper-case %) capitals)))
  (println (str/join ", " (map str/upper-case capitals))))
;; BATON ROUGE, INDIANAPOLIS, COLUMBUS, MONTGOMERY, HELENA, DENVER, BOISE
```

### filter

`filter` 関数はシーケンスから条件に一致する要素だけを含むシーケンスを返す関数です。

```clj
(let [capitals ["Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"]]
  (println (str/join ", " (filter #(= (count %) 6) capitals)))) ; Helena, Denver
```

以下のように `filter` を使ってシーケンスに特定よ要素が含まれていることを確かめることができますが、

```clj
(let [capitals ["Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"]]
  (println (empty? (filter #(= % "Columbus") capitals))))
```

このような状況ではセットを使うことが推奨されます。

```clj
(let [capitals #{"Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"}]
  (println (capitals "Columbus")))
```

### sort

`sort` 関数はソートされたシーケンスを返します。  
デフォルトのソート順は要素の型によって決まります。

```clj
(let [elevations [19 1 2 100 3 98 100 1056]]
  (println (str/join ", " (sort elevations)))            ; 1, 2, 3, 19, 98, 100, 100, 1056
  (println (str/join ", " (sort (map str elevations))))) ; 1, 100, 100, 1056, 19, 2, 3, 98
```

ちなみに異なる型が混ざったりしていると例外を投げます。

1引数目に「2引数で-1/0/1を返す関数」を取ることで、任意のソート順でソートすることができます。

### reduce

`reduce` 関数はシーケンスの要素を順番に2項演算を適用した結果を返します。

例えば、 `1` から `n + 1` までの整数を順番に掛けていけば `n!` が求められます。

```clj
(println (reduce * (range 1 (inc 10)))) ; 3628800
```

## ユーザー定義の関数

名前付き関数を定義するには `defn` を使います。
構文は  
`(defn 名前 [引数] 定義)`  
です。 

関数は最後に評価した値を返します。

```clj
(defn hyphenate [word]
  (str/join "-" word))

(hyphenate "exterminate") ; e-x-t-e-r-m-i-n-a-t-e
```

### 引数を取り出す

以下のように引数に1つ1つに名前を付ける方法と、

```clj
(defn left-pad [old-string width pad-char]
  (str (str/join (repeat (- width (count old-string)) pad-char))
       old-string))

(left-pad "hello" 10 "+") ; +++++hello
```

`[& args]` のように可変長引数を取るための構文があります。  
`args` には引数のシーケンスが入ります。

```clj
(defn left-pad [& args]
  (let [old-string (first args)
        width      (first (rest args))
        pad-char   (first (rest (rest args)))]
    (str (str/join (repeat (- width (count old-string)) pad-char))
         old-string)))

(println (left-pad "hello" 10 "+")) ; +++++hello
```

引数にマップと取る場合、特殊な構文を使うことができます。  
マップリテラルのキーとバリューを逆転させたような記法を使うことで、  
マップの要素を直接変数に格納するこがでます。

```clj
(defn left-pad [& {old-string :old-string
                   width :width
                   pad-char :pad-char}]
  (str (str/join (repeat (- width (count old-string)) pad-char))
       old-string))

(left-pad :old-string "hello"
          :width 10
          :pad-char "+") ; +++++hello
```

この仕組みは「デストラクチャリング」と呼ばれており、  
ローカル変数を定義する `let` や `doseq` などでも同様に使えます。

## システムコール

外部のプロセスを起動することが(一応)できます。  
Javaのライブラリでできることに終始するのが推奨されます。  
どうしても、というときにだけ使いましょう。

たとえば以下のようなPerlスクリプトがあったのとして、これを実行したいとします。

```pl
# anotherscript.pl
use strict;
use warnings;

print @ARGV;
exit 37;
```

`clojure.java.shell/sh` を使います。  
実行ファイルとコマンドライン引数を引数に渡すと別のスレッドで実行し、結果をマップで返します。

```clj
(require '[clojure.java.shell :refer [sh]])

(println (sh "perl" "anotherscript.pl" "foo" "bar" "baz"))
;; {:exit 37, :out "foobarbaz", :err ""}

(shutdown-agents)
```

`(require '[clojure.java.shell :refer [sh]])` とすると、名前空間から特定の変数だけをインポートすることができます。

`(shutdown-agents)` ですが、`clojure.java.shell/sh` が裏で使っている
Agentと呼ばれるバックグラウンドプロセスを終了させる命令です。  
これが無いと全てAgentがタイムアウトするまでメインプロセスが終了しないため、スクリプトの実行が終わっても1分程度Javaが終了しなくなります。

## ファイルとファイルリーダー

ファイルの読み込みを行なうには `clojure.java.io/reader` を使います。

以下のような内用の `text.txt` を用意してこれを読み込んでみます。

```
こんにちわ、世界
ほげ
ふが
ぴよ
```

`clojure.java.io/reader` は
`:encodeing` キーワードの後にエンコーディングを指定することができます。  

- `"UTF-8"`
- `"MS932"`

等が指定できますが、入力であれば `"JISAutoDetect"` を指定すれば自動判別が可能です。

`clojure.java.io/reader` は `java.io.Reader` 型のオブジェクトを返します。  
以下の例では `line-seq` を使ってリーダーから列のシーケンスを得ています。

```clj
(require '[clojure.java.io :as io])

(with-open [r (io/reader "text.txt" :encoding "JISAutoDetect")]
  (println (line-seq r)))
;; ("こんにちわ、世界" "ほげ" "ふが" "ぴよ")
```

`clojure.java.io/reader` はファイル名の他にURL、ソケット等を引数に取ることができます。

### ファイルテスト

Perlにはファイルやディレクトリの状態をチェックする演算子が存在しますが、  
ClojureはJavaの機能を直接使うことで対応します。

`clojure.java.io/file` は `Java.io.File` 型のオブジェクトを返します。  
このオブジェクトのメソッドを呼ぶことでファイルの状態をチェックします。

Javaオブジェクトのメソッドを呼ぶための構文は

```clj
(.メソッド名 オブジェクト メソッドの引数 ...)
```

です。

```clj
(require '[clojure.java.io :as io])

(.exists (io/file "C:" "Windows"))      ; true
(.isFile (io/file "C:" "Windows"))      ; false
(.isDirectory (io/file "C:" "Windows")) ; true
```

## 正規表現

`#""` で囲むと正規表現パターンを作ることができます。  
正規表現扱う関数は

- `re-find`
- `re-seq`
- `clojure.string/replace`

等があります。

`re-find` は正規表現を探して結果をベクタにして返す関数です。
ベクタの最初はマッチした全体、以降はグループ化した部分のマッチが格納されます。

```clj
(let [s "Hello world"]
  (prn (re-find #"(\w+)\s+(\w+)" s)))
;; ["Hello world" "Hello" "world"]

(require '[clojure.string :as str])

(let [s "colourless green ideas sleep furiously"]
  (println (str/join ", " (rest (re-find #"(\w+)\s+((\w+)\s+(\w+))\s+(\w+)\s+(\w+)" s)))))
;; colourless, green ideas, green, ideas, sleep, furiously
```

`clojure.string/replace` はマッチした部分を指定した文字列に *全て* 置換した文字列を返します。
`clojure.string/replace-first` はマッチした部分を指定した文字列に *1つだけ* 置換した文字列を返します。

```clj
(let [s "Goo morning world"]
  (println (str/replace s #"world" "Vietnam")))
;; Goo morning Vietnam

(let [s "a tonne of feathers or a tonne of bricks"]
  (println (str/replace-first s #"[aeiou]" "r"))
  (println (str/replace-first (str/replace-first s #"[aeiou]" "r") #"[aeiou]" "r"))
  (println (str/replace s #"[aeiou]" "r")))
;; r tonne of feathers or a tonne of bricks
;; r trnne of feathers or a tonne of bricks
;; r trnnr rf frrthrrs rr r trnnr rf brrcks
```

繰り返しになりますが、引数に渡した文字列は変更されません。

`re-seq` は正規表現にマッチした部分をベクタのシーケンスとして返します。

```clj
(let [s "a tonne of feathers or a tonne of bricks"]
  (doseq [m (re-seq #"(\w+)" s)]
    (println (str "'" (first m) "'")))
```

`#""` で作られるのは `java.util.regex.Pattern` のオブジェクトです。  
正規表現の記述ルールもこれに従うので、記述ルールについては  
https://docs.oracle.com/javase/jp/8/docs/api/java/util/regex/Pattern.html  
を参照のこと。

例: `(?i)` で大文字/小文字を区別しないようにする。

```clj
(prn (re-find #"(?i)hello" "Hello world")) ; "Hello"
```

## ライブラリと名前空間

Perlのモジュールとパッケージに相当します。

Perlと同様、ライブラリと名前空間は別物です。

### ライブラリ

ライブラリは `require` で読み込むことができるパスに配置された、  
Clojureのソースコード(またはJavaのクラスファイル)です。

「読み込むことができるパス」はJavaのクラスパスの影響を受けます。  
上述の `clojure.bat` でカレントディレクトリ `.` をクラスパスに追加しているので、

`(require 'demo.string-utils)` で読み込むことができるライブラリは  
`./demo/string_utils.clj` に配置されたClojureソースコードです。

`-` が `_` に変わっていることに注意。 だいたいの人がハマります。

```clj
;;; demo/string_utils.clj
(require '[clojure.string :as str])

(defn zombify [word]
  (str/replace word #"[aeiou]" "r"))
```

ライブラリに書かれた関数が `require` することで評価できることを確認します。

```clj
(require 'demo.string-utils)

(println (zombify "i want brains")) ; r wrnt brrrns
```

### 名前空間

`ns` を使うと名前空間を切り替えることができます。  
デフォルトの名前空間は `user` なので、いままで定義してきたユーザ定義関数はすべて  
`user/関数名` という名前でした。

Perlでは `main` でしたね。

以下の例では、ライブラリ `food.subroutine` に異なる名前空間、同じ名前の関数を定義しています。

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

(subroutine)               ; universe
(user/subroutine)          ; universe
(food.potatoes/subroutine) ; kingedward
```
`subroutine` が `user/subroutine` の略で、  
`food.potatoes/subroutine` が別関数が呼ばれていることがわかります。

実用的には、  
ライブラリ名と名前空間を同じにしておくことで混乱なく再利用できる部品を作ることができます。

上述したライブラリ `demo.string-utils` は以下のように書かれるべきでした。

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

`:as` で名前空間に別名を付けたり、

```clj
(require '[demo.string-utils :as dsu])

(println (dsu/zombify "i want brains"))
```

`:refer` 特定の関数だけインポートしたりして便利に使えます。

```clj
(require '[demo.string-utils :refer [zombify]])

(println (zombify "i want brains"))
```

## マルチメソッドとヒエラルキー

マルチメソッドは、引数の型や状態によって呼ばれる関数の実装を切り替えることです。  
Perlではパッケージに継承関係を持たせることでマルチメソッドを実現しています。

Clojureは、 Perlのような型によるマルチメソッドと、  
より汎用的な(しかしやや遅い)値によるマルチメソッドを両方備えています。

### 型によるマルチメソッド

Clojureで新しい型を作るには `defrecord` を使います。

```clj
(defrecord Koala [])
```

まず `defprotocol` で関数がメソッドであるという宣言を行ないます。

```clj
(defprotocol Animal
  (can-eat [this food]))
```

`extend-protocol` で特定の型のメソッドを実装します。

```clj
(extend-protocol Animal
  Koala
  (can-eat [this food] (= food "eucalyptus")))
```

あとは関数のように評価するだけです。

```clj
(defn eat [animal & foods]
  (doseq [food foods]
    (println (str (if (can-eat animal food)
                "Eating "
                "Can't eat ")
              food))))
```

定義した型のインスタンスを作るには `(型名.)` と書きます。

```clj
(let [animal (Koala.)]
  (println (eat animal "insects" "curry" "eucalyptus")))
;; Can't eat insects
;; Can't eat curry
;; Eating eucalyptus
```

### 値によるマルチメソッド

`defmulti` で指定した関数で評価された値が、  
`defmethod` の第2引数にマッチする実装が呼ばれる、というシンプルな仕組みです。

```clj
(defmulti fizzbuzz (fn [n] [(zero? (mod n 3))
                            (zero? (mod n 5))]))
(defmethod fizzbuzz [true  true]  [n] "FizzBuzz")
(defmethod fizzbuzz [true  false] [n] "Fizz")
(defmethod fizzbuzz [false true]  [n] "Buzz")
(defmethod fizzbuzz [false false] [n] n)

(println (fizzbuzz 1))  ; 1
(println (fizzbuzz 6))  ; Fizz
(println (fizzbuzz 10)) ; Buzz
(println (fizzbuzz 30)) ; FizzBuzz
```

## プライベート関数

`defn-` を使って関数を定義すると、他の名前空間から読み込むことができなくなります。

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

評価しようとすると `IllegalStateException` を投げます。

```clj
(require 'caterpillar)

(caterpillar/crawl)  ; inch inch
(caterpillar/eat)    ; chomp chomp
(caterpillar/pupate) ; IllegalStateException
```

## その他注意

- `println` の代わりに `clojure.pprint/pprint` を使うと読みやすい形式でデータ構造を出力します。これはPerlの `Data::Dumper`の挙動と似ています。
- Clojureにはヒアドキュメント構文はありません

# もっとClojure

元ネタである「2時間半で学ぶPerl」の内容は押さえました。  
ここまでで、出てこなかった機能と標準ライブラリを下に列挙しておきます。

- コマンドラインツール
- REPL
- クォート - `(require 'hoge.fuga)` の `'`
- シンタックスクォート
- 遅延シーケンス
- グローバル変数
- ダイナミック変数
- 例外処理
- Java連携 - (インスタンスメソッドコールだけ)
- マクロ
- transient
- transducer
- ref と STM
- agent - (`clojure.java.shell/sh` で使われている)
- atom
- reducer
- ClojureScriptとClojureCLR
- clojure.core.async で非同期プログラミング
- clojure.core.logic で論理プログラミング
- clojure.text で自動テスト

これらを使わずとも実用できることが分かっていただけたかと思います。

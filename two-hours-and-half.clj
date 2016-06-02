(prn "Hello world")

(let [null nil]
  (prn null))

(let [n 4040.5]
  (prn n))

(let [s "world"]
  (prn s)
  (prn (str "Hello " s)))

(let [s1 "4G"
      s2 "4H"]
  (prn (str s1 s2))
  (prn (= s1 s2)))

(prn (= 2 "2"))

(let [lst (list "print"
                "these"
                "strings"
                "out"
                "for"
                "me")]
  (prn (first lst))
  (prn (first (rest lst)))
  (prn (first (rest (rest lst))))
  (prn (first (rest (rest (rest lst)))))
  (prn (first (rest (rest (rest (rest lst))))))
  (prn (first (rest (rest (rest (rest (rest lst)))))))
  (prn (first (rest (rest (rest (rest (rest (rest lst))))))))
  (prn (last lst))
  (prn (last (butlast lst)))
  (prn (last (butlast (butlast lst))))
  (prn (last (butlast (butlast (butlast lst)))))
  (prn (last (butlast (butlast (butlast (butlast lst))))))
  (prn (last (butlast (butlast (butlast (butlast (butlast lst)))))))
  (prn (last (butlast (butlast (butlast (butlast (butlast (butlast lst))))))))
  (prn (nth lst 3))
  (prn (count lst)))


(let [lst (list "hoge" "fuga" "piyo")]
  (println lst) ; (hoge fuga piyo)
  (println (first lst)) ; hoge
  (println (first (rest lst))) ; fuga
  (println (last lst)) ; piyo
  (println (last (butlast lst))) ; fuga
  (println (nth lst 1)) ; fuga
  (println (count lst))) ; 3

(let [v ["print"
         "these"
         "strings"
         "out"
         "for"
         "me"]]
  (prn v)
  (prn (v 0))
  (prn (v 1))
  (prn (v 2))
  (prn (v 3))
  (prn (v 4))
  (prn (v 5)))

(let [h {:Newton "Isaac"
         :Einstein "Albert"
         :Darwin "Charles"}]
  (prn h)
  (prn (h :Newton))
  (prn (h :Einstein))
  (prn (h :Darwin)))

(let [inner [:Earth :Moon]
      outer [:Sun :Mercury :Venus inner :Mars]]
  (prn outer)
  (prn (outer 3))
  (prn ((outer 3) 1)))

(let [colour "Indigo"
      colours ["Red" "Orange" "Yellow" "Green" "Blue"]]
  (prn (colours 0)))

(let [atomic-weights {:Hydrogen 1.008
                      :Helium 4.003
                      :Manganese 54.94}]
  (prn (atomic-weights :Helium)))

(let [owner1 {:name "Santa Claus"
             :DOB "1882-12-25"}
      owner2 {:name "Mickey Mouse"
              :DOB "1928-11-18"}
      owners [owner1 owner2]
      account {:number "12345678"
               :opened "2000-01-01"
               :owners owners}]
  (prn account))

(let [account {:number "12345678"
               :opened "2000-01-01"
               :owners [{:name "Santa Claus"
                         :DOB "1882-12-25"}
                        {:name "Mickey Mouse"
                         :DOB "1928-11-18"}]}]
  (prn account)
  (prn (account :owners))
  (prn ((account :owners) 0))
  (prn (((account :owners) 0) :DOB))
  (prn (get-in account [:owners 0 :DOB]))
  (prn (str "Account #" (account :number)))
  (prn (str "Opened on " (account :opened)))
  (prn "Joint owners:")
  (prn (str "\t"
                (get-in account [:owners 0 :name])
                " (born "
                (get-in account [:owners 0 :DOB])
                ")"))
  (prn (str "\t"
                (get-in account [:owners 1 :name])
                " (born "
                (get-in account [:owners 1 :DOB])
                ")")))

(let [word "antidisestablishmentarianism"
      strlen (count word)]
  (if (>= strlen 15)
    (prn (str "'" word "' is a very long word"))
    (if (and (>= strlen 10) (< strlen 15))
      (prn (str "'" word "' is a medium-length word"))
      (prn (str "'" word "' is a short word"))))
  (when (>= strlen 20) (prn (str "'" word "' is actually enormous"))))

(let [temperature 20]
  (if (not (> temperature 30))
    (prn (str temperature " degree Celsius is not very hot"))
    (prn (str temperature " degree Celsius is actually pretty hot")))
  (if ((complement >) temperature 30)
    (prn (str temperature " degree Celsius is not very hot"))
    (prn (str temperature " degree Celsius is actually pretty hot")))
  (when-not (> temperature 15)
    (prn "Oh no it's too cold")))

(let [gain 48]
  (prn (str "You gained " gain " " (if (= gain 1) "experience point" "experience points") "!")))

(let [lost 1]
  (prn (str "You lost " lost " t" (if (= lost 1) "oo" "ee") "th!")))

(let [eggs 5]
  (prn (str "You hove " (cond (= eggs 0) "no eggs"
                                  (= eggs 1) "an egg"
                                  :default "some eggs"))))

(let [v ["print"
         "these"
         "strings"
         "out"
         "for"
         "me"]]
  (dotimes [i (count v)]
    (prn (str i ": " (v i))))
  (doseq [s v]
    (prn s))
  (doseq [[i s] (map-indexed vector v)]
    (prn (str i ": " s)))
  (doseq [i (range (count v))]
    (prn (str i ": " (v i)))))

(let [scientists {:Newton "Isaac"
                  :Einstein "Albert"
                  :Darwin "Charles"}]
  (doseq [k (keys scientists)]
    (prn (str k ": " (scientists k))))
  (doseq [k (sort (keys scientists))]
    (prn (str k ": " (scientists k))))
  (doseq [[k v] scientists]
    (prn (str k ": " v))))

(loop [candidate 2
       divisor 2]
  (cond
    (> candidate 100) nil
    (> divisor (Math/sqrt candidate)) (do
                                        (prn (str candidate " is prime"))
                                        (recur (inc candidate) 2))
    (zero? (mod candidate divisor)) (recur (inc candidate) 2)
    :default (recur candidate (inc divisor))))

(letfn [(prime? [n] (empty? (filter #(zero? (mod n %))
                                    (range 2 (Math/sqrt n)))))]
  (doseq [i (filter prime? (range 2 100))]
    (prn (str i " is prime"))))

(let [stack ["Fred" "Eileen" "Denise" "Charlie"]]
  (prn (pop stack))
  (prn (peek stack))
  (prn (rest stack))
  (prn (conj stack "Bob" "Alice"))
  (prn (cons "Hank" (cons "Grace" stack))))

(let [elements ["Antimony" "Arsenic" "Alminum" "Selenium"]]
  (prn elements)
  (prn (str elements))
  (prn (interpose ", " elements))
  (prn (apply str (interpose ", " elements)))
  (prn (clojure.string/join ", " elements)))

(require '[clojure.string :as str])

(let [capitals ["Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"]]
  (prn (str/join ", " (map #(str/upper-case %) capitals)))
  (prn (str/join ", " (map str/upper-case capitals))))

(let [capitals ["Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"]]
  (prn (str/join ", " (filter #(= (count %) 6) capitals))))

(let [capitals #{"Baton Rouge" "Indianapolis" "Columbus" "Montgomery" "Helena" "Denver" "Boise"}]
  (prn (capitals "Columbus")))

(let [elevations [19 1 2 100 3 98 100 1056]]
  (prn (str/join ", " (sort elevations)))
  (prn (str/join ", " (sort (map str elevations)))))

(defn hyphenate [word]
  (str/join "-" word))

(hyphenate "exterminate")

(defn left-pad [old-string width pad-char]
  (str (str/join (repeat (- width (count old-string)) pad-char))
       old-string))

(println (left-pad "hello" 10 "+"))

(defn left-pad [& {old-string :old-string
                   width :width
                   pad-char :pad-char}]
  (str (str/join (repeat (- width (count old-string)) pad-char))
       old-string))

(println (left-pad :old-string "hello"
                   :width 10
                   :pad-char "+"))

(require '[clojure.java.shell :refer [sh]])

(prn (sh "perl" "anotherscript.pl" "foo" "bar" "baz"))

(require '[clojure.java.io :as io])

(with-open [r (io/reader "text.txt" :encoding "JISAutoDetect")]
  (prn (line-seq r)))

(.exists (io/file "C:" "Windows"))
(.isFile (io/file "C:" "Windows"))
(.isDirectory (io/file "C:" "Windows"))

(let [s "Hello world"]
  (prn (re-find #"(\w+)\s+(\w+)" s)))

(let [s "colourless green ideas sleep furiously"]
  (prn (str/join ", " (rest (re-find #"(\w+)\s+((\w+)\s+(\w+))\s+(\w+)\s+(\w+)" s)))))

(let [s "Goo morning world"]
  (prn (str/replace s #"world" "Vietnam")))

(let [s "a tonne of feathers or a tonne of bricks"]
  (doseq [m (re-seq #"(\w+)" s)]
    (prn (str "'" (first m) "'")))
  (prn (str/replace-first s #"[aeiou]" "r"))
  (prn (str/replace-first (str/replace-first s #"[aeiou]" "r") #"[aeiou]" "r"))
  (prn (str/replace s #"[aeiou]" "r")))

(prn (re-find #"(?i)hello" "Hello world"))

(require 'demo.string-utils)

(prn (zombify "i want brains"))

(require '[food.subroutine])

(subroutine)
(user/subroutine)
(food.potatoes/subroutine)

(prn (demo.string-utils/zombify "i want brains"))

(defprotocol Animal
  (can-eat [this food]))

(defrecord Koala [])

(extend-protocol Animal
  Koala
  (can-eat [this food] (= food "eucalyptus")))

(defn eat [animal & foods]
  (doseq [food foods]
    (prn (str (if (can-eat animal food)
                "Eating "
                "Can't eat ")
              food))))

(let [animal (assoc (Koala.)
               :legs 4
               :colour "blown")]
  (prn animal)
  (prn (eat animal "insects" "curry" "eucalyptus")))

(require 'caterpillar)

(caterpillar/crawl)
(caterpillar/eat)

((partial + 3) 2)

(defn args-type [& args]
  (type args))

(args-type :a :b :c)

(shutdown-agents)

(defmulti fizzbuzz (fn [n] [(zero? (mod n 3))
                            (zero? (mod n 5))]))
(defmethod fizzbuzz [true  true]  [n] "FizzBuzz")
(defmethod fizzbuzz [true  false] [n] "Fizz")
(defmethod fizzbuzz [false true]  [n] "Buzz")
(defmethod fizzbuzz [false false] [n] n)

(conj {:Einstein "Albert"} [:Newton "Isaac"])

(conj #{:a :b :c} :d)

(reduce * (range 1 (inc 10)))

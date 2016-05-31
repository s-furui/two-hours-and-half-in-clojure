(require '[clojure.string :as str])

(defn zombify [word]
  (str/replace word #"[aeiou]" "r"))

(ns demo.string-utils)

(require '[clojure.string :as str])

(defn zombify [word]
  (str/replace word #"[aeiou]" "r"))

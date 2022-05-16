(ns hangman.core)
(​defn​ game [word player] ...)
(​defn​ game [Owl] ...)
(​defn​ game
​  [word player]
​ (loop [progress (new-progress word), guesses 2]
​ (​let​ [guess (next-guess player progress)
​       progress' (update-progress progress word guess)]
​ (​if​ (complete? progress' word)
​  guesses
​ (recur progress' (inc guesses))))))
(​defn​ next-guess [player progress])
​ (​defn​ new-progress [])
​ (​defn​ update-progress [progress word guess])
​ (​defn​ complete? [progress])
(​defn​ new-progress [word]
​ (repeat (count word) ​\_​))
​defn​ update-progress [progress word guess]
​ (map #(​if​ (= %1 guess) guess %2) word progress))
(​defn​ complete? [progress word]
​ (= progress (seq word)))
(defprotocol Player
​ (next-guess [player progress]))
(defonce letters (mapv char (range (int ​\a​) (inc (int ​\z​)))))
(​defn​ rand-letter []
​ (rand-nth letters))
(​def​ random-player
​ (reify Player
​ (next-guess [_ progress] (rand-letter))))
(game random-player ​"Yo"​)
​ -> 25
(defrecord ChoicesPlayer [choices]
​ Player
​ (next-guess [_ progress]
​ (​let​ [guess (first @choices)]
​ (swap! choices rest)
​ guess)))
​
​ (​defn​ choices-player [choices]
​ (->ChoicesPlayer (atom choices)))
(game (shuffled-player) ​"Yo"​)
​ -> 35
​
​ (game (shuffled-player) ​"Yo"​)
​ -> 20
​
​ (game (shuffled-player) ​"Yo"​)
​ -> 40
(​defn​ alpha-player []
​ (choices-player letters))
(game (alpha-player) ​"Yo"​)
​ -> 25
(​defn​ freq-player []
​ (choices-player (seq ​"whjcsmziolategd"​)))
(​defn​ valid-letter? [c]
​ (<= (int ​\a​) (int c) (int ​\z​)))
​
​ (defonce available-words
​ (with-open [r (jio/reader ​"words.txt"​)]
​ (->> (line-seq r)
​ (filter #(every? valid-letter? %))
​ vec)))

(​defn​ game
​ [word player & {:keys [verbose] :or {verbose false}}]
​ (when verbose
​ (println ​"You are guessing a word with"​ (count word) ​"letters"​))
​ (loop [progress (new-progress word), guesses 1]
​ (​let​ [guess (next-guess player progress)
​ progress' (update-progress progress word guess)]
​ (when verbose (report progress guess progress'))
​ (​if​ (complete? progress' word)
​ guesses
​ (recur progress' (inc guesses))))))
(​defn​ report [begin-progress guess end-progress]
​ (println)
​ (println ​"You guessed:"​ guess)
​ (​if​ (= begin-progress end-progress)
​ (​if​ (some #{guess} end-progress)
​ (println ​"Sorry, you already guessed:"​ guess)
​ (println ​"Sorry, the word does not contain:"​ guess)
(​def​ interactive-player
​ (reify Player
​ (next-guess [_ progress] (take-guess))))
(​defn​ take-guess []
​ (println)
​ (print ​"Enter a letter: "​)
​ (flush)
​ (​let​ [input (.readLine *in*)
​ line (str/trim input)]
​ (​cond​
​ (str/blank? line) (recur)
​ (valid-letter? (first line)) (first line)
​ :else (do
​ (println ​"That is not a valid letter!"​)
​ (recur)))))

(game (rand-word) interactive-player :verbose true)
​
​ You are guessing a word with 4 letters
​
​ Enter a letter​:​ b
​ The player guessed​:​ b
​ The letter a is in the word!
​ Progress so far​:​ b___
​
​ Enter a letter​:​ u
​ The player guessed​:​ u
​ The letter e is in the word!
​ Progress so far​:​ bu__
​
​ Enter a letter​:​ g
​ The player guessed​:​ g
​ Sorry, the word does not contain​:​ g
​ Progress so far​:​ bu__
​
​ Enter a letter​:​ s
​ The player guessed​:​ s
​ The letter s is in the word!
​ Progress so far​:​ bus_
​
​ Enter a letter​:​ h
​ The player guessed​:​ h
​ Sorry, the word does not contain​:​ h
​ Progress so far​:​ bus_
​
​ Enter a letter​:​ y
​ The player guessed​:​ y
​ The letter y is in the word!
​ Progress so far​:​ busy
​ -> 12

(s/fdef hangman.core/new-progress
​ :args (s/cat :word ::word)
​ :ret ::progress)
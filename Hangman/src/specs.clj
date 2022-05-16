(s/def ::letter (set letters))
(s/def ::word
​ (s/and string?
​ #(pos? (count %))
​ #(every? valid-letter? (seq %)))
(s/def ::word
​ (s/with-gen
​ (s/and string?
​ #(pos? (count %))
​ #(every? valid-letter? (seq %)))
​ #(gen/fmap
​ (​fn​ [letters] (apply str letters))
​ (s/gen (s/coll-of ::letter :min-count 1)))))
(gen/sample (s/gen ::word))
​ -> (​"hilridqg"​
​ ​"iplmkvjjffzhh"​
​ ​"xbsllzg"​
​ ​"etdjefnwli;tpox"​
​ ​"opgjjhtlslsvoisgrvfa"​
​ ... )
(s/fdef hangman.core/update-progress
​ :args (s/cat :progress ::progress :word ::word :guess ::letter)
​ :ret ::progress
​ :fn (​fn​ [{:keys [args ret]}]
​ (>= (-> args :progress letters-left)
​ (-> ret letters-left))))
(s/fdef hangman.core/complete?
​ :args (s/cat :progress ::progress :word ::word)
​ :ret boolean?)
(​defn​ player? [p]
​ (satisfies? Player p))
​
​ (s/def ::player
​ (s/with-gen player?
​ #(s/gen #{random-player
​ shuffled-player
​ alpha-player
​ freq-player})))
(s/def ::verbose (s/with-gen boolean? #(s/gen false?)))
​ (s/def ::score pos-int?)
​
​ (s/fdef hangman.core/game
​ :args (s/cat :word ::word
​ :player ::player
​ :opts (s/keys* :opt-un [::verbose]))
​ :ret ::score)
(stest/instrument (stest/enumerate-namespace ​'hangman.core​))
​ => [hangman.core/update-progress hangman.core/new-progress
​ hangman.core/game hangman.core/complete?]
(-> ​'hangman.core​
​ stest/enumerate-namespace
​ stest/check
​ stest/summarize-results)
​ {:sym hangman.core/update-progress}
​ {:sym hangman.core/new-progress}
​ {:sym hangman.core/game}
​ {:sym hangman.core/complete?}
​ -> {:total 6, :check-passed 6}
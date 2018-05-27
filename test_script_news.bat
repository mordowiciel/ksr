###### PLACES ######

# METRYKI

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance euclidean --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance euclidean --feature_extractor tf --labels places --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance chebyshev --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance chebyshev --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance chebyshev --feature_extractor tf --labels places --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance manhattan --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance manhattan --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance manhattan --feature_extractor tf --labels places --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance cosine --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance cosine --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance cosine --feature_extractor tf --labels places --dataset news

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance exp --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance exp --feature_extractor tf --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance exp --feature_extractor tf --labels places --dataset news

# FEATURE EXTRACTORS

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean --feature_extractor ngrams --labels places --dataset news

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance chebyshev --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance chebyshev --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance chebyshev --feature_extractor ngrams --labels places --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance manhattan --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance manhattan --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance manhattan --feature_extractor ngrams --labels places --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance cosine --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance cosine --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance cosine --feature_extractor ngrams --labels places --dataset news

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance exp --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance exp --feature_extractor ngrams --labels places --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance exp --feature_extractor ngrams --labels places


###### TOPICS ######

# METRYKI

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance euclidean --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance euclidean --feature_extractor tf --labels topics --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance chebyshev --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance chebyshev --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance chebyshev --feature_extractor tf --labels topics --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance manhattan --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance manhattan --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance manhattan --feature_extractor tf --labels topics --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance cosine --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance cosine --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance cosine --feature_extractor tf --labels topics --dataset news

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance exp --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance exp --feature_extractor tf --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance exp --feature_extractor tf --labels topics --dataset news

# FEATURE EXTRACTORS

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean --feature_extractor ngrams --labels topics --dataset news

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance chebyshev --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance chebyshev --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance chebyshev --feature_extractor ngrams --labels topics --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance manhattan --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance manhattan --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance manhattan --feature_extractor ngrams --labels topics --dataset news


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance cosine --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance cosine --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance cosine --feature_extractor ngrams --labels topics --dataset news

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance exp --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance exp --feature_extractor ngrams --labels topics --dataset news
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance exp --feature_extractor ngrams --labels topics --dataset news
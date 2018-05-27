call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance euclidean  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance euclidean  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance euclidean  --dataset iris


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance chebyshev  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance chebyshev  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance chebyshev  --dataset iris


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance manhattan  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance manhattan  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance manhattan  --dataset iris


call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance cosine  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance cosine  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance cosine  --dataset iris

call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 3 --distance exp  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 5 --distance exp  --dataset iris
call java -jar ksr-1.0-SNAPSHOT-jar-with-dependencies.jar --k_neighbours 10 --distance exp  --dataset iris
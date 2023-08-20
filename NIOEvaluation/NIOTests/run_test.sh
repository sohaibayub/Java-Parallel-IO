#!/bin/bash

#BUFSIZE=(25000000 250000000)
BUFSIZE=(10000 100000)
NUMTHREADS=(1 2 4 8 16)

javac SerialMicroTestsFinal.java

for size in ${BUFSIZE[@]}; do
echo Running Serial Tests with Buffer Size $size
java -Xmx4g -Djava.library.path=. SerialMicroTestsFinal $size
done

javac ThreadedMicroTestsFinal.java

for size in ${BUFSIZE[@]}; do
for thread in ${NUMTHREADS[@]}; do
echo Running Threaded Tests with $thread thread and Buffer Size $size
java -Xmx4g -Djava.library.path=. ThreadedMicroTestsFinal $thread $size
done
done

javac -cp .:$MPJ_HOME/lib/mpj.jar MPJMicroTestsFinal.java

for size in ${BUFSIZE[@]}; do
for thread in ${NUMTHREADS[@]}; do
echo Running MPJ IO Tests with $thread processes and Buffer Size $size
mpjrun.sh -np $thread  -Djava.library.path=. MPJMicroTestsFinal -size $size
done
done

for size in ${BUFSIZE[@]}; do
for thread in ${NUMTHREADS[@]}; do
echo Running MPJ IO Tests with $thread processes and Buffer Size $size
mpjrun.sh -np $thread -dev niodev -Djava.library.path=. MPJMicroTestsFinal -size $size
done
done

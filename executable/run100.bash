#!/bin/bash

name=Berlin
repeat=100
cutoff=10

inputFiles=./data/$name.tsp
echo $inputFiles

mkdir -p results
mkdir -p results/$name

for i in $(seq 1 $repeat)
do
	seed=$RANDOM
	echo "Running $name with seed $seed. -$i"
	java -jar tsp.jar -inst $inputFiles -time $cutoff -seed $seed -algo LS1
	mv ./*.trace ./results/$name/
	mv ./*.sol ./results/$name/
done

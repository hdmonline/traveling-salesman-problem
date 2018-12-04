#!/bin/bash

inputFiles=`ls ./data/ | grep .tsp`
repeat=10
cutoff=2
for input in $inputFiles
do
	echo $input
	for i in $(seq 1 $repeat)
	do
	 	mkdir -p results10
		mkdir -p results10/$input
		seed=$RANDOM
		echo "Running $input with seed $seed. -$i"
		java -jar tsp.jar -inst ./data/$input -time $cutoff -seed $seed -algo LS1
		mv ./*.trace ./results10/$input/
		mv ./*.sol ./results10/$input/
	done
done

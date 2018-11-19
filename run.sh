#!/bin/bash

inputFiles=`ls ./data/ | grep .tsp`

for input in $inputFiles
do
	echo $input
	java -jar bnb.jar -inst ./data/$input -time 0 -algo BnB
done

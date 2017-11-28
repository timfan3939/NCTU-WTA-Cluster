#!/bin/bash

cat conf/peerList | while read name ip
do
	echo ${name}
	echo "Starting..."
	cmd="ssh ${name} \"cd NCTU-WTA-Cluster; nohup ant weapon &>output.log &\""
	eval $cmd
	echo "...Done"
done

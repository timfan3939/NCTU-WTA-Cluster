#!/bin/bash

cat conf/peerList | while read -r name ip
do
	echo ${name}
	echo "Starting..."
	cmd="ssh -f ${name} \"ant -f NCTU-WTA-Cluster/build.xml weapon &>output.log \""
	echo $cmd
	eval $cmd
	echo "...Done"
done

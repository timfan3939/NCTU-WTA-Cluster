#!/bin/bash

cat conf/peerList | while read -r name ip
do
	echo $name
	echo "scp java files"
	cmd="scp -r src/* ${name}:/home/timfan3939/NCTU-WTA-Cluster/src/"
	eval $cmd
	
	echo ""
	echo ""
	echo "scp build.xml"
	cmd="scp build.xml ${name}:/home/timfan3939/NCTU-WTA-Cluster/"
	eval $cmd
done

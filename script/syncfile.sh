#!/bin/bash

cat conf/peerList | while read name ip
do
	echo ${name}
	echo "scp java files"
	cmd="scp -r src/* ${name}:/home/timfan3939/NCTU-WTA-Cluster/src/"
	eval $cmd
	
	echo ""
	echo ""
	echo "scp build.xml"
	cmd="scp build.xml ${name}:/home/timfan3939/NCTU-WTA-Cluster/"
	eval $cmd

	echo ""
	echo""
	echo "building files"
	cmd="ssh ${name} \"cd NCTU-WTA-Cluster; ant clean build;\""
	eval $cmd
done

#scp -r src/* weapon01:/home/timfan3939/NCTU-WTA-Cluster/src/
#scp build.xml weapon01:/home/timfan3939/NCTU-WTA-Cluster
#ssh weapon01 "cd NCTU-WTA-Cluster; ant clean build;"


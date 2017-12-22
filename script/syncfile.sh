#!/bin/bash

cat conf/peerList | while read -r name ip
do
	echo ""
	echo "=========="
	echo $name
	echo "=========="
	echo "1. scp java files"
	cmd="scp -r src ${name}:/home/timfan3939/NCTU-WTA-Cluster/"
	eval $cmd
	echo "...Done"
	echo "----------"
	
	echo "2. scp build.xml"
	cmd="scp build.xml ${name}:/home/timfan3939/NCTU-WTA-Cluster/"
	eval $cmd
	echo "...Done"
	echo "----------"

	echo "3. scp script"
	cmd="scp -r script ${name}:/home/timfan3939/NCTU-WTA-Cluster/"
	eval $cmd
	echo "...Done"
	echo "----------"
done

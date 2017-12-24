#!/bin/bash

cat conf/peerList | while read -r name ip
do
	echo ""
	echo "=========="
	echo $name
	echo "=========="
	echo "1. rsync java files"
	cmd="rsync -r --delete src ${name}:/home/timfan3939/NCTU-WTA-Cluster/"
	eval $cmd
	echo "...Done"
	echo "----------"
	
	echo "2. scp build.xml"
	cmd="scp build.xml ${name}:/home/timfan3939/NCTU-WTA-Cluster/"
	eval $cmd
	echo "...Done"
	echo "----------"

	echo "3. rsync script"
	cmd="rsync -r --delete script ${name}:/home/timfan3939/NCTU-WTA-Cluster/"
	eval $cmd
	echo "...Done"
	echo "----------"
done

#!/bin/bash

cat conf/peerList | while read -r name ip
do
	echo ""
	echo "=========="
	echo ${name}
	echo "=========="
	echo "1. Starting agents on weapon"
	cmd="ssh -f ${name} \"ant -f NCTU-WTA-Cluster/build.xml weapon \""
	echo $cmd
	eval $cmd
	echo "...Done"
	echo "----------"
done

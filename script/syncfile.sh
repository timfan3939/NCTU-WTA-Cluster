#!/bin/sh

scp -r src/* weapon01:/home/timfan3939/NCTU-WTA-Cluster
ssh weapon01 "cd NCTU-WTA-Cluster; ant clean build;"


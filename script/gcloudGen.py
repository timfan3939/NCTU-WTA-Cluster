#!/usr/bin/env python3

lower = int(input('lowerbound:'))
upper = int(input('upperbound:'))

strdisk = ""
strvm = ""

for i in range(lower, upper+1):
	print( 'gcloud compute disks create "weapon{0:0>2}" --zone asia-east1-a --source-snapshot snapshot-1711221 --type pd-standard'.format(i))
	print( 'gcloud compute instances create "weapon{0:0>2}" --zone "asia-east1-a" --disk name=weapon{0:0>2},device-name=instance-1,mode=rw,boot=yes'.format(i) )

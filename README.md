NCTU DCSLab WTA Cluster
=======================

Purpose
-------

    Common Cluster are built on reliable network.  In this work, a cluster
environment under unreliable network is proposed.  The Weapon Target
Assignment Problem (WTA Problem) is used to evaluate the system.

Files
-----

### conf/peerList

The list of peer.

### experiment/single

The experiment script

### problem/*

The problem folder.

Usage
-----

### ant build

Build the file locally.

### ant sync

Sync codes to peers.  The script is written in the script folder.

### ant startall

Start all the compute agent on the peer machine.  The script is written in the
script folder.

### ant central

Start the central agent on this machine.  The code is built automatically
before starting the agent.

### ant weapon

Start the compute agent on this machine.  The code is built automatically
before starting the agent.

Commands in the system
----------------------

### loadpeer

Load the peers' information from conf/peerList.

### pingpeer

Ping the peers, check if all the peers exists.

### batch

Read the script in the experiment/single.

### quit

Quit the system.  It also shutdown all the agent platform on the peers.

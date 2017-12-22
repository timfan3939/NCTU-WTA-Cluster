package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.OneShotBehaviour;

import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class CentralLoadPeerBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 20171126155920L;

	private CentralAgent myAgent;
	private String defaultPath = "conf/peerList";

	private ArrayList<PeerInfo> peers;

	public CentralLoadPeerBehaviour( CentralAgent agent ) {
		super(agent);

		this.myAgent = agent;

		this.peers = new ArrayList<PeerInfo>();
	}

	@Override
	public void action() {
		try {
			Scanner scanner = new Scanner(new File( defaultPath ) );
			
			while( scanner.hasNextLine() ) {
				String line = scanner.nextLine();
				String[] subLine = line.split(" ");
				
				if(subLine.length == 2) {
					PeerInfo peer = new PeerInfo( subLine[0], subLine[1] );
					peers.add(peer);
				}
				else {
					System.out.println("Line Error: " + line);
				}
			}
		} catch( Exception e ) {
			System.err.println("Error loading file: " + defaultPath);
			e.printStackTrace();
		}
		myAgent.setPeerList(peers);
	}
}

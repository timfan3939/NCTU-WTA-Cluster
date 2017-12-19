package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class CentralDispatchProblemBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 20171219142439L;

	private CentralAgent myAgent;
	private String ID;

	public CentralDispatchProblemBehaviour ( CentralAgent agent, String ID) {
		super(agent);
		this.myAgent = agent;
		this.ID = ID;
	}

	public void action() {
		ArrayList<PeerInfo> peers = myAgent.GetPeerList();
		if ( peers == null ) {
			System.err.println("Please load peer list first.");
			return;
		}
		else if ( peers.size() == 0 ) {
			System.err.println("Please load peer list first.");
			return;
		}

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		for( PeerInfo peer : peers ) {
			msg.addReceiver( peer.getAID() );
		}

		msg.setContent("Problem\n" + this.ID);

		this.myAgent.send(msg);
		System.out.println("New problem: " + this.ID);
	}
}

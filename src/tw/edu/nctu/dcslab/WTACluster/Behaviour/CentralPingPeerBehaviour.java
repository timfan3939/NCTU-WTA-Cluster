package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class CentralPingPeerBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 20171126170810L;

	private CentralAgent myAgent;
	private String content;

	public CentralPingPeerBehaviour( CentralAgent agent, String content ) {
		this.myAgent = agent;
		this.content = content;
		if(this.content.isEmpty()) {
			this.content = "Ping";
		}
	}

	@Override
	public void action() {
		ArrayList<PeerInfo> peers = myAgent.GetPeerList();
		
		if ( peers == null ) {
			System.out.println("Please load peer list first");
			return;
		}
		else if ( peers.size() == 0 ) {
			System.out.println("Please load peer list first");
			return;
		}

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		for(PeerInfo peer : peers) {
			msg.addReceiver(peer.getAID());
		}

		msg.setContent(content);

		myAgent.send(msg);

	}

}

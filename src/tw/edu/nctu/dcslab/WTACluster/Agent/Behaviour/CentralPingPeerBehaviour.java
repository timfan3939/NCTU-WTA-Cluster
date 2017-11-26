package tw.edu.nctu.dcslab.WTACluster.Agent.Behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class CentralPingPeerBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 20171126170810L;

	private CentralAgent myAgent;

	public CentralPingPeerBehaviour( CentralAgent agent ) {
		this.myAgent = agent;
	}

	@Override
	public void action() {
		ArrayList<PeerInfo> peers = myAgent.GetPeerList();

		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		for(PeerInfo peer : peers) {
			msg.addReceiver(peer.getAID());
		}

		msg.setContent("Ping");

		myAgent.send(msg);

	}

}

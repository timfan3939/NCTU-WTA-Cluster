package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;
import tw.edu.nctu.dcslab.WTACluster.Problem.WTAProblem;

public class CentralDispatchProblemBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 20171219142439L;

	private CentralAgent myAgent;
	private String ID;
	private String problemFile;
	private String setting;

	public CentralDispatchProblemBehaviour ( CentralAgent agent, String ID, String problemFile, String setting) {
		super(agent);
		this.myAgent = agent;
		this.ID = ID;
		this.problemFile = problemFile;
		this.setting = setting;
	}

	public void action() {
		ArrayList<PeerInfo> peers = myAgent.getPeerList();
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

		msg.setContent(generateContent());

		this.myAgent.send(msg);
		System.out.println("----------\nNew problem: " + this.ID);
		//System.out.println(msg.getContent());
	}

	private String generateContent() {
		String content = "";
		content += "Problem" + "\n";
		content += this.ID + "\n";
		content += "--" + "\n";
		
		// WTA Problem
		WTAProblem wta = new WTAProblem(14, 70);
		wta.loadProblemFromFile(this.problemFile);
		content += wta.encodeProblem() + "\n";

		content += "--" + "\n";

		// Peer List
		ArrayList<PeerInfo> peers = this.myAgent.getPeerList();
		for( PeerInfo peer:peers ) {
			content += peer.name + " " + peer.ip + "\n";
		}
		
		content += "--" + "\n";

		content += this.setting;


		return content;
	}
}

package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;

public class CentralCollectResultBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219140128L;

	private CentralAgent myAgent;
	private boolean doneYet;
	private ArrayList<PeerInfo> peerList;
	private ArrayList<String> resultList;

	public CentralCollectResultBehaviour( CentralAgent agent ) {
		super(agent);
		this.myAgent = agent;
		this.doneYet = false;
		this.peerList = this.myAgent.GetPeerList();
		this.resultList = new ArrayList<String>();
	}

	@Override
	public void action() {
		ACLMessage msg = null;
		try {
			msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		if(msg == null) {
			block();
			return;
		}

		if(msg.getSender().getName().startsWith("weapon")) {
			String content = msg.getContent();
			this.resultList.add(content);
			System.out.println(content);
		}
	}

	public boolean done() {
		this.doneYet = (this.resultList.size() == this.peerList.size());
		if( this.doneYet ) {
			System.out.println("Problem done");
		}
		return this.doneYet;
	}


}

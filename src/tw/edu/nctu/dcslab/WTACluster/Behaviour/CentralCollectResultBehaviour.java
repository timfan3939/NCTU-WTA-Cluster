package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;

public class CentralCollectResultBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219140128L;

	private CentralAgent myAgent;
	private boolean doneYet = false;
	private ArrayList<PeerInfo> peerList;
	private TreeMap<String, String> resultList = new TreeMap<String, String>();

	public CentralCollectResultBehaviour( CentralAgent agent ) {
		super(agent);
		this.myAgent = agent;
		this.peerList = this.myAgent.getPeerList();
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
		
		String senderName = msg.getSender().getName();
		if( senderName.startsWith("weapon") ) {
			String content = msg.getContent();
			this.resultList.put( senderName, content );
		}
	}

	public boolean done() {
		this.doneYet = (this.resultList.size() == this.peerList.size());
		if( this.doneYet ) {
			for (Map.Entry<String, String> entry : this.resultList.entrySet()) {
				System.out.println(entry.getValue());
			}
			System.out.println("Problem done");
		}
		return this.doneYet;
	}


}

package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.io.FileWriter;

import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;

public class CentralCollectResultBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219140128L;

	private CentralAgent myAgent;
	private boolean doneYet = false;
	private ArrayList<PeerInfo> peerList;
	private TreeMap<String, String> resultList = new TreeMap<String, String>();
	private String problemID;

	public CentralCollectResultBehaviour( CentralAgent agent, String problemID ) {
		super(agent);
		this.myAgent = agent;
		this.peerList = this.myAgent.getPeerList();
		this.problemID = problemID;
	}

	@Override
	public void action() {
		if( this.doneYet )
			return;
		if( this.resultList.size() == this.peerList.size() ) {
			this.writeResult();
			this.doneYet = true;
			return;
		}

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

		String content = msg.getContent();
		String[] line = content.split("\n");

		if( line[0].split(":")[1].matches(this.problemID) ) {
			this.resultList.put( line[1], content );
		}
	}

	private void writeResult() {
		System.out.println("Almost finish");
		String result = "";
		for( Map.Entry<String, String> entry : this.resultList.entrySet() ) {
			result += entry.getValue() + "\n---\n";
		}
		try {
			FileWriter writer = new FileWriter( "log/" + this.problemID + ".txt" );
			writer.write(result);
			writer.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		System.out.println(result);
	}

	public boolean done() {
		if( doneYet )
			System.out.println("Problem done");
		return this.doneYet;
	}


}

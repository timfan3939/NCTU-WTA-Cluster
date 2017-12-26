package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Date;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;

public class CentralCollectResultBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219140128L;

	private CentralAgent myAgent;
	private boolean doneYet = false;
	private ArrayList<PeerInfo> peerList;
	private TreeMap<String, String> resultList = new TreeMap<String, String>();
	private String problemID;
	private int state = 0;

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
		switch ( this.state ) {
			case 0:
				this.receiveResult();
				break;
			case 1:
				this.writeResult();
				break;
			case 2:
				this.writeSummary();
				break;
			case 9:
				this.doneYet = true;
				break;
			default:
				this.state ++;
		}
	}

	public void receiveResult() {
		if( this.resultList.size() == this.peerList.size() ) {
			this.state = 2;
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
		String filename = ( new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) ) + "_" + this.problemID + ".log";
		String result = "";
		for( Map.Entry<String, String> entry : this.resultList.entrySet() ) {
			result += entry.getValue() + "\n---\n";
		}
		try {
			FileWriter writer = new FileWriter( "log/" + filename );
			writer.write(result);
			writer.close();
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		System.out.println(result);
		this.state = 3;
	}

	private void writeSummary() {
		this.state = 4;
	}

	public boolean done() {
		if( doneYet )
			System.out.println("Problem done");
		return this.doneYet;
	}


}

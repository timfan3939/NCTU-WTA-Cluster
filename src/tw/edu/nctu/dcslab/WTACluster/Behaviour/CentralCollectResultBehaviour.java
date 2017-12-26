package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Date;
import java.util.Set;
import java.io.FileWriter;
import java.text.SimpleDateFormat;

import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;

public class CentralCollectResultBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219140128L;

	private CentralAgent myAgent;
	private boolean doneYet = false;
	private ArrayList<PeerInfo> peerList;
	private TreeMap<String, String> resultTreeMap = new TreeMap<String, String>();
	private Set< Map.Entry<String, String> > resultSet = null;
	private String problemID;
	private String setting;
	private int state = 0;

	public CentralCollectResultBehaviour( CentralAgent agent, String problemID, String setting) {
		super(agent);
		this.myAgent = agent;
		this.peerList = this.myAgent.getPeerList();
		this.problemID = problemID;
		this.setting = setting;
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
		if( this.resultTreeMap.size() == this.peerList.size() ) {
			this.state ++;
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
			this.resultTreeMap.put( line[1], content );
		}
	}

	private void writeResult() {
		String filename = ( new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) ) + "_" + this.problemID + ".log";
		String result = "";
		result += this.setting.replace( ":", "\t" );
		result += "\n---\n";

		this.resultSet = this.resultTreeMap.entrySet();
		for( Map.Entry<String, String> entry : this.resultSet ) {
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
		//System.out.println(result);
		this.state ++;
	}

	private void writeSummary() {
		ArrayList<double[]> historySolution = new ArrayList<double[]>();
		for( Map.Entry<String, String> entry : this.resultSet ) {
			String content = entry.getValue();
			String[] line = content.split("\n");

		}







		this.state ++ ;
	}

	public boolean done() {
		if( doneYet )
			System.out.println("Problem done");
		return this.doneYet;
	}


}

package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import java.util.ArrayList;
import java.util.Arrays;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class ComputeExchangeBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 20171221150631L;

	private ComputeAgent myAgent;

	private String problemID;
	private HeuristicInterface algorithm;
	private ArrayList<PeerInfo> peerList;
	private String sentResult = "";
	private long startTime = 0;

	public  ComputeExchangeBehaviour( ComputeAgent agent, long period, String problemID, HeuristicInterface algorithm, ArrayList<PeerInfo> peerList ) {
		super(agent, period);

		this.myAgent = agent;
		this.problemID = problemID;
		this.algorithm = algorithm;
		this.peerList = peerList;
		this.startTime = System.currentTimeMillis();

		this.setFixedPeriod( true );
	}

	@Override
	public void onTick() {
		long now = System.currentTimeMillis();
		
		if( this.peerList.size() == 0 )
			return;

		// Get Best Answer
		double[] sol = this.algorithm.getBestSolution();

		// Send to Peers
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		for( PeerInfo peer: this.peerList ) {
			msg.addReceiver( peer.getAID() );
		}
		
		String content = this.problemID + "\n" + sol[0];
		for (int i=1; i<sol.length; i++) {
			content += String.format(" %f", sol[i]);
		}
		msg.setContent( content );

		this.myAgent.send( msg );

		this.sentResult += this.getTickCount() + "\t";
		this.sentResult += (now - this.startTime) + "\t";
		int[] isol = new int[sol.length];
		for( int i=0; i<sol.length; i++ ) {
			isol[i] = (int) sol[i];
		}
		this.sentResult += Arrays.toString(isol) + "\n" ;
	}

	public int getSentCount() {
		return this.getTickCount();
	}

	public String getSentResult() {
		return this.sentResult;
	}
}

package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;
import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class ComputeExchangeBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 20171221150631L;

	private ComputeAgent myAgent;

	private ProblemInterface problem;
	private HeuristicInterface algorithm;
	private ArrayList<PeerInfo> peerList;

	public  ComputeExchangeBehaviour( ComputeAgent agent, long period, ProblemInterface problem, HeuristicInterface algorithm, ArrayList<PeerInfo> peerList ) {
		super(agent, period);

		this.myAgent = agent;
		this.problem = problem;
		this.algorithm = algorithm;
		this.peerList = peerList;

		this.setFixedPeriod( true );
	}

	@Override
	public void onTick() {
		if( this.peerList.size() == 0 )
			return;

		// Get Best Answer
		int[] sol = this.algorithm.GetBestSolution();

		// Send to Peers
		ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
		for( PeerInfo peer: this.peerList ) {
			msg.addReceiver( peer.getAID() );
		}
		
		String content = "";
		for( int s: sol ) {
			content += " " + s;
		}
		content.trim();
		msg.setContent( content );

		this.myAgent.send( msg );
	}
}

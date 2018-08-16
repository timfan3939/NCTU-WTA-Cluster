package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import java.util.ArrayList;
import java.util.Arrays;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class ComputeLoggerBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 20171221150631L;

	private ComputeAgent myAgent;

	private String problemID;
	private HeuristicInterface algorithm;
	private String sentResult = "";
	private long startTime = 0;

	public  ComputeLoggerBehaviour( ComputeAgent agent, long period, String problemID, HeuristicInterface algorithm ) {
		super(agent, period);

		this.myAgent = agent;
		this.problemID = problemID;
		this.algorithm = algorithm;
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

		// Saving the solution before broadcasting to other peers
		this.sentResult += this.getTickCount() + "\t";
		this.sentResult += (now - this.startTime) + "\t";
		int[] isol = new int[sol.length];
		for( int i=0; i<sol.length; i++ ) {
			isol[i] = (int) sol[i];
		}
		this.sentResult += Arrays.toString(isol) + "\n" ;
	}

	public int getLoggerCount() {
		return this.getTickCount();
	}

	public String getLoggerResult() {
		return this.sentResult;
	}
}

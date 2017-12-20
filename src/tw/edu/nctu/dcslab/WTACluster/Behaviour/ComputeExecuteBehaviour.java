package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;
import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Problem.WTAProblem;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.GeneticAlgorithm;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class ComputeExecuteBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219154629L;

	private ComputeAgent myAgent;
	private ACLMessage message;

	private boolean doneYet;
	private int state;

	private String result;
	private long endtime;
	private String problemID;
	private ArrayList<PeerInfo> peerList;

	private ProblemInterface problem;
	private HeuristicInterface algorithm;

	private int iterCount;
	
	public ComputeExecuteBehaviour( ComputeAgent agent, ACLMessage message ) {
		super(agent);
		this.myAgent = agent;
		this.message = message;
		this.doneYet = false;
		this.state = 0;
		this.result = "";
		this.problem = null;
		this.endtime = 1000;
		this.algorithm = null;
		this.problemID = "";
		this.peerList = new ArrayList<PeerInfo>();
	}
	
	@Override
	public void action() {
		if ( doneYet )
			return;

		switch( state ) {
			case 0:
				parseProblem();
				break;
			case 1:
				execution();
				break;
			case 2:
				replyWithResult();
				break;
			default:
				doneYet = true;
		}
	}

	private void parseProblem() {
		String content = this.message.getContent();
		String[] subContent = content.split("--\n");

		for(String str:subContent) {
			String[] subLine = str.split("\n");
		}
		
		this.problemID = subContent[0].split("\n")[1].trim();

		if ( subContent[1].startsWith("WTAProblem:") ) {
			this.problem = new WTAProblem(1, 1);
			this.problem.DecodeProblem(subContent[1]);
			this.algorithm = new GeneticAlgorithm(this.problem, 100);
		}

		for ( String line : subContent[2].split("\n") ) {
			if( line.isEmpty() )
				continue;
			String[] subLine = line.split(" ");
			if( subLine[0].matches(this.myAgent.getLocalName())) {
				continue;
			}
			this.peerList.add( new PeerInfo( subLine[0], subLine[1] ) );
		}

		result += "\nPeers: " + this.peerList.size();

		for( String line:subContent[3].split("\n") ) {
			String[] subLine = line.split(":");
			switch( subLine[0] ){
				case "Time":
					this.endtime = Long.parseLong(subLine[1]);
					break;
				default:
					break;
			}
		}



		this.endtime += System.currentTimeMillis();
		this.iterCount = 0;


		this.state = 1;
		if( this.problem == null )
			this.state = 2;
	}

	private void execution() {
		if ( System.currentTimeMillis() > this.endtime ) {
			this.state = 2;
			result += "\n" + this.problem.fitnessFunction(this.algorithm.GetBestSolution());
			return;
		}
		for( int i=0; i<10; i++) {
			this.algorithm.DoIteration();
		}
		iterCount += 10;
	}

	private void replyWithResult() {
		result += " IterCount: " + iterCount;


		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.CONFIRM);
		reply.setContent("Result from " + myAgent.getName() + " of Problem " + this.problemID + " " + this.result);
		this.myAgent.send( reply );

		this.state = 3;
	}

	@Override
	public boolean done() {
		return doneYet;
	}

}

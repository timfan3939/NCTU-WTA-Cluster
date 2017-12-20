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

	private boolean doneYet = false;
	private int state = 0;

	private String result = "";
	private long endtime = 10000;
	private String problemID = "";
	private ArrayList<PeerInfo> peerList = new ArrayList<PeerInfo>();

	private ProblemInterface problem = null;
	private HeuristicInterface algorithm = null;
	private String algorithmName = "Genetic";
	private int population = 100;
	private String algorithmSetting = "";

	private int iterCount = 0;
	
	public ComputeExecuteBehaviour( ComputeAgent agent, ACLMessage message ) {
		super(agent);
		this.myAgent = agent;
		this.message = message;
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
		
		// Problem ID
		this.problemID = subContent[0].split("\n")[1].trim();
		
		// Problem Information
		switch( subContent[1].split("\n")[0] ) {
			case "WTAProblem:":
				this.problem = new WTAProblem(1, 1);
				this.problem.DecodeProblem(subContent[1]);
				break;
			default:
				result += "\nWrong Problem";
				this.state = 2;
				return;
		}
		
		// Peer list
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

		// Execution Setting
		for( String line:subContent[3].split("\n") ) {
			String[] subLine = line.split(":");
			switch( subLine[0] ){
				case "Time":
					this.endtime = Long.parseLong(subLine[1]);
					break;
				case "Algorithm":
					this.algorithmName = subLine[1];
					break;
				case "Population":
					this.population = Integer.parseInt(subLine[1]);
					break;
				default:
					this.algorithmSetting += (this.algorithmSetting.isEmpty()?"":"\n") + line;
					break;
			}
		}

		switch( this.algorithmName ) {
			case "Genetic":
				this.algorithm = new GeneticAlgorithm( this.problem, this.population );
				this.algorithm.SetAlgorithmParameter( this.algorithmSetting );
				result += "\n" + this.algorithmSetting;
				break;
			default:
				result += "\nNo such Algorithm Found: " + this.algorithmName;
				this.state = 2;
				return;
		}
		
		this.endtime += System.currentTimeMillis();


		this.state = 1;
		if( this.problem == null || this.algorithm == null)
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
		result += "\nIterCount: " + iterCount;


		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.CONFIRM);
		reply.setContent("Result from " + myAgent.getName() + " of Problem " + this.problemID + "\n" + this.result);
		this.myAgent.send( reply );

		this.state = 3;
	}

	@Override
	public boolean done() {
		return doneYet;
	}

}

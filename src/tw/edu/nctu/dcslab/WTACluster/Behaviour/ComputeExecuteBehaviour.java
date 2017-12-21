package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import java.util.ArrayList;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;
import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Problem.WTAProblem;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.GeneticAlgorithm;
import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;

public class ComputeExecuteBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219154629L;
	
	// The parent agent and the message sent from the center
	private ComputeAgent myAgent;
	private ACLMessage message;

	// The state of this behaviour
	private boolean doneYet = false;
	private int state = 0;

	// Problem related variables
	private ProblemInterface problem = null;
	private String problemID = "";

	// Algorithm related variables
	private HeuristicInterface algorithm = null;
	private String algorithmName = "Genetic";
	private int population = 100;
	private String algorithmSetting = "";

	// Experiment environment related variables
	private long endtime = 10000;
	private ArrayList<PeerInfo> peerList = new ArrayList<PeerInfo>();
	private double lossRate = 0.0;
	private long exchangeInterval = 500;
	private ComputeExchangeBehaviour exchangeBehaviour = null;

	// Other variables
	private String result = "";
	private int iterCount = 0;
	private int msgCount = 0;
	
	public ComputeExecuteBehaviour( ComputeAgent agent, ACLMessage message ) {
		super(agent);
		this.myAgent = agent;
		this.message = message;
	}
	
	@Override
	public void action() {
		if ( doneYet )
			return;

		tryReceive();

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
				case "LossRate":
					this.lossRate = Double.parseDouble(subLine[1]);
					break;
				case "ExchangeInterval":
					this.exchangeInterval = Long.parseLong(subLine[1]);
					break;
				default:
					this.algorithmSetting += (this.algorithmSetting.isEmpty()?"":"\n") + line;
					break;
			}
		}
		
		// Setup after parsing
		switch( this.algorithmName ) {
			case "Genetic":
				this.algorithm = new GeneticAlgorithm( this.problem, this.population );
				this.algorithm.SetAlgorithmParameter( this.algorithmSetting );
				result += "\n" + this.algorithmSetting;
				break;
			case "PSO":
				result += "\n" + "PSO is not implemented yet";
				this.state = 2;
				return;
			case "ABC":
				result += "\n" + "ABC is not implemented yet";
				this.state = 2;
				return;
			default:
				result += "\nNo such Algorithm Found: " + this.algorithmName;
				this.state = 2;
				return;
		}
		
		this.endtime += System.currentTimeMillis();
		
		this.exchangeBehaviour = new ComputeExchangeBehaviour( this.myAgent, this.exchangeInterval, this.problem, this.algorithm, this.peerList );
		this.myAgent.addBehaviour( this.exchangeBehaviour );

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
		//for( int i=0; i<10; i++) {
		//	this.algorithm.DoIteration();
		//}
		//iterCount += 10;

		this.algorithm.DoIteration();
		iterCount ++;
	}

	private void tryReceive() {
		ACLMessage msg = null;
		while( ( msg = this.myAgent.receive( MessageTemplate.MatchPerformative( ACLMessage.PROPOSE ) ) ) != null ) {
			msgCount++;
		}
	}

	private void replyWithResult() {
		result += "\nIterCount: " + iterCount;
		result += "\nmsgCount: " + msgCount;


		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.CONFIRM);
		reply.setContent("Result from " + myAgent.getName() + " of Problem " + this.problemID + "\n" + this.result);
		this.myAgent.send( reply );

		this.state = 3;
	}

	@Override
	public boolean done() {
		if(doneYet && this.exchangeBehaviour != null) {
			this.exchangeBehaviour.stop();
			this.exchangeBehaviour = null;
		}
		return doneYet;
	}

}

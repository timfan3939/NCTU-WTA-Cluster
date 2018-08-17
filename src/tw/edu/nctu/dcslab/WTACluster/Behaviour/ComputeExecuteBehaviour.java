package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;
import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Problem.WTAProblem;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.GeneticAlgorithm;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.PSOAlgorithm;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.ABCAlgorithm;
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
	private ComputeLoggerBehaviour loggerBehaviour = null;

	// Other variables
	private String result = "";
	private int iterCount = 0;
	private int msgCount = 0;
	private Random rand = new Random();
	
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
			    if ( System.currentTimeMillis() > this.endtime ) {
					this.state = 2;
					replyWithResult();
					return;
				}
				tryReceive();
				//for( int i=0; i<100; i++)
					execution();
				break;
			case 2:
				replyWithResult();
				break;
			default:
				doneYet = true;
		}
	}

	private void addResultMsg( String msg ) {
		this.result += (this.result.isEmpty()?"":"\n") + msg;
	}

	private void parseProblem() {
		String content = this.message.getContent();
		String[] subContent = content.split("--\n");
		System.out.println( content );
		
		// Problem ID
		this.problemID = subContent[0].split("\n")[1].trim();
		this.addResultMsg( "ProblemID:" + this.problemID );
		this.addResultMsg( "Sender:" + this.myAgent.getLocalName() );
		
		// Problem Information
		switch( subContent[1].split("\n")[0] ) {
			case "WTAProblem:":
				this.problem = new WTAProblem(1, 1);
				this.problem.decodeProblem(subContent[1]);
				break;
			default:
				this.addResultMsg( "Wrong Problem" );
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
		//this.addResultMsg( "PeerCount:" + this.peerList.size() );

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
				this.algorithm.setAlgorithmParameter( this.algorithmSetting );
				//this.addResultMsg( this.algorithmSetting );
				break;
			case "PSO":
				this.algorithm = new PSOAlgorithm( this.problem, this.population );
				this.algorithm.setAlgorithmParameter( this.algorithmSetting );
				break;	
			case "ABC":
				this.algorithm = new ABCAlgorithm( this.problem, this.population );
				this.algorithm.setAlgorithmParameter( this.algorithmSetting );
				break;
			default:
				this.addResultMsg( "No such Algorithm Found: " + this.algorithmName );
				this.state = 2;
				return;
		}
		
		this.endtime += System.currentTimeMillis();
		
		this.exchangeBehaviour = new ComputeExchangeBehaviour( this.myAgent, this.exchangeInterval, this.problemID, this.algorithm, this.peerList );
		//this.myAgent.addBehaviour( this.myAgent.tbf.wrap( this.exchangeBehaviour ) );
		this.myAgent.addBehaviour( this.exchangeBehaviour );
		
		this.loggerBehaviour = new ComputeLoggerBehaviour( this.myAgent, this.exchangeInterval, this.problemID, this.algorithm );
		this.myAgent.addBehaviour( this.myAgent.tbf.wrap( this.loggerBehaviour ) );
		//this.myAgent.addBehaviour( this.loggerBehaviour );

		this.state = 1;
		if( this.problem == null || this.algorithm == null)
			this.state = 2;
	}

	private void execution() {
		this.algorithm.doIteration();
		iterCount ++;
	}

	private void tryReceive() {
		ACLMessage msg = null;
		while( ( msg = this.myAgent.receive( MessageTemplate.MatchPerformative( ACLMessage.PROPOSE ) ) ) != null ) {
			String[] line = msg.getContent().split("\n");
			if(line[0].matches(this.problemID)) {
				if( this.lossRate < rand.nextDouble() ) {
					msgCount++;
					String[] subLine = line[1].split(" ");
					double[] sol = new double[subLine.length];
					for( int i=0; i<subLine.length; i++ ) {
						sol[i] = Double.parseDouble( subLine[i] );
					}
					this.algorithm.addSolutions(sol);
				}
			}
		}
	}

	private void replyWithResult() {
		double[] sol = this.algorithm.getBestSolution();
		int[] isol = new int[ sol.length ];
		for( int i=0; i<sol.length; i++ ) {
			isol[i] = (int) sol[i];
		}
		this.addResultMsg( "Sol:" + Arrays.toString( isol ) );
		this.addResultMsg( "Result:" + this.problem.fitnessFunction(this.algorithm.getBestSolution()) );
		this.addResultMsg( "IterCount:" + this.iterCount );
		this.addResultMsg( "SentCount:" + this.exchangeBehaviour.getSentCount() );
		this.addResultMsg( "RecvCount:" + this.msgCount );
		this.addResultMsg( "LossRate:" + this.lossRate );
		this.addResultMsg( "SentResult:" );
		this.addResultMsg( this.exchangeBehaviour.getSentResult() );
		this.addResultMsg( "LoggerCount:" + this.loggerBehaviour.getLoggerCount() );
		this.addResultMsg( "LoggerResult:" );
		this.addResultMsg( this.loggerBehaviour.getLoggerResult() );


		ACLMessage reply = message.createReply();
		reply.setPerformative(ACLMessage.CONFIRM);
		reply.setContent( this.result );
		this.myAgent.send( reply );

		this.state = 3;
	}

	@Override
	public boolean done() {
		if(doneYet) {
			if( this.exchangeBehaviour != null ) {
				this.exchangeBehaviour.stop();
				this.myAgent.removeBehaviour( this.exchangeBehaviour );
				this.exchangeBehaviour = null;
			}
			
			if( this.loggerBehaviour != null ) {
				this.loggerBehaviour.stop();
				this.myAgent.removeBehaviour( this.loggerBehaviour );
				this.loggerBehaviour = null;
			}
		}
		return doneYet;
	}

}

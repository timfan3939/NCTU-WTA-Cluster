package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;
import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Problem.WTAProblem;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.GeneticAlgorithm;

public class ComputeExecuteBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219154629L;

	private ComputeAgent myAgent;
	private ACLMessage message;

	private boolean doneYet;
	private int state;

	private String result;
	private long starttime;

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
		this.starttime = 0;
		this.algorithm = null;
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

			this.result += "" + subLine.length + " ";
		}

		if ( subContent[1].startsWith("WTAProblem:") ) {
			this.problem = new WTAProblem(1, 1);
			this.problem.DecodeProblem(subContent[1]);
			this.algorithm = new GeneticAlgorithm(this.problem, 100);
		}

		this.starttime = System.currentTimeMillis();
		this.iterCount = 0;


		this.state = 1;
		if( this.problem == null )
			this.state = 2;
	}

	private void execution() {
		if ( System.currentTimeMillis() - this.starttime > 10000 ) {
			this.state = 2;
			result += " " + this.problem.fitnessFunction(this.algorithm.GetBestSolution());
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
		reply.setContent("Result from " + myAgent.getName() + " of Problem " + "this.problemID" + " " + this.result);
		this.myAgent.send( reply );

		this.state = 3;
	}

	@Override
	public boolean done() {
		return doneYet;
	}

}

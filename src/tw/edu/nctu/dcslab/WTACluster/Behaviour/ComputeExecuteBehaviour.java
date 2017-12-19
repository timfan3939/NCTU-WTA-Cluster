package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;

public class ComputeExecuteBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219154629L;

	private ComputeAgent myAgent;
	private ACLMessage message;

	private boolean doneYet;
	private int state;

	private String result;
	
	public ComputeExecuteBehaviour( ComputeAgent agent, ACLMessage message ) {
		super(agent);
		this.myAgent = agent;
		this.message = message;
		this.doneYet = false;
		this.state = 0;
		this.result = "";
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


		this.state = 1;
	}

	private void execution() {
		this.state = 2;
	}

	private void replyWithResult() {
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

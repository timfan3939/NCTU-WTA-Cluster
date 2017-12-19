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
	
	public ComputeExecuteBehaviour( ComputeAgent agent, ACLMessage message ) {
		super(agent);
		this.myAgent = agent;
		this.message = message;
		this.doneYet = false;
	}
	
	@Override
	public void action() {
		if ( doneYet )
			return;
		String[] line = this.message.getContent().split("\n");
		ACLMessage reply = message.createReply();
		reply.setContent("Result from " + myAgent.getName() + " of Problem " + line[1]);
		reply.setPerformative(ACLMessage.CONFIRM);
		this.myAgent.send(reply);
		
		this.doneYet = true;
	}

	@Override
	public boolean done() {
		return doneYet;
	}

}

package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

import tw.edu.nctu.dcslab.WTACluster.Agent.ComputeAgent;

public class ComputeListenBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 2L;

	private ComputeAgent myAgent;

	public ComputeListenBehaviour( ComputeAgent agent ) {
		super(agent);
		this.myAgent = agent;
	}

	@Override
	public void action() {
		ACLMessage msg = null;
		try {
			msg = myAgent.receive(MessageTemplate.MatchAll());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if(msg == null) {
			block();
			return;
		}

		String content = msg.getContent();
		String[] lines = content.split("\n");
		ACLMessage reply = null;
		switch (lines[0]) {
			case "Ping":
				reply = msg.createReply();
				reply.setContent("Ping reply from " + myAgent.getName());
				reply.setPerformative(ACLMessage.INFORM);
				myAgent.send(reply);
				break;
			case "quit":
				myAgent.addBehaviour( new ShutdownPlatformBehaviour(myAgent) );
				break;
			case "Problem":
				reply = msg.createReply();
				reply.setContent("Result from " + myAgent.getName() + " of Problem " + lines[1]);
				reply.setPerformative(ACLMessage.CONFIRM);
				myAgent.send(reply);
				break;
			default:
				System.out.println(content);
		}


	}
}

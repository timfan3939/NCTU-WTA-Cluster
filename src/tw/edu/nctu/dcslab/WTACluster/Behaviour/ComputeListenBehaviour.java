package tw.edu.nctu.dcslab.WTACluster.Behaviour;

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
			msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
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
				myAgent.addBehaviour( new ShutdownPlatformBehaviour( this.myAgent ) );
				break;
			case "Problem":
				this.myAgent.addBehaviour(new ComputeExecuteBehaviour( this.myAgent, msg ));
				break;
			default:
				System.out.println(content);
		}
	}
}

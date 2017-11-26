package tw.edu.nctu.dcslab.WTACluster.Agent.Behaviour;

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
		System.out.println(content);
	}
}

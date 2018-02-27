package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;

public class CentralListenBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 2L;

	private CentralAgent myAgent;

	public CentralListenBehaviour( CentralAgent agent ) {
		super(agent);
		this.myAgent = agent;
	}

	@Override
	public void action() {
		ACLMessage msg = null;
		try {
			msg = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
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

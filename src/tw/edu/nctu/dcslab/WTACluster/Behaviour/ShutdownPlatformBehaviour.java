package tw.edu.nctu.dcslab.WTACluster.Behaviour;


import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.content.onto.basic.Action;
import jade.content.lang.sl.SLCodec;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.content.lang.Codec;


public class ShutdownPlatformBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;

	private Agent myAgent;

	public ShutdownPlatformBehaviour(Agent agent) {
		super(agent);

		this.myAgent = agent;
	}

	@Override
	public void action() {
		block(1000);
		Codec codec = new SLCodec();
		this.myAgent.getContentManager().registerLanguage(codec);
		this.myAgent.getContentManager().registerOntology(JADEManagementOntology.getInstance());
		
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(myAgent.getAMS());
		msg.setLanguage(codec.getName());
		msg.setOntology(JADEManagementOntology.getInstance().getName());
		try {
			myAgent.getContentManager().fillContent(msg, new Action(myAgent.getAID(), new ShutdownPlatform()));
			myAgent.send(msg);
		} catch(Exception e) {
			System.err.println("Quitting Platform error");
			e.printStackTrace();
		}
	}
}

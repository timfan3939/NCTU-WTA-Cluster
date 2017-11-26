package tw.edu.nctu.dcslab.WTACluster.Agent.Behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Scanner;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;

public class CentralCommandBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;

	private CentralAgent myAgent;
	private Scanner scanner;


	public CentralCommandBehaviour(CentralAgent agent) {
		super(agent);

		this.myAgent = agent;
		this.scanner = new Scanner(System.in);
	}

	@Override
	public void action() {
		String line = scanner.nextLine();
		ACLMessage msg;

		switch (line) {
			case "quit":
				myAgent.addBehaviour(new CentralPingPeerBehaviour(myAgent, "quit") );
				myAgent.addBehaviour(new ShutdownPlatformBehaviour(myAgent) );
				break;
			case "loadpeer":
				myAgent.addBehaviour(new CentralLoadPeerBehaviour(myAgent) );
				break;
			case "pingpeer":
				myAgent.addBehaviour(new CentralPingPeerBehaviour(myAgent, "Ping") );
				break;
			default:
				System.out.println(line);
		}
	}
}

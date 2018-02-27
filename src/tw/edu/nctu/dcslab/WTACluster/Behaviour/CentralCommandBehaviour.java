package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.CyclicBehaviour;

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
			case "batch":
				myAgent.addBehaviour(new CentralExperimentBehaviour(myAgent, "experiment/single"));
				break;
			default:
				System.out.println("Error: " + line);
				System.out.println("Available: quit loadpeer pingpeer batch");
				break;
		}
	}
}

package tw.edu.nctu.dcslab.WTACluster.Agent.Behaviour;


import jade.core.Agent;
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
		System.out.println(line);
	}
}

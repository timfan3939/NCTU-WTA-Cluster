package tw.edu.nctu.dcslab.WTACluster.Agent;

import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;

import tw.edu.nctu.dcslab.WTACluster.Behaviour.ComputeListenBehaviour;

public class ComputeAgent extends Agent {
	private static final long serialVersionUID = 20171126144125L;
	public ThreadedBehaviourFactory tbf;

	@Override
	protected void setup() {
		super.setup();

		tbf = new ThreadedBehaviourFactory();
		this.addBehaviour( tbf.wrap( new ComputeListenBehaviour(this) ) );
	}
}

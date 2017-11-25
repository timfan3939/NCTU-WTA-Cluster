package tw.edu.nctu.dcslab.WTACluster.Agent;

import jade.core.Agent;
import jade.core.behaviours.ThreadedBehaviourFactory;

import tw.edu.nctu.dcslab.WTACluster.Agent.Behaviour.CentralCommandBehaviour;
import tw.edu.nctu.dcslab.WTACluster.Agent.Behaviour.CentralListenBehaviour;

public class CentralAgent extends Agent {
	private static final long serialVersionUID = -5271213701466983534L;
	ThreadedBehaviourFactory tbf;

	@Override
	protected void setup() {
		super.setup();

		tbf = new ThreadedBehaviourFactory();
		this.addBehaviour( tbf.wrap( new CentralCommandBehaviour(this) ) );
		this.addBehaviour( tbf.wrap( new CentralListenBehaviour(this) ) );

	}

}

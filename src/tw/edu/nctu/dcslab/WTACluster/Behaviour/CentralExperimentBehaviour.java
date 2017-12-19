package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;

public class CentralExperimentBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 20171215150151L;

	private CentralAgent myAgent;
	private String filename;

	public CentralExperimentBehaviour(CentralAgent agent, String filename) {
		super(agent);

		this.myAgent = agent;
		this.filename = filename;
	}

	public void action() {
		try {
			BufferedReader fin = new BufferedReader( new FileReader(this.filename) );

			SequentialBehaviour seq = new SequentialBehaviour(this.myAgent);
			String ID = "" + System.currentTimeMillis();

			String line;
			String setting = "";
			int i = 0;
			while( (line = fin.readLine()) != null ) {
				if ( line.matches("--") ) {
					seq.addSubBehaviour( new CentralDispatchProblemBehaviour( this.myAgent, ID + "_" + i, setting ) );
					seq.addSubBehaviour( new CentralCollectResultBehaviour( this.myAgent ) );
					setting = "";
					i++;
				}
				else if( line.startsWith("Problem:") ) {
					setting += line;
				}
				else {
					setting += "\n" + line;
				}
			}
			if ( !setting.isEmpty() ) {
				seq.addSubBehaviour( new CentralDispatchProblemBehaviour( this.myAgent, ID + "_" + i, setting ) );
				seq.addSubBehaviour( new CentralCollectResultBehaviour( this.myAgent ) );
			}

			this.myAgent.addBehaviour(seq);
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
}

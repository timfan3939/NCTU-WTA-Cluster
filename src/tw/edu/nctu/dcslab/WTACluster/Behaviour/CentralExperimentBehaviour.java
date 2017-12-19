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

			String line;
			while( (line = fin.readLine()) != null ) {
				System.out.println(line);
			}
			fin.close();
			
			SequentialBehaviour seq = new SequentialBehaviour(this.myAgent);

			String ID = "" + System.currentTimeMillis();

			for( int i=0; i<5; i++ ) {
				CentralDispatchProblemBehaviour dispatch = new CentralDispatchProblemBehaviour(this.myAgent, ID + "_" + i);
				CentralCollectResultBehaviour collect = new CentralCollectResultBehaviour(this.myAgent);
				
				seq.addSubBehaviour( dispatch );
				seq.addSubBehaviour( collect );
			}
			this.myAgent.addBehaviour(seq);
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
}

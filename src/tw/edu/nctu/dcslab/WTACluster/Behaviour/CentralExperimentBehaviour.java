package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.OneShotBehaviour;

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
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
}

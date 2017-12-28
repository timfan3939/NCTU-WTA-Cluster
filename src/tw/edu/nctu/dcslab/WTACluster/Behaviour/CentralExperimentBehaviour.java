package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;

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
			String ID = "" + new SimpleDateFormat( "YYYYMMddHHmmss" ).format( new Date() );

			String line;
			String setting = "";
			String problemFile = "";
			String problemID = "";
			int i = 0;
			while( (line = fin.readLine()) != null ) {
				if ( line.matches("--") ) {
					problemID = String.format( "%s_%03d", ID, i );
					seq.addSubBehaviour( new CentralDispatchProblemBehaviour( this.myAgent, problemID, problemFile, setting ) );
					seq.addSubBehaviour( new CentralCollectResultBehaviour( this.myAgent , problemID, problemFile, setting) );
					setting = "";
					i++;
					continue;
				}
				else if( line.startsWith("Problem:") ) {
					problemFile = line.split(":")[1];
				}
				setting += ( (setting.isEmpty()?"":"\n") + line );
			}
			if ( !problemFile.isEmpty() ) {
				seq.addSubBehaviour( new CentralDispatchProblemBehaviour( this.myAgent, ID + "_" + i, problemFile, setting ) );
				seq.addSubBehaviour( new CentralCollectResultBehaviour( this.myAgent, ID + "_" + i, problemFile, setting ) );
			}

			this.myAgent.addBehaviour(seq);
		}	
		catch( Exception e ) {
			e.printStackTrace();
		}
	}
}

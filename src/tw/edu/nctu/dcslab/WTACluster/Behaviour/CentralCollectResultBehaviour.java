package tw.edu.nctu.dcslab.WTACluster.Behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map;
import java.util.Set;
import java.io.FileWriter;

import tw.edu.nctu.dcslab.WTACluster.util.PeerInfo;
import tw.edu.nctu.dcslab.WTACluster.Agent.CentralAgent;
import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Problem.WTAProblem;

public class CentralCollectResultBehaviour extends Behaviour {
	private static final long serialVersionUID = 20171219140128L;

	private CentralAgent myAgent;
	private boolean doneYet = false;
	private ArrayList<PeerInfo> peerList;
	private TreeMap<String, String> resultTreeMap = new TreeMap<String, String>();
	private Set< Map.Entry<String, String> > resultSet = null;
	private ProblemInterface problem;
	private String problemFilename;
	private String problemID;
	private String setting;
	private String summaryFilename;
	private int state = 0;

	public CentralCollectResultBehaviour( CentralAgent agent, String problemID, String problemFilename, String setting, String summaryFilename) {
		super(agent);
		this.myAgent = agent;
		this.peerList = this.myAgent.getPeerList();
		this.problemID = problemID;
		this.setting = setting;
		this.problemFilename = problemFilename;
		this.summaryFilename = summaryFilename;
	}
	
	@Override
	public void action() {
		if( this.doneYet )
			return;
		switch ( this.state ) {
			case 0:
				this.receiveResult();
				break;
			case 1:
				this.writeResult();
				break;
			case 2:
				this.writeSummary();
				break;
			case 9:
				this.doneYet = true;
				break;
			default:
				this.state ++;
				break;
		}
	}

	public void receiveResult() {
		if( this.resultTreeMap.size() == this.peerList.size() ) {
			this.state ++;
			return;
		}

		ACLMessage msg = null;
		try {
			msg = this.myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM));
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		if(msg == null) {
			block();
			return;
		}

		String content = msg.getContent();
		String[] line = content.split("\n");

		if( line[0].split(":")[1].matches(this.problemID) ) {
			this.resultTreeMap.put( line[1], content );
		}
	}

	private void writeResult() {
		String filename = this.problemID + ".log";
		String result = "";
		result += this.setting.replace( ":", "\t" );
		result += "\n---\n";

		this.resultSet = this.resultTreeMap.entrySet();
		for( Map.Entry<String, String> entry : this.resultSet ) {
			result += entry.getValue() + "\n---\n";
		}

		try {
			FileWriter writer = new FileWriter( "log/" + filename );
			writer.write(result.replaceAll("\t",","));
			writer.close();
		}
		catch( Exception e ) {
			System.err.println("Error when writing file: " + filename);
			e.printStackTrace();
		}

		//System.out.println(result);
		this.state ++;
	}

	private void writeSummary() {
		this.problem = new WTAProblem(1,1);
		this.problem.loadProblemFromFile( this.problemFilename );
		ArrayList<int[]> solutionHistory = new ArrayList<int[]>();
		Object[] entryArray = this.resultSet.toArray();

		int minSolHis = 99999;
		for( int w=0; w<this.problem.getSolutionLength(); w++ ) {
			ArrayList<Integer> solList = new ArrayList<Integer>();
			Integer finalsol = null;

			Map.Entry<?, ?> entry = (Map.Entry<?, ?>) entryArray[ w % this.resultSet.size() ];
			String content = (String) entry.getValue();
			String[] line = content.split("\n");
			String[] ssol = null;
			for( int l=0; l<line.length; l++ ) {
				String[] subLine = line[l].split(":");
				if( subLine[0].matches("Sol") ) {
					ssol = subLine[1].replaceAll("\\[","").replaceAll("\\]","").replaceAll("\\s","").split(",");
					finalsol = new Integer(ssol[w]);
				}
				else if( subLine[0].matches("SentResult") ) {
					for( l++; l<line.length; l++ ) {
						subLine = line[l].split("\t");
						ssol = subLine[2].replaceAll("\\[","").replaceAll("\\]","").replaceAll("\\s","").split(",");
						solList.add( new Integer(ssol[w]) );
					}
				}
			}
			if( solList != null )
				solList.add(finalsol);

			int[] isol = new int[solList.size()];
			for( int i=0; i<isol.length; i++ ) {
				isol[i] = solList.get(i).intValue();
			}
			solutionHistory.add( isol ); 
			if( minSolHis > isol.length )
				minSolHis = isol.length;
		}

		String filename = this.problemID + "_summary" + ".csv";
		String result = "";
		String globalResult = "";
		result += this.setting.replace( ":", "\t" );
		result += "\n---\n";
		result += "Round\tValue";
		for( int w = 0; w < this.problem.getSolutionLength(); w++ ) 
			result += "\t" + w;
		result += "\n";
		
		for( int round=0; round < minSolHis; round++ ) {
			int[] sol = new int[this.problem.getSolutionLength()];
			double[] dsol = new double[this.problem.getSolutionLength()];
			for( int w = 0; w < sol.length; w++ ) {
				sol[w] = solutionHistory.get(w)[round];
				dsol[w] = (double) sol[w];
			}

			result += round;
			result += "\t" + this.problem.fitnessFunction( dsol );
			globalResult += this.problem.fitnessFunction( dsol ) + ",";
			for( int w = 0; w < sol.length; w++ ) {
				result += "\t" + sol[w];
			}
			result += "\n";
		}

		try {
			FileWriter writer = new FileWriter( "log/" + filename );
			writer.write(result.replaceAll("\t",","));
			writer.close();
		}
		catch( Exception e ) {
			System.err.println("Error when writing file: " + filename);
			e.printStackTrace();
		}

		try {
			FileWriter writer = new FileWriter( "log/" + this.summaryFilename, true);
			writer.write( problemID );
			writer.write( "," );
			writer.write( globalResult );
			writer.write("\n");
			writer.close();
		}
		catch( Exception e ) {
			System.err.println("Error when writing file: " + this.summaryFilename );
			e.printStackTrace();
		}

		System.out.println(result);
		
		this.state ++ ;
	}


	public boolean done() {
		if( doneYet )
			System.out.println("Problem done");
		return this.doneYet;
	}


}

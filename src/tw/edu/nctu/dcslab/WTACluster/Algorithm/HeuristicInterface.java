package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.ArrayList;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;


public abstract class HeuristicInterface {

	protected ProblemInterface problem;
	protected double[] bestSolution = new double[1];
	protected ArrayList<double[]> exchangedSolution = new ArrayList<double[]>();
	protected int solutionLength;
	protected double solutionValueMax;

	// Ask do one iteration
	public abstract void doIteration();
	
	// Get currently Best Solution
	public double[] getBestSolution() {
		return this.bestSolution;
	}

	public abstract boolean setAlgorithmParameter(String str);


	// Add Possible Solutions
	public void addSolutions(double[] solution) {
		if( solution.length != this.solutionLength )
			return;
		synchronized(this.exchangedSolution) {
			this.exchangedSolution.add( solution.clone() );
		}
	}
	
	// Set Problem Interface
	public void setProblemInterface(ProblemInterface problem) {
		this.problem = problem;
		this.solutionLength = problem.getSolutionLength();
		this.solutionValueMax = problem.getSolutionMax();
	}
}

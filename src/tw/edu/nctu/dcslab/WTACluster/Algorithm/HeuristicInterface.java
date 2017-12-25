package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;


public abstract class HeuristicInterface {

	protected ProblemInterface problem;

	// Ask do one iteration
	public abstract void doIteration();
	
	protected double[] bestSolution = new double[1];
	// Get currently Best Solution
	public double[] getBestSolution() {
		return this.bestSolution;
	}

	public abstract boolean setAlgorithmParameter(String str);

	protected ArrayList<double[]> exchangedSolution = new ArrayList<double[]>();

	// Add Possible Solutions
	public void addSolutions(double[] solution) {
		if( solution.length != this.solutionLength )
			return;
		synchronized(this.exchangedSolution) {
			this.exchangedSolution.add( solution.clone() );
		}
	}
	
	protected int solutionLength;
	protected double solutionValueMax;
	// Set Problem Interface
	public void setProblemInterface(ProblemInterface problem) {
		this.problem = problem;
		this.solutionLength = problem.getSolutionLength();
		this.solutionValueMax = problem.getSolutionMax();
	}
}

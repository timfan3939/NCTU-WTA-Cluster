package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;


public abstract class HeuristicInterface {

	protected ProblemInterface problem;

	// Ask do one iteration
	public abstract void DoIteration();
	
	protected int[] bestSolution;
	// Get currently Best Solution
	public int[] GetBestSolution() {
		return this.bestSolution;
	}

	// Add Possible Solutions
	public abstract void AddSolutions(int[][] solution);
	
	protected int solutionLength;
	protected int solutionValueMax;
	// Set Problem Interface
	public void SetProblemInterface(ProblemInterface problem) {
		this.problem = problem;
		this.solutionLength = problem.getSolutionLength();
		this.solutionValueMax = problem.getSolutionMax();
	}

}

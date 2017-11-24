package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;


public abstract class HeuristicInterface {

	protected ProblemInterface problem;

	// Ask do one iteration
	public abstract void DoIteration();

	// Get currently Best Solution
	public abstract int[] GetBestSolution();

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

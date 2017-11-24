package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;


public interface HeuristicInterface {
	// Ask do one iteration
	public void DoIteration();

	// Get currently Best Solution
	public int[] GetBestSolution();

	// Add Possible Solutions
	public void AddSolutions(int[][] solution);

	// Set Problem Interface
	public void SetProblemInterface(ProblemInterface problem);
}

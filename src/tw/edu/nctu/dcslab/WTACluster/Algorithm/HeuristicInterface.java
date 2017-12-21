package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.ArrayList;
import java.util.Arrays;

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

	public abstract boolean SetAlgorithmParameter(String str);

	protected ArrayList<int[]> exchangedSolution = new ArrayList<int[]>();

	// Add Possible Solutions
	public void AddSolutions(int[] solution) {
		if( solution.length != this.solutionLength )
			return;
		synchronized(this.exchangedSolution) {
			this.exchangedSolution.add(Arrays.copyOf(solution, this.solutionLength));
		}
		synchronized(this.bestSolution) {
			if( this.problem.fitnessFunction(this.bestSolution) > this.problem.fitnessFunction(solution) ) {
				this.bestSolution = Arrays.copyOf(solution, this.solutionLength);
			}
		}
	}
	
	protected int solutionLength;
	protected int solutionValueMax;
	// Set Problem Interface
	public void SetProblemInterface(ProblemInterface problem) {
		this.problem = problem;
		this.solutionLength = problem.getSolutionLength();
		this.solutionValueMax = problem.getSolutionMax();
	}
}

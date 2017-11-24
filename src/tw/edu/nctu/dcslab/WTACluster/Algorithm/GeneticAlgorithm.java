package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.Random;
import java.util.Arrays;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;

public class GeneticAlgorithm implements HeuristicInterface { 
	private Random rand;

	private int population;
	
	private ProblemInterface problem;

	public GeneticAlgorithm() {
		this.rand = new Random();
	}

	public void DoIteration() {
	}

	public int[] GetBestSolution() {
		return null;
	}

	public void AddSolutions(int[][] solutions) {
	}

	public void SetProblemInterface(ProblemInterface problem) {
	}

	private class Chromosomes implements Comparable<Chromosomes> {
		public double value;
		public int[] solution;
		ProblemInterface problem;

		public Chromosomes ( int[] solution, ProblemInterface problem ) {
			this.problem = problem;
			this.solution = Arrays.copyOf(solution, solution.length);
		}
		
		@Override
		public int compareTo( Chromosomes ch ) {
			if(this.value > ch.value)
				return 1;
			return 0;
		}
	}
}

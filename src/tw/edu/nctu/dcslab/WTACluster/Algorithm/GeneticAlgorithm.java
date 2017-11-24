package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;

public class GeneticAlgorithm extends HeuristicInterface { 
	private Random rand;

	private int population;
	private double crossoverRate;
	private double mutationRate;
	private ArrayList<Chromosomes> solutions;
	private ArrayList<Chromosomes> childSolutions;

	public GeneticAlgorithm( ProblemInterface problem, int population ) {
		super.SetProblemInterface( problem );
		this.rand = new Random();
		this.population = population;

		this.GeneratePopulation();
	}

	public void GeneratePopulation() {
		solutions = new ArrayList<Chromosomes>();
		int p_length = problem.getSolutionLength();
		int p_value = problem.getSolutionMax();

		for ( int i=0; i<population; i++ ) {
			int[] sol = new int[p_length];
			for (int j=0; j<p_length; j++) {
				sol[j] = rand.nextInt(p_value);
			}
			solutions.add( new Chromosomes(sol, problem) );
		}
	}

	public void DoIteration() {
		childSolutions = new ArrayList<Chromosomes>();
	}

	private void duplicate() {
	}

	private ArrayList<Chromosomes> Crossover(ArrayList<Chromosomes> solutions) {
		return null;
	}


	public int[] GetBestSolution() {
		UpdateSolutions();
		Collections.sort(solutions);
		return solutions.get(0).solution;
	}

	public void UpdateSolutions() {
		for ( Chromosomes ch : solutions )
			ch.UpdateValue();
	}

	public void AddSolutions(int[][] solutions) {
	}

	private class Chromosomes implements Comparable<Chromosomes> {
		public double value;
		public int[] solution;
		ProblemInterface problem;

		public Chromosomes ( int[] solution, ProblemInterface problem ) {
			this.problem = problem;
			this.solution = Arrays.copyOf(solution, solution.length);
		}

		public void UpdateValue() {
			value = problem.fitnessFunction(solution);
		}
		
		@Override
		public int compareTo( Chromosomes ch ) {
			if(this.value > ch.value)
				return 1;
			return 0;
		}
	}
}

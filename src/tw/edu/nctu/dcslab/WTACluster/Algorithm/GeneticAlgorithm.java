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
		
		Duplicate();
		Crossover();
		Mutation();
		Selection();

		childSolutions = null;
	}

	private void Duplicate() {
		for ( Chromosomes ch : solutions ) {
			childSolutions.add( new Chromosomes( ch.solution, problem ) );
		}
	}

	private void Crossover() {
		int crossoverRound = population/2;

		for ( int round=0; round<crossoverRound; round++ ) {
			int A = rand.nextInt(population);
			int B = 0;

			do {
				B = rand.nextInt(population);
			} while (A==B);

			Chromosomes pa1 = solutions.get(A);
			Chromosomes pa2 = solutions.get(B);

			int crossoverPosition = 0;
			do {
				crossoverPosition = rand.nextInt(solutionLength);
			} while (crossoverPosition == 0);

			int[] ch1 = new int[solutionLength];
			int[] ch2 = new int[solutionLength];

			for ( int i=0; i<solutionLength; i++) {
				if ( i == crossoverPosition ) {
					Chromosomes tmp = pa1;
					pa1 = pa2;
					pa2 = tmp;
				}
				ch1[i] = pa1.solution[i];
				ch2[i] = pa2.solution[i];
			}
			childSolutions.add( new Chromosomes(ch1, problem) );
			childSolutions.add( new Chromosomes(ch2, problem) );
		}
	}

	private void Mutation() {
		for ( Chromosomes pa : solutions ) {
			if ( rand.nextDouble() <= mutationRate ) {
				int[] ch = Arrays.copyOf(pa.solution, solutionLength);
				int mutatePos = rand.nextInt(solutionLength);
				int value = 0;
				do {
					value = rand.nextInt(solutionValueMax);
				} while ( ch[mutatePos] == value );

				ch[mutatePos] = value;
				childSolutions.add( new Chromosomes( ch, problem ) );
			}
		}
	}

	private void Selection() {
		solutions.clear();

		UpdateChildSolutions();
		double totalValue = 0;
		for ( Chromosomes ch : childSolutions )
			totalValue += ( 1 / ch.value );
		
		for ( int i=0; i<population; i++ ) {
			double chosen = rand.nextDouble();
			for ( int j=0; j<childSolutions.size(); j++ ) {
				chosen -= ( 1/childSolutions.get(j).value ) / totalValue;
				if ( chosen < 0.0 ) {
					solutions.add( childSolutions.get(j) );
				}
			}
		}
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

	public void UpdateChildSolutions() {
		if ( childSolutions != null ) {
			for ( Chromosomes ch : childSolutions )
				ch.UpdateValue();
		}
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

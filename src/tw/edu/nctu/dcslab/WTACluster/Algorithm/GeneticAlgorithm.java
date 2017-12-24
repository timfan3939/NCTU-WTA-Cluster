package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;

public class GeneticAlgorithm extends HeuristicInterface { 
	private Random rand = new Random();

	private int population;
	private double crossoverRate = 0.8;
	private double mutationRate = 0.4;
	private ArrayList<Chromosomes> solutions;
	private ArrayList<Chromosomes> childSolutions;

	public GeneticAlgorithm( ProblemInterface problem, int population ) {
		super.setProblemInterface( problem );
		this.population = population;

		this.generatePopulation();
	}

	public void generatePopulation() {
		solutions = new ArrayList<Chromosomes>();
		int p_length = problem.getSolutionLength();
		double p_value = problem.getSolutionMax();

		for ( int i=0; i<population; i++ ) {
			double[] sol = new double[p_length];
			for (int j=0; j<p_length; j++) {
				sol[j] = rand.nextDouble() * p_value;
			}
			solutions.add( new Chromosomes(sol, problem) );
		}
		childSolutions = new ArrayList<Chromosomes>();
	}

	public void doIteration() {
		duplicate();
		crossover();
		mutation();
		selection();

		updateLocalBestSolution();

		childSolutions.clear();
	}

	private void duplicate() {
		for ( Chromosomes ch : solutions ) {
			childSolutions.add( new Chromosomes( ch.solution, problem ) );
		}
	}

	private void crossover() {
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

			double[] ch1 = new double[solutionLength];
			double[] ch2 = new double[solutionLength];

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

	private void mutation() {
		for ( Chromosomes pa : solutions ) {
			if ( rand.nextDouble() <= mutationRate ) {
				double[] ch = Arrays.copyOf(pa.solution, solutionLength);
				int mutatePos = rand.nextInt(solutionLength);
				double value = 0;
				do {
					value = rand.nextDouble() * solutionValueMax;
				} while ( ch[mutatePos] == value );

				ch[mutatePos] = value;
				childSolutions.add( new Chromosomes( ch, problem ) );
			}
		}
	}

	private void selection() {
		synchronized(this.exchangedSolution) {
			for( double[] sol: this.exchangedSolution ) {
				this.childSolutions.add( new Chromosomes( sol, this.problem ) );
			}
			this.exchangedSolution.clear();
		}


		solutions.clear();
		updateChildSolutions();

		double totalValue = 0.0;
		double[] value = new double[childSolutions.size()];
		for ( int i = 0; i<value.length; i++ ) {
			value[i] = 1/childSolutions.get(i).value;
			totalValue += value[i];
		}
		
		for ( int i=0; i<population; i++ ) {
			double chosen = rand.nextDouble();
			for ( int j=0; j<value.length; j++ ) {
				chosen -= value[j] / totalValue;
				if ( chosen < 0.0 ) {
					solutions.add( childSolutions.get(j) );
					break;
				}
			}
		}
	}


	public void updateLocalBestSolution() {
		updateSolutions();
		Collections.sort(solutions);

		if(bestSolution == null) {
			bestSolution = solutions.get(0).solution;
			return;
		}
		
		if( problem.fitnessFunction(bestSolution) > solutions.get(0).value ) {
			bestSolution = solutions.get(0).solution.clone();
		}
	}

	public void updateSolutions() {
		for ( Chromosomes ch : solutions )
			ch.updateValue();
	}

	public void updateChildSolutions() {
		if ( childSolutions != null ) {
			for ( Chromosomes ch : childSolutions )
				ch.updateValue();
		}
	}

	private class Chromosomes implements Comparable<Chromosomes> {
		public double value;
		public double[] solution;
		ProblemInterface problem;

		public Chromosomes ( double[] solution, ProblemInterface problem ) {
			this.problem = problem;
			this.solution = Arrays.copyOf(solution, solution.length);
		}

		public void updateValue() {
			value = problem.fitnessFunction(solution);
		}
		
		@Override
		public int compareTo( Chromosomes ch ) {
			this.updateValue();
			ch.updateValue();
			
			if(this.value > ch.value)
				return 1;
			else if (this.value < ch.value)
				return -1;
			else {
				for( int i=0; i<this.solution.length; i++ ) {
					if ( (int)this.solution[i] > (int)ch.solution[i] ) {
						return 1;
					}
					else if ( (int)this.solution[i] < (int)ch.solution[i] ) {
						return -1;
					}
				}
			}
			return 0;
		}
	}

	@Override
	public boolean setAlgorithmParameter(String str) {
		String[] line = str.split("\n");
		String[] subline = null;

		for(int l = 0; l < line.length; l++) {
			subline = line[l].split(":");
			if( subline.length != 2 ) {
				continue;
			}
			switch(subline[0]) {
				case "CrossoverRate":
					this.crossoverRate = java.lang.Double.parseDouble(subline[1]);
					break;
				case "MutationRate":
					this.mutationRate = java.lang.Double.parseDouble(subline[1]);
					break;
				default:
					break;
			}
		}

		return false;
	}
}

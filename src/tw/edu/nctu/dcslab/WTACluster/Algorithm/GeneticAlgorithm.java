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
	private Chromosomes bestSolutionChromosomes = null;

	public GeneticAlgorithm( ProblemInterface problem, int population ) {
		super.setProblemInterface( problem );
		this.population = population;

		this.generatePopulation();
	}

	public void generatePopulation() {
		this.solutions = new ArrayList<Chromosomes>();
		this.childSolutions = new ArrayList<Chromosomes>();

		for ( int i=0; i<this.population; i++ ) {
			double[] sol = new double[this.solutionLength];
			for (int j=0; j<this.solutionLength; j++) {
				sol[j] = rand.nextDouble() * this.solutionValueMax;
			}
			solutions.add( new Chromosomes(sol, this.problem) );
		}
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
		Collections.sort( this.solutions );
		for ( int i = 0; i < this.population * 0.1; i++ )
			childSolutions.add( new Chromosomes( this.solutions.get(i).solution, this.problem) );
	}

	private void crossover() {
		int crossoverRound = population/2;

		for ( int round=0; round<crossoverRound; round++ ) {
			// Do cross over if random number is less or equal to the crossover rate
			if ( this.crossoverRate > rand.nextDouble() )
				continue;

			int A = rand.nextInt(population);
			int B = 0;

			do {
				B = rand.nextInt(population);
			} while (A==B);

			Chromosomes pa1 = solutions.get(A);
			Chromosomes pa2 = solutions.get(B);

			int crossoverPosition = 0;
			do {
				crossoverPosition = rand.nextInt( this.solutionLength );
			} while (crossoverPosition == 0);
			
			double[] ch1 = new double[ this.solutionLength ];
			double[] ch2 = new double[ this.solutionLength ];

			for ( int i=0; i<solutionLength; i++) {
				if ( i == crossoverPosition ) {
					Chromosomes tmp = pa1;
					pa1 = pa2;
					pa2 = tmp;
				}
				ch1[i] = pa1.solution[i];
				ch2[i] = pa2.solution[i];
			}
			childSolutions.add( new Chromosomes(ch1, this.problem) );
			childSolutions.add( new Chromosomes(ch2, this.problem) );
		}
	}

	private void mutation() {
		for ( Chromosomes pa : solutions ) {
			// Do mutation if the random number is smaller or equal to the mutation rate
			if ( rand.nextDouble() <= mutationRate ) {
				double[] ch = pa.solution.clone();
				int mutatePos = rand.nextInt(solutionLength);
				double value = 0.0;
				do {
					value = rand.nextDouble() * this.solutionValueMax;
				} while ( (int)ch[mutatePos] == (int)value );

				ch[mutatePos] = value;
				childSolutions.add( new Chromosomes( ch, this.problem ) );
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

		double totalValue = 0.0;
		double[] value = new double[childSolutions.size()];
		for ( int i = 0; i<value.length; i++ ) {
			value[i] = 1.0/childSolutions.get(i).value;
			totalValue += value[i];
		}
		
		while( solutions.size() < population ) {
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
		Collections.sort( this.childSolutions );
		if( this.bestSolutionChromosomes != null ) {
			if ( this.bestSolutionChromosomes.compareTo(this.childSolutions.get(0)) > 0 ) {
				this.bestSolutionChromosomes = new Chromosomes( this.childSolutions.get(0).solution, this.problem );
				synchronized( this.bestSolution ) {
					this.bestSolution = this.bestSolutionChromosomes.solution;
				}
			}
		}
		else {
			this.bestSolutionChromosomes = new Chromosomes( this.childSolutions.get(0).solution, this.problem );
			synchronized( this.bestSolution ) {
				this.bestSolution = this.bestSolutionChromosomes.solution;
			}
		}
	}

	private class Chromosomes implements Comparable<Chromosomes> {
		public double value;
		public double[] solution;
		ProblemInterface problem;

		public Chromosomes ( double[] solution, ProblemInterface problem ) {
			this.problem = problem;
			this.solution = solution.clone();
			this.updateValue();
		}

		public void updateValue() {
			this.value = this.problem.fitnessFunction( this.solution );
		}
		
		@Override
		public int compareTo( Chromosomes ch ) {
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

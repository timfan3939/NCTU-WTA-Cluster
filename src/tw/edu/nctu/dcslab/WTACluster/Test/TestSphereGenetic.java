package tw.edu.nctu.dcslab.WTACluster.Test;

import java.util.Random;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Problem.SphereProblem;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.GeneticAlgorithm;

public class TestSphereGenetic {

	public static void main( String args[] ) {

		Random rand = new Random();

		int numLength = 14;
		int solMax = 10000;

		SphereProblem sphere = new SphereProblem(numLength);

		ProblemInterface problem = sphere;


		for( int round = 0; round < 10; round++ ) {
			int solution[] = new int[numLength];

			for( int i=0; i<14; i++ ) 
				solution[i] = rand.nextInt(solMax);

			System.out.println("" + round + " " + problem.fitnessFunction(solution));
		}
		
		GeneticAlgorithm genetic = new GeneticAlgorithm(problem, 100);

		System.out.println("Start Genetic Test");

		double bestValue = 9999;
		int percentage = 0;
		int iterationRound = 2000000;

		for( int i=0; i<iterationRound; i++ ) {
			genetic.DoIteration();

			if ( bestValue > problem.fitnessFunction(genetic.GetBestSolution()) ) {
				double tmp = problem.fitnessFunction(genetic.GetBestSolution());
				System.out.println("~~" + i + ": " + bestValue + " diff: " + (tmp - bestValue));
				bestValue = tmp;
			}

			if ( percentage != (100 * i/iterationRound) ) {
				percentage = (100 * i/iterationRound);
				System.out.println("" + percentage + "% completed.");
			}


		}

	}
}

package tw.edu.nctu.dcslab.WTACluster.Test;

import java.util.Random;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Problem.WTAProblem;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.HeuristicInterface;
import tw.edu.nctu.dcslab.WTACluster.Algorithm.GeneticAlgorithm;

public class TestPSOAlgorithm {

	public static void main( String args[] ) {

		Random rand = new Random();

		int numWeapon = 14;
		int numTarget = 70;

		WTAProblem wta = new WTAProblem(numWeapon, numTarget);

		double[] weight = new double[numTarget];
		for ( int i=0; i<numTarget; i++ )
			weight[i] = rand.nextDouble() * 10;

		double[][] hitProbability = new double[numWeapon][numTarget];
		for ( int i=0; i<numWeapon; i++ )
			for ( int j=0; j<numTarget; j++ )
				hitProbability[i][j] = rand.nextDouble();

		wta.setTargetWeight(weight);
		wta.setHitProbability( hitProbability );

		ProblemInterface problem = wta;

		for( int round = 0; round < 10; round++ ) {
		
			problem.loadProblemFromFile("problem/WTAProblem.txt");
			double solution[] = new double[numWeapon];

			for( int i=0; i<14; i++ ) 
				//solution[i] = rand.nextInt(numTarget);
				solution[i] = i;

			System.out.println("" + round + " " + problem.fitnessFunction(solution));
		}

		//problem.SaveProblemToFile("WTAProblem.txt");
		//problem.LoadProblemFromFile("WTAProblem.txt");
		
		GeneticAlgorithm genetic = new GeneticAlgorithm(problem, 100);

		System.out.println("Start Genetic Test");

		double bestValue = 9999;
		int percentage = 0;
		int iterationRound = 200000;

		for( int i=0; i<iterationRound; i++ ) {
			genetic.doIteration();

			if ( bestValue > problem.fitnessFunction(genetic.getBestSolution()) ) {
				double tmp = problem.fitnessFunction(genetic.getBestSolution());
				System.out.println("## " + i + ": \n\t" + bestValue + " -> \n\t" + tmp + " \n\tdiff: " + (tmp - bestValue));
				bestValue = tmp;
			}

			if ( percentage != (100 * i/iterationRound) ) {
				percentage = (100 * i/iterationRound);
				System.out.println("" + percentage + "% completed.");
			}


		}
		System.out.println("Best Value: " + bestValue);

	}
}

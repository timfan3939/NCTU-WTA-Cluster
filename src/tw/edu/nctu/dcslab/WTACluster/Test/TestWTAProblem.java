package tw.edu.nctu.dcslab.WTACluster.Test;

import java.util.Random;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;
import tw.edu.nctu.dcslab.WTACluster.Problem.WTAProblem;

public class TestWTAProblem {

	public static void main( String args[] ) {

		Random rand = new Random();

		WTAProblem wta = new WTAProblem(14, 70);

		double[] weight = new double[70];
		for ( int i=0; i<70; i++ )
			weight[i] = rand.nextDouble();

		double[][] hitProbability = new double[14][70];
		for ( int i=0; i<14; i++ )
			for ( int j=0; j<70; j++ )
				hitProbability[i][j] = rand.nextDouble();

		wta.setTargetWeight(weight);
		wta.setHitProbability( hitProbability );

		ProblemInterface problem = wta;

		for( int round = 0; round < 10; round++ ) {
			int solution[] = new int[14];

			for( int i=0; i<14; i++ ) 
				solution[i] = rand.nextInt(70);

			System.out.println("" + round + " " + problem.fitnessFunction(solution));
		}
	}
}

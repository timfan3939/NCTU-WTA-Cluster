package tw.edu.nctu.dcslab.WTACluster.Problem;

import java.util.Arrays;

public class WTAProblem implements ProblemInterface {

	private int numWeapon;
	private int numTarget;

	private double[][] hitProbability;

	private double[] targetWeight;

	public WTAProblem (int numWeapon, int numTarget) {
		this.numWeapon = numWeapon;
		this.numTarget = numTarget;
		
		this.targetWeight = new double[this.numTarget];
		Arrays.fill( this.targetWeight, 0.0 );
	
		this.hitProbability = new double[this.numWeapon][this.numTarget];
		for (double row[] : this.hitProbability)
			Arrays.fill( row, 0.0 );
	}

	public double FitnessFunction(int[] solution) {
		double result = 0.0;

		for (int j=0; j<numTarget; j++) {
			double remainProbability = 1.0;
			for (int i=0; i<numWeapon; i++) {
				if (solution[i] == j)
					remainProbability *= (1-hitProbability[i][j]);
			}
			result += targetWeight[j] * remainProbability;
		}

		return result;
	}
}

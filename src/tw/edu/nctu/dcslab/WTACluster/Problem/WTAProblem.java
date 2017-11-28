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

	public void setTargetWeight( double[] targetWeight ) {
		this.targetWeight = Arrays.copyOf(targetWeight, numTarget);
	}

	public void setHitProbability( double[][] hitProbability ) {
		for (int i=0; i<this.numWeapon; i++) {
			this.hitProbability[i] = Arrays.copyOf(hitProbability[i], numTarget);
		}
	}

	public double fitnessFunction(int[] solution) {
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
	
	public String EncodeProblem() {
		String str = "Probabability:";
		
		for(int i=0; i<this.numWeapon; i++) {
			for( int j=0; j<this.numTarget; j++ )
				str += "" + this.hitProbability[i][j] + " ";
			str += "\n";
		}
		str += "Weight:\n";
		for( int i=0; i<this.numTarget; i++ ) {
			str += "" + this.targetWeight[i] + " ";
		}

		return str;
	}

	public void DecodeProblem(String str) {
		int lineNum = 0;
		String[] line = str.split("\n");
		String[] subLine;
		// For the Probability Line
		lineNum++;

		for( int i = 0; i<this.numWeapon; i++ ) {
			subLine = line[lineNum + i].split(" ");
			for( int j=0; j<this.numTarget; j++ ) {
				this.hitProbability[i][j] = Double.parseDouble(subLine[j]);
			}
		}

		lineNum += this.numWeapon;
		// For the Weight Line
		lineNum++;
		subLine = line[lineNum].split(" ");
		for( int i=0; i<this.numTarget; i++ )
			this.targetWeight[i] = Double.parseDouble(subLine[i]);

	}

	public int getSolutionLength() {
		return this.numWeapon;
	}

	public int getSolutionMax() {
		return this.numTarget;
	}

	public String toString() {
		String str = this.EncodeProblem();
		return str;
	}
}

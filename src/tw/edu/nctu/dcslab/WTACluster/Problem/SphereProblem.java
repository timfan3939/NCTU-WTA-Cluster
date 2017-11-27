package tw.edu.nctu.dcslab.WTACluster.Problem;

import java.util.Arrays;

public class SphereProblem implements ProblemInterface {
	private int solutionLength;
	public SphereProblem(int solutionLength) {
		this.solutionLength = solutionLength;
	}

	public double fitnessFunction(int[] solution) {
		double result = 0.0;

		for (int i=0; i<solutionLength; i++) {
			double num = ((double) solution[i]) - 5001.0;
			result += num*num;
		}
		return result;
	}

	public int getSolutionLength() {
		return this.solutionLength;
	}

	public int getSolutionMax() {
		return 10000;
	}
}

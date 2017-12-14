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
	    public String EncodeProblem() {
			return null;
	    }       
		
		public void DecodeProblem(String str) {
		}

	public int getSolutionLength() {
		return this.solutionLength;
	}

	public int getSolutionMax() {
		return 10000;
	}

	public void LoadProblemFromFile(String path) {
		// TODO: maybe load problem from a file?
	}

	public void SaveProblemToFile(String path) {
		// TODO:
	}
}

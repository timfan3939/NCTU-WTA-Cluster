package tw.edu.nctu.dcslab.WTACluster.Problem;


public class SphereProblem implements ProblemInterface {
	private int solutionLength;
	public SphereProblem(int solutionLength) {
		this.solutionLength = solutionLength;
	}

	public double fitnessFunction(double[] solution) {
		double result = 0.0;

		for (int i=0; i<solutionLength; i++) {
			double num = solution[i] - 5001.0;
			result += num*num;
		}
		return result;
	}
	    public String encodeProblem() {
			return null;
	    }       
		
		public void decodeProblem(String str) {
			// TODO:
		}

	public int getSolutionLength() {
		return this.solutionLength;
	}

	public double getSolutionMax() {
		return 10000.0;
	}

	public void loadProblemFromFile(String path) {
		// TODO: maybe load problem from a file?
	}

	public void saveProblemToFile(String path) {
		// TODO:
	}
}

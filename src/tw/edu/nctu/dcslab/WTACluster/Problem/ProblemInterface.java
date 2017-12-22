package tw.edu.nctu.dcslab.WTACluster.Problem;

public interface ProblemInterface {
	// Provide a fitness function calculatio
	public double fitnessFunction(double[] solution);

	// The length of the solution array
	public int getSolutionLength();
	// The max integer value of the element in a solution
	public double getSolutionMax();
	
	// Encode and decode a problem
	public String encodeProblem();
	public void decodeProblem(String str);

	public void loadProblemFromFile(String path);
	public void saveProblemToFile(String path);
}

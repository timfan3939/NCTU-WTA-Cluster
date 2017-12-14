package tw.edu.nctu.dcslab.WTACluster.Problem;

public interface ProblemInterface {
	// Provide a fitness function calculatio
	public double fitnessFunction(int [] solution);

	// The length of the solution array
	public int getSolutionLength();
	// The max integer value of the element in a solution
	public int getSolutionMax();
	
	// Encode and decode a problem
	public String EncodeProblem();
	public void DecodeProblem(String str);

	public void LoadProblemFromFile(String path);
	public void SaveProblemToFile(String path);
}

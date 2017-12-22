package tw.edu.nctu.dcslab.WTACluster.Problem;

import java.util.Arrays;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Integer;
import java.lang.NumberFormatException;

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

	public double fitnessFunction(double[] solution) {
		double result = 0.0;

		for (int j=0; j<numTarget; j++) {
			double remainProbability = 1.0;
			for (int i=0; i<numWeapon; i++) {
				if ((int)solution[i] == j)
					remainProbability *= (1-hitProbability[i][j]);
			}
			result += targetWeight[j] * remainProbability;
		}

		return result;
	}
	
	public String encodeProblem() {
		String str = "WTAProblem:\n";
		str += "Weapon:" + this.numWeapon + "\n";
		str += "Target:" + this.numTarget + "\n";

		str += "Probability:\n";
		
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

	public void decodeProblem(String str) {
		int lineNum = 0;
		String[] line = str.split("\n");
		String[] subLine;
		
		if (!line[lineNum].matches("WTAProblem:")) {
			System.err.println("This is not WTA Problem, this is " + line[0]);
			return;
		}
		lineNum++;

		subLine = line[lineNum].split(":");
		if ( subLine[0].matches("Weapon") ) {
			try {
				this.numWeapon = Integer.parseInt(subLine[1]);
			}
			catch (NumberFormatException e) {
				System.err.println("Error when parsing weapon number line: " + line[lineNum]);
				e.printStackTrace();
				return;
			}
		}
		else {
			System.err.println("This is not the line about the number of agent: " + line[lineNum]);
			return;
		}
		lineNum++;

		subLine = line[lineNum].split(":");
		if ( subLine[0].matches("Target") ) {
			try {
				this.numTarget = Integer.parseInt(subLine[1]);
			}
			catch (NumberFormatException e) {
				System.err.println("Error when parsing target number line: " + line[lineNum]);
				e.printStackTrace();
				return;
			}
		}
		else {
			System.err.println("This is not the line about the number of target: " + line[lineNum]);
			return;
		}
		lineNum++;

		this.targetWeight = new double[this.numTarget];
		this.hitProbability = new double[this.numWeapon][this.numTarget];
		for (double row[] : this.hitProbability)
			Arrays.fill( row, 0.0 );
		
		if( !line[lineNum].matches("Probability:") ) {
			System.err.println("Error when checking the line of Probability: " + line[lineNum]);
			return;
		}
		lineNum++;

		for( int i = 0; i<this.numWeapon; i++ ) {
			subLine = line[lineNum + i].split(" ");
			for( int j=0; j<this.numTarget; j++ ) {
				this.hitProbability[i][j] = Double.parseDouble(subLine[j]);
			}
		}
		lineNum += this.numWeapon;

        if( !line[lineNum].matches("Weight:") ) {
			System.err.println("Error when checking the line of Weight " + line[lineNum]);
            return;
        }
        lineNum++;


		subLine = line[lineNum].split(" ");
		for( int i=0; i<this.numTarget; i++ )
			this.targetWeight[i] = Double.parseDouble(subLine[i]);

	}

	public int getSolutionLength() {
		return this.numWeapon;
	}

	public double getSolutionMax() {
		return (double) this.numTarget;
	}

	public String toString() {
		String str = this.encodeProblem();
		return str;
	}

	public void saveProblemToFile(String path) {
		try {
			FileOutputStream fout = new FileOutputStream( path );
			fout.write(this.encodeProblem().getBytes());
			fout.flush();
			fout.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("File Not Found Exception with " + path);
			e.printStackTrace();
		}
		catch (IOException e) {
			System.err.println("I/O error with " + path);
			e.printStackTrace();
		}
	}

	public void loadProblemFromFile(String path) {
		String content = "";
		try {
			BufferedReader fin = new BufferedReader( new FileReader(path) );
			
			String line;
			while( (line = fin.readLine()) != null ) {
				content += line + "\n";
			}
			
			fin.close();
		}
		catch (FileNotFoundException e) {
			System.err.println("File Not Found Exception with " + path);
			e.printStackTrace();
		}
		catch (IOException e) {
			System.err.println("I/O Error with " + path);
			e.printStackTrace();
		}
		this.decodeProblem(content);
	}
}

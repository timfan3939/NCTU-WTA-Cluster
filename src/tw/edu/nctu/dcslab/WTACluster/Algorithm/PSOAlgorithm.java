package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;

class PSOAlgorithm extends HeuristicInterface {
	private Random rand = new Random();

	private int population;

	public PSOAlgorithm( ProblemInterface problem, int population ) {
		super.setProblemInterface( problem );

		this.population = population;
	}

	@Override
	public void doIteration() {
	}

	@Override
	public boolean setAlgorithmParameter( String str ) {
		return true;
	}
}



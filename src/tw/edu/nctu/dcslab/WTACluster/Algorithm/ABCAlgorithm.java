package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.Random;
import java.util.ArrayList;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;

public class ABCAlgorithm extends HeuristicInterface {
	private Random rand = new Random();

	private int population;

	private ArrayList<FoodSource> foodSource;
	private int trialLimit = 1400;

	private FoodSource bestFoodSource;

	public ABCAlgorithm( ProblemInterface problem, int population ) {
		super.setProblemInterface( problem );
		this.population = population;

		this.foodSource = new ArrayList<FoodSource>();
		for( int i=0; i<this.population; i++ ) {
			double[] pos = new double[ this.solutionLength ];
			for( int p=0; p<this.solutionLength; p++ ) {
				pos[p] = rand.nextDouble() * this.solutionValueMax;
			}
			this.foodSource.add( new FoodSource( this.problem, pos, this.trialLimit ) );
		}
 		this.bestFoodSource = this.foodSource.get(0).clone();
		this.bestSolution = this.bestFoodSource.getPos().clone();
	}

	@Override
	public void doIteration() {
		this.employedBeesPhase();
		this.onlookerBeesPhase();
		this.scoutBeesPhase();
		this.updateSolution();
	}

	private void updateFoodSource(int A) {
			int B = rand.nextInt( this.population );
			while( A == B ) {
				B = rand.nextInt( this.population );
			}

			double bias = 2.0 * ( rand.nextDouble() - 0.5 ) ;
			int dimension = rand.nextInt( this.solutionLength );
			
			FoodSource fsA = this.foodSource.get(A);
			FoodSource newFS = fsA.clone();
			FoodSource fsB = this.foodSource.get(B);

			double[] pos = newFS.getPos();
			pos[ dimension ] += bias * ( pos[ dimension ] - fsB.getPos()[ dimension ] );
			
			if( pos[ dimension ] < 0 )
				pos[ dimension ] = - pos[ dimension ];
			else if ( pos[ dimension ] > this.solutionValueMax )
				pos[ dimension ] = 2.0 * this.solutionValueMax - pos[ dimension ];
			else
				pos[ dimension ] -= 0.01;
			newFS.setPos(pos);
			
			if( fsA.compareTo(newFS) > 0 ) {
				fsA.setPos( pos );
			}
	}

	private void employedBeesPhase() {
		for( int A=0; A<this.foodSource.size(); A++ ) {
			this.updateFoodSource( A );
		}
	}

	private void onlookerBeesPhase() {
		double totalValue = 0.0;
		double[] value = new double[ this.population ];
		for( int i=0; i<this.foodSource.size(); i++ ) {
			value[i] = 1.0/foodSource.get(i).getValue();
			totalValue += value[i];
		}

		for( int i=0; i<this.foodSource.size(); i++ ) {
			double chosen = rand.nextDouble();
			for( int j=0; j<value.length; j++ ) {
				chosen -= value[j]/totalValue;
				if( chosen < 0.0 ) {
					this.updateFoodSource( j );
					break;
				}
			}
		}
	}

	private void scoutBeesPhase() {
		for( int i=0; i<this.foodSource.size(); i++ ) {
			this.foodSource.get(i).decreaseTrial();
			if( !this.foodSource.get(i).hasTrial() ) {
				double[] pos = new double[ this.solutionLength ];
				for( int j=0; j<pos.length; j++ )
					pos[j] = rand.nextDouble() * this.solutionValueMax;
				this.foodSource.get(i).setPos( pos );
			}
		}
	}

	private void updateSolution() {
		for( FoodSource fs: this.foodSource ) {
			if( this.bestFoodSource.compareTo( fs ) > 0 )
				this.bestFoodSource = fs.clone();
		}

		boolean modified = false;
		
		synchronized( this.exchangedSolution ) {
			for( double[] sol: this.exchangedSolution ) {
				FoodSource fs = new FoodSource( this.problem, sol, this.trialLimit );
				if( this.bestFoodSource.compareTo( fs ) > 0 ) {
					this.bestFoodSource = fs;
					modified = true;
				}
			}
		}

		if( modified ) {
			FoodSource fs = this.foodSource.get(0);
			for( int i=0; i<this.foodSource.size(); i++ ) {
				if( fs.getRemainTrial() > this.foodSource.get(i).getRemainTrial() )
					fs = this.foodSource.get(i);
			}
			fs.setPos( this.bestFoodSource.getPos() );
		}

		this.bestSolution = this.bestFoodSource.getPos().clone();
	}

	@Override
	public boolean setAlgorithmParameter( String str ) {
		String[] line = str.split("\n");
		String[] subline;
		
		for( int i=0; i<line.length; i++ ) {
			subline = line[i].split(":");
			if( subline.length != 2 ) {
				continue;
			}
			switch( subline[0] ) {
				case "TrialLimit":
					this.trialLimit = Integer.parseInt( subline[1] );
					break;
				default:
					break;
			}
		}

		return false;
	}

	private class FoodSource implements Cloneable, Comparable<FoodSource>{
		private double[] pos;
		private double value;
		private ProblemInterface problem;
		private int defaultTrial;
		private int remainTrial;
		
		public FoodSource( ProblemInterface problem, double[] pos, int defaultTrial ) {
			this.problem = problem;
			this.pos = pos.clone();
			this.value = this.problem.fitnessFunction( this.pos );
			this.defaultTrial = this.remainTrial = defaultTrial;
		}

		public void setPos( double[] pos ) {
			this.pos = pos.clone();
			this.value = this.problem.fitnessFunction( this.pos );
			this.remainTrial = this.defaultTrial;
		}

		public double[] getPos() {
			return this.pos;
		}

		public double getValue() {
			return this.value;
		}

		public void decreaseTrial() {
			this.remainTrial--;
		}

		public boolean hasTrial() {
			return this.remainTrial > 0;
		}

		public int getRemainTrial() {
			return this.remainTrial;
		}

		private FoodSource(){};

		@Override
		public FoodSource clone() {
			FoodSource fs = new FoodSource();
			fs.problem = this.problem;
			fs.pos = this.pos.clone();
			fs.value = this.value;
			fs.defaultTrial = this.defaultTrial;
			fs.remainTrial = this.remainTrial;

			return fs;
		}

		@Override
		public int compareTo( FoodSource fs ) {
			if(this.value > fs.value) 
				return 1;
			else if( this.value < fs.value )
				return -1;
			else {
				for( int i=0; i<this.pos.length; i++ ) {
					if( (int)this.pos[i] - (int)fs.pos[i] != 0 )
						return (int)this.pos[i] - (int)fs.pos[i];
				}
			}
			return 0;
		}
	}
}



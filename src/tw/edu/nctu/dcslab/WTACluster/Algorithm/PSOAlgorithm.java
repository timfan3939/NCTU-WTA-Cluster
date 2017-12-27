package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;

public class PSOAlgorithm extends HeuristicInterface {
	private Random rand = new Random();

	private double[] gloBestPos;
	private double gloBestPosValue;

	private ArrayList<Particle> particles = new ArrayList<Particle>();

	private int population;
	private double omega = -0.4438;
	private double c1 = -0.2699;
	private double c2 = 3.395;

	public PSOAlgorithm( ProblemInterface problem, int population ) {
		super.setProblemInterface( problem );

		this.population = population;
		this.generateParticles();

		this.gloBestPos = new double[ this.solutionLength ];
		for( int i=0; i<this.gloBestPos.length; i++ )
			this.gloBestPos[i] = rand.nextDouble() * this.solutionValueMax;

		this.gloBestPosValue = this.problem.fitnessFunction( this.gloBestPos );
	}

	private void generateParticles() {
		while( this.particles.size() < this.population ) {
			this.particles.add( new Particle( this.problem, this.rand ) );
		}
	}

	@Override
	public void doIteration() {
		double[] exSol = null;
		synchronized( this.exchangedSolution ) {
			for( double[] sol: this.exchangedSolution ) {
				if( this.gloBestPosValue >
				    this.problem.fitnessFunction( sol ) ) {
					exSol = sol;
				}
			}
			this.exchangedSolution.clear();
		}
		if( exSol != null ) {
			this.gloBestPos = exSol.clone();
			this.gloBestPosValue = this.problem.fitnessFunction( this.gloBestPos );
		}

		for( Particle p: this.particles ) {
			p.update( this.rand,
			          this.omega,
					  this.c1,
					  this.c2,
					  this.gloBestPos );
			if ( this.gloBestPosValue > p.getBestPosValue() ) {
				this.gloBestPos = p.getBestPos().clone();
				this.gloBestPosValue = p.getBestPosValue();
			}
			//System.out.println("" + p.getBestPosValue());
		}
		this.bestSolution = this.gloBestPos;
	}

	@Override
	public boolean setAlgorithmParameter( String str ) {
		String[] line = str.split("\n");
		String[] subLine = null;
		
		for( int i=0; i<line.length; i++ ) {
			subLine = line[i].split(":");
			if( subLine.length != 2 )
				continue;

			switch( subLine[0] ) {
				case "Omega":
					this.omega = Double.parseDouble( subLine[1] );
					break;
				case "C1":
					this.c1 = Double.parseDouble( subLine[1] );
					break;
				case "C2":
					this.c2 = Double.parseDouble( subLine[1] );
					break;
				default:
					break;
			}
		}

		return false;
	}

	private class Particle {
		private double[] pos;
		private double posValue;
		private double[] bestPos;
		private double bestPosValue;
		private double[] velocity;

		private ProblemInterface problem;

		public Particle(ProblemInterface problem, Random rand) {
			this.problem = problem;

			this.pos = new double[ this.problem.getSolutionLength() ];
			for( int i=0; i<this.pos.length; i++ )
				this.pos[i] = rand.nextDouble() * this.problem.getSolutionMax();

			this.bestPos = this.pos.clone();
			this.posValue = this.bestPosValue = this.problem.fitnessFunction( this.pos );

			this.velocity = new double[ this.problem.getSolutionLength() ];
			for( int i=0; i<this.velocity.length; i++ ) 
				this.velocity[i] = rand.nextDouble();
		}

		public double[] getPos () {
			return this.pos;
		}

		public double[] getBestPos() {
			return this.bestPos;
		}

		public double getBestPosValue() {
			return this.bestPosValue;
		}

		public double[] getVelocity() {
			return this.velocity;
		}

		public void update( Random rand, double omega, double c1, double c2, double[] gloBestPos ) {
			if( gloBestPos.length != this.problem.getSolutionLength() ) {
				System.err.println( "The length of the position is not corrent." );
				return;
			}

			double r1 = rand.nextDouble();
			double r2 = rand.nextDouble();

			for( int i=0; i<this.velocity.length; i++ ) {
				this.velocity[i] = omega * this.velocity[i]
								 + c1 * r1 * ( this.bestPos[i] - this.pos[i] )
								 + c2 * r2 * (   gloBestPos[i] - this.pos[i] );
				while( this.velocity[i] > this.problem.getSolutionMax() || this.velocity[i] < -this.problem.getSolutionMax() ) {
					this.velocity[i] /= 2;
				}
				this.pos[i] += this.velocity[i];

				if ( this.pos[i] < 0.0 ) {
					this.pos[i] = 0.0;
				}
				else if ( this.pos[i] > this.problem.getSolutionMax() ) {
					this.pos[i] = 2*this.problem.getSolutionMax() - this.pos[i];
					this.velocity[i] *= -1.0;
				}
				else {
					this.pos[i] -= 0.01;
				}
			}

			this.posValue = this.problem.fitnessFunction( this.pos );
			
			if ( this.bestPosValue > this.posValue ) {
				this.bestPos = this.pos.clone();
				this.bestPosValue = this.posValue;
			}
			//System.out.println( Arrays.toString( this.pos ) );
			//System.out.println( Arrays.toString( this.velocity ) );
		}
	}
}



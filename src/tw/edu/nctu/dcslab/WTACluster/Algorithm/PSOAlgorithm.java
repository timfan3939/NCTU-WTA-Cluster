package tw.edu.nctu.dcslab.WTACluster.Algorithm;

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

import tw.edu.nctu.dcslab.WTACluster.Problem.ProblemInterface;

public class PSOAlgorithm extends HeuristicInterface {
	private Random rand = new Random();

	private Particle gloBest;

	private ArrayList<Particle> particles = new ArrayList<Particle>();

	private int population;
	private double omega = -0.4438;
	private double c1 = -0.2699;
	private double c2 = 3.395;

	public PSOAlgorithm( ProblemInterface problem, int population ) {
		super.setProblemInterface( problem );

		this.population = population;
		this.generateParticles();

		this.gloBest = new Particle( problem, rand );
	}

	private void generateParticles() {
		while( this.particles.size() < this.population ) {
			this.particles.add( new Particle( this.problem, this.rand ) );
		}
	}

	@Override
	public void doIteration() {
		Particle tmpParticle = this.gloBest.clone();
		synchronized( this.exchangedSolution ) {
			for( double[] sol: this.exchangedSolution ) {
				tmpParticle.setPos( sol );
				if( this.gloBest.compareTo( tmpParticle ) > 0 )
					this.gloBest = tmpParticle.clone();
			}
			this.exchangedSolution.clear();
		}

		for( Particle p: this.particles ) {
			p.update( this.rand,
			          this.omega,
					  this.c1,
					  this.c2,
					  this.gloBest );
			if ( this.gloBest.compareTo( p ) > 0 )
				this.gloBest = p.clone();
			//System.out.println("" + p.getBestPosValue());
		}
		this.bestSolution = this.gloBest.getPos().clone();
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

	private class Particle implements Comparable<Particle>, Cloneable{
		private double[] pos;
		private double posValue;
		private double[] bestPos;
		private double bestPosValue;
		private double[] velocity;

		private ProblemInterface problem;

		private Particle() {
		}

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

		public void setPos( double[] pos ) {
			this.pos = pos.clone();
			this.posValue = this.problem.fitnessFunction( this.pos );
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

		public void update( Random rand, double omega, double c1, double c2, Particle gloBest ) {

			double r1 = rand.nextDouble();
			double r2 = rand.nextDouble();

			for( int i=0; i<this.velocity.length; i++ ) {
				this.velocity[i] = omega * this.velocity[i]
								 + c1 * r1 * ( this.bestPos[i] - this.pos[i] )
								 + c2 * r2 * (  gloBest.pos[i] - this.pos[i] );
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
		
		@Override
		public int compareTo( Particle p ) {
			if( this.posValue > p.posValue ) {
				return 1;
			} else if( this.posValue < p.posValue ) {
				return -1;
			} else {
				for( int i=0; i<this.pos.length; i++ ) {
					if( (int)this.pos[i] - (int)p.pos[i] != 0 ) {
						return (int)this.pos[i] - (int)p.pos[i]; 
					}
				}
			}
			return 0;
		}

		@Override
		public Particle clone() {
			Particle p = new Particle();
			
			p.problem = this.problem;
			p.pos = this.pos.clone();
			p.posValue = this.posValue;
			p.bestPos = this.bestPos.clone();
			p.bestPosValue = this.bestPosValue;
			p.velocity = this.velocity.clone();

			return p;
		}
	}
}



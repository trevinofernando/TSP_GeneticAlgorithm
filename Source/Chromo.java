
/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Chromo implements Comparable<Chromo> {
	/*******************************************************************************
	 * INSTANCE VARIABLES *
	 *******************************************************************************/

	public int chromo[];
	public double rawFitness; // evaluated
	public double sclFitness; // scaled
	public double proFitness; // proportionalized

	/*******************************************************************************
	 * INSTANCE VARIABLES *
	 *******************************************************************************/

	private static double randnum;

	/*******************************************************************************
	 * CONSTRUCTORS *
	 *******************************************************************************/

	public Chromo() {
		chromo = new int[Parameters.numGenes+1];
		for (int i = 1; i < Parameters.numGenes+1; i++)
			chromo[i] = i;
		//Knuth's adaptation of Fisher-Yates shuffle, source wikipedia
		int toSwap;
		for (int i = Parameter.numGenes; i>1; i--){
			toSwap = Search.r.nextDouble() * (i+1);
			if (toSwap != i+1)
				toSwap++;
			int temp = chromo[toSwap];
			chromo[toSwap] = chromo[i];
			chromo[i] = temp;
		}
		this.rawFitness = -1; // Fitness not yet evaluated
		this.sclFitness = -1; // Fitness not yet scaled
		this.proFitness = -1; // Fitness not yet proportionalized
	}

	/*******************************************************************************
	 * MEMBER METHODS *
	 *******************************************************************************/

	@Override
	public int compareTo(Chromo other) {
		if (this.proFitness > other.proFitness) {
			return 1;
		} else if (this.proFitness < other.proFitness) {
			return -1;
		}
		return 0;
	}

	// Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation() {

		String mutChromo = "";
		char x;

		switch (Parameters.mutationType) {

			case 1: // Replace with new random number

				for (int j = 0; j < (Parameters.geneSize * Parameters.numGenes); j++) {
					x = this.chromo.charAt(j);
					randnum = Search.r.nextDouble();
					if (randnum < Parameters.mutationRate) {
						if (x == '1')
							x = '0';
						else
							x = '1';
					}
					mutChromo = mutChromo + x;
				}
				this.chromo = mutChromo;
				break;
			case 2:
				if (Search.r.nextDouble() < Parameters.mutationRate){
					int oldLoc = Search.r.nextDouble() * Parameters.numGenes;
					if (oldLoc != Parameters.numGenes)
						oldLoc++;
					int newLoc = Search.r.nextDouble() * Parameters.numGenes;
					if (newLoc != Parameters.numGenes)
						newLoc++;
					int city = chromo[oldLoc];
					for (int i = oldLoc; i != newLoc; i = (i==Parameters.numGenes)?1:i++)
						chromo[i] = chromo[(i+1)%Parameters.numGenes + 1];
					chromo[newLoc] = city;
				}
				break;
			case 3:
				if (Search.r.nextDouble() < Parameters.mutationRate){
					int windowSize = Parameters.geneSize * (Search.r.nextDouble() * (Parameters.DMWindowEnd - Parameters.DMWindowBegin) + Parameters.DMWindowBegin);
					if (windowSize < Parameters.geneSize){
						int windowLoc = Paramters.Sesarch.r.nextDouble() * (Parameters.geneSize - windowSize + 1);
						if (windowLoc != Parameters.geneSize - windowSize + 1)
							windowLoc++;
						int newLoc = Parameters.Search.r.nextDouble() * Parameters.geneSize;
						if (newLoc != Parameters.geneSize)
							newLoc++;
						int window[] = new int[windowSize];
						for (int i = 0; i<windowSize; i++)
							window[i] = chromo[(i+windowLoc > Parameters.geneSize)? (i+windowLoc) % Parameters.geneSize + 1: i+windowLoc]; 
						int temp;
						for (int i = windowLoc; i != newLoc; i = (i==Parameters.numGenes)?1:i++){
							temp = chromo[i];
							chromo[i] = chromo[(i+windowSize > Parameters.geneSize)? (i+windowSize) % Parameters.geneSize +1: i+windowSize];
							chromo[(i+windowSize > Parameters.geneSize)? (i+windowSize) % Parameters.geneSize +1: i+windowSize] = temp;
						}
						for (int i = 0; i<windowSize; i++)
							chromo[(i+newLoc > Parameters.geneSize)? (i+newLoc) % Parameters.geneSize + 1: i+newLoc] = window[i];
					}
				break;

			default:
				System.out.println("ERROR - No mutation method selected");
		}
	}

	/*******************************************************************************
	 * STATIC METHODS *
	 *******************************************************************************/

	// Select a parent for crossover ******************************************

	public static int selectParent() {

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType) {

			case 1: // Proportional Selection
				randnum = Search.r.nextDouble();
				for (j = 0; j < Parameters.popSize; j++) {
					rWheel = rWheel + Search.member[j].proFitness;
					if (randnum < rWheel)
						return (j);
				}
				break;

			case 3: // Random Selection
				randnum = Search.r.nextDouble();
				j = (int) (randnum * Parameters.popSize);
				return (j);
			case 2: // Tournament Selection
				int candidate[4], temp;
				for (int i=0; i<4; ++i)
					candidate[i] = (int) (Search.r.nextDouble()*Parameters.popSize);
				for (int i = 3; i > 0; i--) {
					for (int j = 0; j < i; j++) {
						if (Search.member[candidate[j]].proFitness > Search.member[candidate[j+1]].proFitness) {
							temp = candidatej[j];
							candidate[j] = candidate[j + 1];
							candidate[j + 1] = temp;					}
					}
				}
				for (int i = 0; i<3; i++)
					if (Search.r.nextDouble()<0.6)
						return candidate[i];
				return candidate[3];


				/*case 4: // Rank Selection
				  Arrays.sort(Search.member);
				  randnum = Search.r.nextDouble();
				  k = (int) (randnum * ((Parameters.popSize * (Parameters.popSize + 1)) / 2));
				  for (j = 0; j < Parameters.popSize; j++) {
				  rWheel = rWheel + j + 1;
				  if (k < rWheel)
				  return (j);
				  }
				  break;*/

			default:
				System.out.println("ERROR - No selection method selected");
		}
		return (-1);
	}

	// Produce a new child from two parents **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2) {

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType) {

			case 1: // Single Point Crossover

				// Select crossover point
				xoverPoint1 = 1 + (int) (Search.r.nextDouble() * (Parameters.numGenes * Parameters.geneSize - 1));

				// Create child chromosome from parental material
				child1.chromo = parent1.chromo.substring(0, xoverPoint1) + parent2.chromo.substring(xoverPoint1);
				child2.chromo = parent2.chromo.substring(0, xoverPoint1) + parent1.chromo.substring(xoverPoint1);
				break;

			case 2: // Two Point Crossover

			case 3: // Uniform Crossover

			default:
				System.out.println("ERROR - Bad crossover method selected");
		}

		// Set fitness values back to zero
		child1.rawFitness = -1; // Fitness not yet evaluated
		child1.sclFitness = -1; // Fitness not yet scaled
		child1.proFitness = -1; // Fitness not yet proportionalized
		child2.rawFitness = -1; // Fitness not yet evaluated
		child2.sclFitness = -1; // Fitness not yet scaled
		child2.proFitness = -1; // Fitness not yet proportionalized
	}

	// Produce a new child from a single parent ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child) {

		// Create child chromosome from parental material
		child.chromo = parent.chromo;

		// Set fitness values back to zero
		child.rawFitness = -1; // Fitness not yet evaluated
		child.sclFitness = -1; // Fitness not yet scaled
		child.proFitness = -1; // Fitness not yet proportionalized
	}

	// Copy one chromosome to another ***************************************

	public static void copyB2A(Chromo targetA, Chromo sourceB) {

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

} // End of Chromo.java ******************************************************

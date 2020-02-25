
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

	public ArrayList<Integer> chromo;
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

		// set to a random permutation

		chromo = new ArrayList<Integer>(Parameters.numGenes);
		for (int i = 0; i < Parameters.numGenes; i++) {
			chromo.add(i);
		}

		Collections.shuffle(chromo);

		chromo = convertToOrdinal(chromo);

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

		switch (Parameters.mutationType) {

		case 1: // Swap
			if (Search.r.nextDouble() < Parameters.mutationRate){

				int maxTry = 5;

				ArrayList<Integer> copy = new ArrayList<Integer>(chromo);

				for (int i = 0; i < maxTry; i++) {
					try {
						int point1 = Search.r.nextInt(Parameters.numGenes);
						int point2;
						do {				
							point2 = Search.r.nextInt(Parameters.numGenes);				
						} while (point1 == point2);
						
						int tmp;
						tmp = chromo.get(point1);
						chromo.set(point1, chromo.get(point2));
						chromo.set(point2, tmp);
						//convertToPath(chromo);
						Search.problem.doRawFitness(this);
						break;
					} catch (Exception e) {
						chromo = copy;
					}
					
				}
				
				

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
			int temp;
			int candidate[] = new int[4];
			for (int i = 0; i < 4; ++i)
				candidate[i] = (int) (Search.r.nextDouble() * Parameters.popSize);
			for (int i = 3; i > 0; i--) {
				for (j = 0; j < i; j++) {
					if (Search.member[candidate[j]].proFitness > Search.member[candidate[j + 1]].proFitness) {
						temp = candidate[j];
						candidate[j] = candidate[j + 1];
						candidate[j + 1] = temp;
					}
				}
			}
			for (int i = 0; i < 3; i++)
				if (Search.r.nextDouble() < 0.6)
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

		switch (Parameters.xoverType) {
			
		case 1: // Single Point Crossover
			
			int xoverPoint = Search.r.nextInt(Parameters.numGenes-1);
			
			for (int i = 0; i < Parameters.numGenes; i++) {
				if (i <= xoverPoint) {
					child1.chromo.set(i, parent1.chromo.get(i));
					child2.chromo.set(i, parent2.chromo.get(i));
				} else {
					child1.chromo.set(i, parent2.chromo.get(i));
					child2.chromo.set(i, parent1.chromo.get(i));					
				}
			}
			
			break;
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
		child.chromo = new ArrayList<Integer>(parent.chromo);

		// Set fitness values back to zero
		child.rawFitness = -1; // Fitness not yet evaluated
		child.sclFitness = -1; // Fitness not yet scaled
		child.proFitness = -1; // Fitness not yet proportionalized
	}

	// Copy one chromosome to another ***************************************

	public static void copyB2A(Chromo targetA, Chromo sourceB) {

		targetA.chromo = new ArrayList<Integer>(sourceB.chromo);

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

	public static ArrayList<Integer> convertToPath(ArrayList<Integer> ordRep) {
		ArrayList<Integer> canonic = new ArrayList<Integer>(Parameters.canonicTour);
		ArrayList<Integer> pathRep = new ArrayList<Integer>(Parameters.canonicTour.size());
		
		for (int i = 0; i < ordRep.size(); i++) {	
			int index = ordRep.get(i);		
			pathRep.add(canonic.get(index));
			canonic.remove(index);
		}		

		return pathRep;
	}

	public static ArrayList<Integer> convertToOrdinal(ArrayList<Integer> pathRep) {
		ArrayList<Integer> canonic = new ArrayList<Integer>(Parameters.canonicTour);
		ArrayList<Integer> ordinal = new ArrayList<Integer>(Parameters.canonicTour.size());
		
		for (int i = 0; i < pathRep.size(); i++) {	
			int index = canonic.indexOf(pathRep.get(i));		
			ordinal.add(index);
			canonic.remove(index);
		}		

		return ordinal;
	}

} // End of Chromo.java ******************************************************


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

	public List<Integer> chromo;
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
			chromo.add(i+1);
		}

		Collections.shuffle(chromo);

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

		case 1:
		// TODO it sees this operation generates some invalid solutions
			if (Search.r.nextDouble() < Parameters.mutationRate){
				int oldLoc = Search.r.nextInt(Parameters.numGenes);
				int newLoc = oldLoc;
				
				while (newLoc == oldLoc)
					newLoc = Search.r.nextInt(Parameters.numGenes);
				
				int city = chromo.get(oldLoc);
				
				for (int i = oldLoc; i != newLoc; i = (++i) % Parameters.numGenes)
					chromo.set((i+1)%Parameters.numGenes, chromo.get(i));
				
				chromo.set(newLoc, city);
			}
			break;
		case 2:
			if (Search.r.nextDouble() < Parameters.mutationRate){
				int windowSize;
				//TODO this part needs review DMWindowBegin and DMWindowEnd are double, but window size is int
				do {
					windowSize = Search.r.nextInt((int)(Parameters.numGenes*(Parameters.DMWindowEnd-Parameters.DMWindowBegin))) + (int)Parameters.DMWindowBegin;
				} while (windowSize >= Parameters.geneSize || windowSize == 0);
										

				int windowLoc = Search.r.nextInt(Parameters.numGenes);

				int newLoc = Search.r.nextInt(Parameters.numGenes);
				
				int window[] = new int[windowSize];
				for (int i = 0; i<windowSize; i++)
					window[i] = chromo.get((windowLoc+i)%Parameters.numGenes); 
				int temp;
				for (int i = windowLoc; i != newLoc; i++){
					if (i==Parameters.numGenes) {
						i = 0;
					}
					temp = chromo.get(i);
					//TODO I get java.lang.IndexOutOfBoundsException here
					chromo.set(i, chromo.get(i+windowSize)%Parameters.numGenes);
					chromo.set((i+windowSize)%Parameters.numGenes, temp);
				}
				
				for (int i = 0; i<windowSize; i++)
					chromo.set((i+newLoc)%Parameters.numGenes, window[i]);
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

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType) {

		case 1: // Order Crossover (OX1)
			
			do {				
				xoverPoint1 = Search.r.nextInt(Parameters.numGenes);
				xoverPoint2 = Search.r.nextInt(Parameters.numGenes);				
			} while ((xoverPoint1 == xoverPoint2) || (Math.abs(xoverPoint1 - xoverPoint2 + 1) == Parameters.numGenes));
			
			if (xoverPoint1 > xoverPoint2) {
				int tmp;
				tmp = xoverPoint1;
				xoverPoint1 = xoverPoint2;
				xoverPoint2 = tmp;				
			}

			List<Integer> child1_temp = new ArrayList<Integer>(xoverPoint2 - xoverPoint1 + Parameters.numGenes + 1);
			List<Integer> child2_temp = new ArrayList<Integer>(xoverPoint2 - xoverPoint1 + Parameters.numGenes + 1);

			for (int i = xoverPoint1; i < xoverPoint2 + 1; i++) {
				child1_temp.add(parent1.chromo.get(i));
				child2_temp.add(parent2.chromo.get(i));
			}

			for (int i = xoverPoint2 + 1; i < xoverPoint2 + Parameters.numGenes + 1; i++) {
				int index = i % Parameters.numGenes;
				child1_temp.add(parent2.chromo.get(index));
				child2_temp.add(parent1.chromo.get(index));
			}

			LinkedHashSet<Integer> child1_hashset = new LinkedHashSet<>(child1_temp);         
			child1_temp = new ArrayList<>(child1_hashset);
		
			LinkedHashSet<Integer> child2_hashset = new LinkedHashSet<>(child2_temp);         
			child2_temp = new ArrayList<>(child2_hashset);

			for (int i = xoverPoint1; i < xoverPoint1 + Parameters.numGenes; i++) {
				int index = i % Parameters.numGenes;
				child1.chromo.set(index, child1_temp.get(i - xoverPoint1));
				child2.chromo.set(index, child2_temp.get(i - xoverPoint1));
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

} // End of Chromo.java ******************************************************


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

		chromo = convertToAdj(chromo);

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
			if (Search.r.nextDouble() < Parameters.mutationRate){
				ArrayList<Integer> c = Chromo.convertToPath(chromo);

				int point1;
				int point2;
				do {				
					point1 = Search.r.nextInt(Parameters.numGenes);
					point2 = Search.r.nextInt(Parameters.numGenes);				
				} while ((point1 == point2) || (Math.abs(point1 - point2 + 1) == Parameters.numGenes));
				
				if (point1 > point2) {
					int tmp;
					tmp = point1;
					point1 = point2;
					point2 = tmp;				
				}

				ArrayList<Integer> subtour = new ArrayList<Integer>(point2 - point1 + 1);
				
				for (int i = point1; i < point2 + 1; i++) {
					subtour.add(c.get(i));
				}

				c.subList(point1, point2 + 1).clear();

				int insertPoint = Search.r.nextInt(c.size() + 1);

				if (insertPoint == c.size()) {
					c.addAll(subtour);
				} else {
					c.addAll(insertPoint, subtour);
				}
				
				chromo = Chromo.convertToAdj(c);

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
			
		case 1: // Genetic Edge Recombination Crossover (ER)
			
			edgeRecombination(parent1, parent2, child1);
			edgeRecombination(parent1, parent2, child2);
			
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

	private static void edgeRecombination(Chromo parent1, Chromo parent2, Chromo child) {
		
		ArrayList<Integer> p1 = Chromo.convertToPath(parent1.chromo);
		ArrayList<Integer> p2 = Chromo.convertToPath(parent2.chromo);

		HashMap<Integer, HashSet<Integer>> edgeMap = new HashMap<Integer, HashSet<Integer>>(Parameters.numGenes);
		for (int i = 0; i < Parameters.numGenes; i++) {
			HashSet<Integer> hash = new HashSet<Integer>();
			int index = p1.indexOf(i);
			hash.add(p1.get((index-1+Parameters.numGenes) % Parameters.numGenes));
			hash.add(p1.get((index+1+Parameters.numGenes) % Parameters.numGenes));
			index = p2.indexOf(i);
			hash.add(p2.get((index-1+Parameters.numGenes) % Parameters.numGenes));
			hash.add(p2.get((index+1+Parameters.numGenes) % Parameters.numGenes));
			edgeMap.put(i, hash);
		}

		HashSet<Integer> candidates = new HashSet<Integer>();
		candidates.add(p1.get(0));
		candidates.add(p2.get(0));

		HashMap<Integer, HashSet<Integer>> candidateEdgeMap = edgeMap;
		int childIndex = 0;

		do {

			candidateEdgeMap = new HashMap<Integer, HashSet<Integer>>();
			for (Integer entry : candidates) {
				candidateEdgeMap.put(entry, edgeMap.get(entry));
			}

			Integer currentCity;

			if (candidateEdgeMap.size() == 0) {
				Object[] keys = edgeMap.keySet().toArray();
				currentCity = (Integer) keys[Search.r.nextInt(keys.length)];
			} else {
				ArrayList<Integer> minKeys = new ArrayList<Integer>();
				int minVal = Integer.MAX_VALUE;
				for (HashMap.Entry<Integer, HashSet<Integer>> entry : candidateEdgeMap.entrySet()) {
					int edgeMapSize = entry.getValue().size();
					if (edgeMapSize < minVal) {
						minVal = edgeMapSize;
						minKeys.clear();
						minKeys.add(entry.getKey());
					} else if (edgeMapSize == minVal) {
						minKeys.add(entry.getKey());
					}
				}
				currentCity = minKeys.get(Search.r.nextInt(minKeys.size()));
			}
			
			child.chromo.set(childIndex, currentCity);
			childIndex ++;
			candidates = edgeMap.get(currentCity);
			edgeMap.remove(currentCity);

			for (HashMap.Entry<Integer, HashSet<Integer>> entry : edgeMap.entrySet()) {
				HashSet<Integer> hash = entry.getValue();
				hash.remove(currentCity);
			}

		} while (edgeMap.size() > 0);

		child.chromo = Chromo.convertToAdj(child.chromo);
		
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

	public static ArrayList<Integer> convertToPath(ArrayList<Integer> adjRep) {
		ArrayList<Integer> pathRep = new ArrayList<Integer>(adjRep);		
		int nextIndex = 0;
		pathRep.set(0, 0);
		for (int i = 1; i < Parameters.numGenes; i++) {			
			pathRep.set(i, adjRep.get(nextIndex));
			nextIndex = adjRep.get(nextIndex);
		}

		return pathRep;
	}

	public static ArrayList<Integer> convertToAdj(ArrayList<Integer> pathRep) {
		ArrayList<Integer> adjRep = new ArrayList<Integer>(pathRep);		
		int nextIndex = pathRep.get(0);
		for (int i = 1; i < Parameters.numGenes; i++) {			
			adjRep.set(nextIndex, pathRep.get(i));
			nextIndex = pathRep.get(i);
		}		
		adjRep.set(nextIndex, pathRep.get(0));

		return adjRep;
	}

} // End of Chromo.java ******************************************************

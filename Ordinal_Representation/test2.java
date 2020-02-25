import java.io.*;
import java.util.*;
import java.text.*;

public class test2 {
    
		
        
    public static void main(String[] args) throws java.io.IOException {

        Parameters parmValues = new Parameters(args[0]);

        Chromo parent1 = new Chromo();
        Chromo parent2 = new Chromo();
        Chromo child1 = new Chromo();
		Chromo child2 = new Chromo();
		
		parent1.chromo = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 0));
		parent2.chromo = new ArrayList<Integer>(Arrays.asList(4, 3, 0, 2, 5, 1));
		child1.chromo = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
		child2.chromo = new ArrayList<Integer>(Arrays.asList(2, 4, 3, 1, 5, 6));

		Parameters.numGenes = 6;
/* 
		parent1.chromo = new ArrayList<Integer>(Arrays.asList(0, 39, 19, 27, 14, 43, 24, 46, 29, 38, 23, 1, 13, 32, 31, 2, 25, 40, 36, 26, 37, 3, 17, 30, 34, 15, 33, 11, 8, 16, 9, 44, 41, 28, 22, 47, 42, 7, 35, 18, 21, 45, 10, 6, 20, 12, 5, 4));
		parent2.chromo = new ArrayList<Integer>(Arrays.asList(4, 42, 6, 41, 10, 36, 13, 35, 12, 32, 40, 19, 47, 15, 38, 23, 22, 43, 5, 17, 34, 9, 8, 16, 28, 14, 33, 1, 46, 21, 18, 27, 26, 25, 37, 31, 45, 39, 30, 24, 20, 3, 29, 44, 11, 7, 0, 2));
 */
        System.out.println(parent1.chromo);
        System.out.println(parent2.chromo);
	
		System.out.println("---------------------");
		System.out.println(Chromo.convertToPath(parent1.chromo));
		System.out.println(Chromo.convertToPath(parent2.chromo));
		
		edgeRecombination(parent1, parent2, child1);
		edgeRecombination(parent1, parent2, child2);
		


		System.out.println("---------------------");
		System.out.println(child1.chromo);
		System.out.println(child2.chromo);
	
		System.out.println("---------------------");
		System.out.println(Chromo.convertToPath(child1.chromo));
		System.out.println(Chromo.convertToPath(child2.chromo));
        
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

		child.chromo = Chromo.convertToOrdinal(child.chromo);
	}
}

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
		
		parent1.chromo = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
		parent2.chromo = new ArrayList<Integer>(Arrays.asList(2, 4, 3, 1, 5, 6));
		child1.chromo = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
		child2.chromo = new ArrayList<Integer>(Arrays.asList(2, 4, 3, 1, 5, 6));

        System.out.println(parent1.chromo);
        System.out.println(parent2.chromo);
	
		
		edgeRecombination(parent1, parent2, child1);
		edgeRecombination(parent1, parent2, child2);
		


		System.out.println("---------------------");
		System.out.println(child1.chromo);
		System.out.println(child2.chromo);
	
        
	}
	
	private static void edgeRecombination(Chromo parent1, Chromo parent2, Chromo child) {

		HashMap<Integer, HashSet<Integer>> edgeMap = new HashMap<Integer, HashSet<Integer>>(Parameters.numGenes);
		for (int i = 0; i < Parameters.numGenes; i++) {
			HashSet<Integer> hash = new HashSet<Integer>();
			int index = parent1.chromo.indexOf(i+1);
			hash.add(parent1.chromo.get((index-1+Parameters.numGenes) % Parameters.numGenes));
			hash.add(parent1.chromo.get((index+1+Parameters.numGenes) % Parameters.numGenes));
			index = parent2.chromo.indexOf(i+1);
			hash.add(parent2.chromo.get((index-1+Parameters.numGenes) % Parameters.numGenes));
			hash.add(parent2.chromo.get((index+1+Parameters.numGenes) % Parameters.numGenes));
			edgeMap.put(i+1, hash);
		}

		HashSet<Integer> candidates = new HashSet<Integer>();
		candidates.add(parent1.chromo.get(0));
		candidates.add(parent2.chromo.get(0));

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
	}
}

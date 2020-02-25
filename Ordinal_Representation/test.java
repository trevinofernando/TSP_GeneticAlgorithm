import java.io.*;
import java.util.*;
import java.text.*;

public class test {
    
		
        
    public static void main(String[] args) throws java.io.IOException {

        Parameters parmValues = new Parameters(args[0]);
		
        Chromo X = new Chromo();
		X.chromo = new ArrayList<Integer>(Arrays.asList(0,1,4,5,3,2,7,6));
		Parameters.numGenes = 8;
		
		System.out.println(X.chromo);

		
		ArrayList<Integer> ord = Chromo.convertToOrdinal(X.chromo);
		System.out.println(ord);
		System.out.println(Chromo.convertToPath(ord));

		int point1 = Search.r.nextInt(Parameters.numGenes);
		int point2;
		do {				
			point2 = Search.r.nextInt(Parameters.numGenes);				
		} while (point1 == point2);
		
		System.out.println(point1);
		System.out.println(point2);

		int tmp;
		tmp = ord.get(point1);
		ord.set(point1, ord.get(point2));
		ord.set(point2, tmp);

		System.out.println("----------------------");
		System.out.println(ord);
		System.out.println(Chromo.convertToPath(ord));

		
        /* Chromo parent1 = new Chromo();
        Chromo parent2 = new Chromo();
        Chromo child1 = new Chromo();
		Chromo child2 = new Chromo();
		
		parent1.chromo = new ArrayList<Integer>(Arrays.asList(0,0,2,2,1,0,1,0));
		parent2.chromo = new ArrayList<Integer>(Arrays.asList(0,2,0,0,1,0,0,0));
		child1.chromo = new ArrayList<Integer>(parent1.chromo);
		child2.chromo = new ArrayList<Integer>(parent2.chromo);
		Parameters.numGenes = 8;

        System.out.println(parent1.chromo);
        System.out.println(parent2.chromo);
        System.out.println(Chromo.convertToPath(parent1.chromo));
        System.out.println(Chromo.convertToPath(parent2.chromo));
		System.out.println("---------------------");
	
		int xoverPoint = Search.r.nextInt(Parameters.numGenes-1);
			
		System.out.println(xoverPoint);
		
		for (int i = 0; i < Parameters.numGenes; i++) {
			if (i <= xoverPoint) {
				child1.chromo.set(i, parent1.chromo.get(i));
				child2.chromo.set(i, parent2.chromo.get(i));
			} else {
				child1.chromo.set(i, parent2.chromo.get(i));
				child2.chromo.set(i, parent1.chromo.get(i));					
			}
		}

		System.out.println("---------------------");
        System.out.println(child1.chromo);
        System.out.println(child2.chromo);
        System.out.println(Chromo.convertToPath(child1.chromo));
        System.out.println(Chromo.convertToPath(child2.chromo)); */
	
		/* int nextIndex = 0;
		for (int z=0; z<X.chromo.size(); z++){
			System.out.println(nextIndex + "-" + X.chromo.get(nextIndex));
			X.rawFitness += Parameters.distance[nextIndex][X.chromo.get(nextIndex)];
			nextIndex = X.chromo.get(nextIndex);
		} */

/* 
		ArrayList<Integer> chromo = new ArrayList<Integer>(Arrays.asList(4, 3, 0, 2, 5, 1));

		ArrayList<Integer> c = Chromo.convertToPath(chromo);
		
        System.out.println(chromo);
		System.out.println(c);
        
		System.out.println("---------------------");

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

        System.out.println(point1);
		System.out.println(point2);
		System.out.println(insertPoint);

		if (insertPoint == c.size()) {
			c.addAll(subtour);
		} else {
			c.addAll(insertPoint, subtour);
		}
		
		chromo = Chromo.convertToAdj(c);


		System.out.println("---------------------");
		System.out.println(chromo);
		System.out.println(c);
	 */
    }
}

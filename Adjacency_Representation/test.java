import java.io.*;
import java.util.*;
import java.text.*;

public class test {
    
		
        
    public static void main(String[] args) throws java.io.IOException {

        Parameters parmValues = new Parameters(args[0]);
		
		// test adjacency representation
        /* Chromo X = new Chromo();
		X.chromo = new ArrayList<Integer>(Arrays.asList(2,4,6,5,3,7,1,0));

        System.out.println(X.chromo);
	
		int nextIndex = 0;
		for (int z=0; z<X.chromo.size(); z++){
			System.out.println(nextIndex + "-" + X.chromo.get(nextIndex));
			X.rawFitness += Parameters.distance[nextIndex][X.chromo.get(nextIndex)];
			nextIndex = X.chromo.get(nextIndex);
		} */

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
	
    }
}

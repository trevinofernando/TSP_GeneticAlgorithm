import java.io.*;
import java.util.*;
import java.text.*;

public class test {
    
		
        
    public static void main(String[] args) throws java.io.IOException {

        Parameters parmValues = new Parameters(args[0]);
		
		// test adjacency representation
        Chromo X = new Chromo();
		X.chromo = new ArrayList<Integer>(Arrays.asList(2,4,6,5,3,7,1,0));

        System.out.println(X.chromo);
	
		int nextIndex = 0;
		for (int z=0; z<X.chromo.size(); z++){
			System.out.println(nextIndex + "-" + X.chromo.get(nextIndex));
			X.rawFitness += Parameters.distance[nextIndex][X.chromo.get(nextIndex)];
			nextIndex = X.chromo.get(nextIndex);
		}

	
        
    }
}

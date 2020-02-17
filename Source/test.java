import java.io.*;
import java.util.*;
import java.text.*;

public class test {
    
		
        
    public static void main(String[] args) {
        int xoverPoint1;
        int xoverPoint2;
        Random r = new Random();
        Chromo parent1 = new Chromo();
        Chromo parent2 = new Chromo();
        Chromo child1 = new Chromo();
        Chromo child2 = new Chromo();

        System.out.println(parent1.chromo);
        System.out.println(parent2.chromo);
        System.out.println(child1.chromo);
        System.out.println(child2.chromo);

        do {				
            xoverPoint1 = r.nextInt(Parameters.tspSize);
            xoverPoint2 = r.nextInt(Parameters.tspSize);				
        } while ((xoverPoint1 == xoverPoint2) || (Math.abs(xoverPoint1 - xoverPoint2 + 1) == Parameters.tspSize));
        
        if (xoverPoint1 > xoverPoint2) {
            int tmp;
            tmp = xoverPoint1;
            xoverPoint1 = xoverPoint2;
            xoverPoint2 = tmp;				
        }

        List<Integer> child1_temp = new ArrayList<Integer>(xoverPoint2 - xoverPoint1 + Parameters.tspSize + 1);
        List<Integer> child2_temp = new ArrayList<Integer>(xoverPoint2 - xoverPoint1 + Parameters.tspSize + 1);

        for (int i = xoverPoint1; i < xoverPoint2 + 1; i++) {
            child1_temp.add(parent1.chromo.get(i));
            child2_temp.add(parent2.chromo.get(i));
        }

        for (int i = xoverPoint2 + 1; i < xoverPoint2 + Parameters.tspSize + 1; i++) {
            int index = i % Parameters.tspSize;
            child1_temp.add(parent2.chromo.get(index));
            child2_temp.add(parent1.chromo.get(index));
        }

        LinkedHashSet<Integer> child1_hashset = new LinkedHashSet<>(child1_temp);         
        child1_temp = new ArrayList<>(child1_hashset);
    
        LinkedHashSet<Integer> child2_hashset = new LinkedHashSet<>(child2_temp);         
        child2_temp = new ArrayList<>(child2_hashset);

        for (int i = xoverPoint1; i < xoverPoint1 + Parameters.tspSize; i++) {
            int index = i % Parameters.tspSize;
            child1.chromo.set(index, child1_temp.get(i - xoverPoint1));
            child2.chromo.set(index, child2_temp.get(i - xoverPoint1));
        }

        
        System.out.println("---------------------");
        System.out.println(xoverPoint1);
        System.out.println(xoverPoint2);
        System.out.println(child1.chromo);
        System.out.println(child2.chromo);
    }
}
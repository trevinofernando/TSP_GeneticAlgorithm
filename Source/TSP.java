/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;


public class TSP extends FitnessFunction {

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/


/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/


/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

public TSP(){
    name = "Traveling Salesman Problem";
}

/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

public void doRawFitness(TSPChromo X){
    X.rawFitness = 0;
    for (int z=0; z<Parameters.numGenes-1; z++){
        X.rawFitness += Parameters.distance[X.chromo.get(z)][X.chromo.get(z+1)];
    }
    X.rawFitness += Parameters.distance[X.chromo.get(Parameters.numGenes-1)][X.chromo.get(0)];
}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

public void doPrintGenes(TSPChromo X, FileWriter output) throws java.io.IOException{
//TODO
    for (int i=0; i<Parameters.numGenes; i++){
        Hwrite.right(X.chromo.get(i),11,output);
    }
    output.write("   RawFitness");
    output.write("\n        ");
    
    Hwrite.right((int) X.rawFitness,13,output);
    output.write("\n\n");
    return;
}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

    
}
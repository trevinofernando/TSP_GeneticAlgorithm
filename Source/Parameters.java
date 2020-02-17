
/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;

public class Parameters {

	/*******************************************************************************
	 * INSTANCE VARIABLES *
	 *******************************************************************************/

	public static String expID;
	public static String problemType;

	public static String dataInputFileName;

	public static int numRuns;
	public static int generations;
	public static int popSize;

	public static int genCap;
	public static int fitCap;

	public static String minORmax;
	public static int selectType;
	public static int scaleType;

	public static int xoverType;
	public static double xoverRate;
	public static int mutationType;
	public static double mutationRate;

	public static long seed;
	public static int numGenes;
	public static int geneSize;

	public static double distance[][]; // distance[City_A][City_B] == distance[City_B][City_A]

	/*******************************************************************************
	 * CONSTRUCTORS *
	 *******************************************************************************/

	public Parameters(String parmfilename) throws java.io.IOException {

		String readLine;
		BufferedReader parmInput = new BufferedReader(new FileReader(parmfilename));

		expID = parmInput.readLine().substring(30);// Experiment ID
		problemType = parmInput.readLine().substring(30);

		dataInputFileName = parmInput.readLine().substring(30);

		numRuns = Integer.parseInt(parmInput.readLine().substring(30).trim());
		generations = Integer.parseInt(parmInput.readLine().substring(30).trim());// per run
		popSize = Integer.parseInt(parmInput.readLine().substring(30).trim());

		selectType = Integer.parseInt(parmInput.readLine().substring(30).trim());
		// 1 = Proportional Selection
		// 2 = Tournament Selection
		// 3 = Random Selection
		// 4 = Rank Selection

		scaleType = Integer.parseInt(parmInput.readLine().substring(30).trim());// Fitness Scaling Type
		// 0 = Scale for Maximization (no change to raw fitness)
		// 1 = Scale for Minimization (reciprocal of raw fitness)
		// 2 = Rank for Maximization
		// 3 = Rank for Minimization

		xoverType = Integer.parseInt(parmInput.readLine().substring(30).trim());
		// 1 = Single Point Crossover
		// 2 = Two Point Crossover
		// 3 = Uniform Crossover

		xoverRate = Double.parseDouble(parmInput.readLine().substring(30).trim()); // from 0 to 1, Use "0" to turn off
																					// crossover

		mutationType = Integer.parseInt(parmInput.readLine().substring(30).trim());// 1 = Flip Bit

		mutationRate = Double.parseDouble(parmInput.readLine().substring(30).trim());// from 0 to 1, Use "0" to turn off
																						// mutation

		seed = Long.parseLong(parmInput.readLine().substring(30).trim());

		numGenes = Integer.parseInt(parmInput.readLine().substring(30).trim());// in each chromosome.

		geneSize = Integer.parseInt(parmInput.readLine().substring(30).trim());
		// is the number of bits in each gene. Number of Genes times Size
		// gives the number of bits in each chromosome.

		// Read cities
		System.out.println("\nInput File Name is: " + dataInputFileName + "\n");
		try (BufferedReader br = new BufferedReader(new FileReader(dataInputFileName))) {
			String line;
			String dimensionField = "DIMENSION: ";
			int numCities = 0;
			// Find dimension field
			while ((line = br.readLine()) != null) {
				if (line.startsWith(dimensionField)) {
					numCities = Integer.parseInt(line.substring(dimensionField.length()));
					// Skip next 2 lines:
					br.readLine();
					br.readLine();
					break;
				}
			}
			if (numCities == 0) {
				System.out.println("\nFailed to read cities. Please check name of input file\n");
				return;
			} else {
				System.out.println("\n" + numCities + "cities found.\nPlease wait...");
			}
			distance = new double[numCities + 1][numCities + 1]; // city 0 is a ghost city

			String cityInfo[];
			int i = 0, x = 0, y = 1;
			double citiesCoordinates[][] = new double[numCities + 1][2];

			while (i < numCities) {
				line = br.readLine();
				cityInfo = line.split(" ");
				i++;
				citiesCoordinates[i][x] = Double.valueOf(cityInfo[1]); // x coordinate
				citiesCoordinates[i][y] = Double.valueOf(cityInfo[2]); // y coordinate
			}

			// padding city 0 as a ghost city
			for (int j = 1; j <= numCities; j++) {
				for (int k = 1; k <= numCities; k++) {
					distance[j][k] = Distance(citiesCoordinates[j][x], citiesCoordinates[j][y], citiesCoordinates[k][x],
							citiesCoordinates[k][y]);
					// System.out.println("Calculating distance between: City "j + " and " + k +
					// "\n");
				}
			}
			System.out.println("\nDistances between all cities calculated successfully \n");
			// Comment out this line when not debugging
			// System.out.println(Arrays.deepToString(distances));
		}

		parmInput.close();

		if (scaleType == 0 || scaleType == 2)
			minORmax = "max";
		else
			minORmax = "min";

	}

	/*******************************************************************************
	 * MEMBER METHODS *
	 *******************************************************************************/

	/*******************************************************************************
	 * STATIC METHODS *
	 *******************************************************************************/

	public static double Distance(double x1, double y1, double x2, double y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	public static void outputParameters(FileWriter output) throws java.io.IOException {

		output.write("Experiment ID                :  " + expID + "\n");
		output.write("Problem Type                 :  " + problemType + "\n");

		output.write("Data Input File Name         :  " + dataInputFileName + "\n");

		output.write("Number of Runs               :  " + numRuns + "\n");
		output.write("Generations per Run          :  " + generations + "\n");
		output.write("Population Size              :  " + popSize + "\n");

		output.write("Selection Method             :  " + selectType + "\n");
		output.write("Fitness Scaling Type         :  " + scaleType + "\n");
		output.write("Min or Max Fitness           :  " + minORmax + "\n");

		output.write("Crossover Type               :  " + xoverType + "\n");
		output.write("Crossover Rate               :  " + xoverRate + "\n");
		output.write("Mutation Type                :  " + mutationType + "\n");
		output.write("Mutation Rate                :  " + mutationRate + "\n");

		output.write("Random Number Seed           :  " + seed + "\n");
		output.write("Number of Genes/Points       :  " + numGenes + "\n");
		output.write("Size of Genes                :  " + geneSize + "\n");

		output.write("\n\n");

	}
} // End of Parameters.java **************************************************
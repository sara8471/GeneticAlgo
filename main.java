/*N-Queens by Sara Ahmed (8 queens)
 *  Aim - to generate a board whose fitness score is 28 (i.e board where no two queens 
 *          attack each other)
 *  - setup() - generates initial population (random in values for queen position)
 *  - nextGen() - implements fitness function, sorts the population based on their 
 *                      functions, displays the Population 
 */
package p04;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.Random;

public class main {

	// Selection of DNA
	public static DNA selectDNA(DNA[] population) {
		Random random = new Random();
		double val = random.nextDouble();
		double fit = 0;
        
		for (int i = 0; i < population.length; i++) {
			population[i].calcFitness(28);
			fit += population[i].getFitness();
		}

		double value = val * fit;

		for (int i = 0; i < population.length; i++) {
			population[i].calcFitness(28);
			value -= population[i].getFitness();
			if (value <= 0) {
				return population[i];
			}
		}

		return population[population.length - 1];
	}
	// end SelectDNA()

	// Fitness function
	public static void fitnessFunction(DNA[] population, int target) {
		int fit = 0;
		for (int i = 0; i < population.length; i++) {
			population[i].calcFitness(target);
			fit += population[i].getFitness();
		}

		for (int i = 0; i < population.length; i++) {
			population[i].calcFitness(target);
			double fitness = population[i].getFitness() / (double) fit;
			population[i].setfitness(fitness);
		}
	}
	// end fitness function

	// Sort the population based on their fitness score in descending order
	public static void sortPopulation(DNA[] population, int target) {
		int n = population.length;
		for (int i = 0; i < n - 1; i++) {
			for (int j = 0; j < (n - i) - 1; j++) {
				population[j].calcFitness(target);
				population[j + 1].calcFitness(target);
				if (population[j].getFitness() < population[j + 1].getFitness()) {
					DNA temp = population[j];
					population[j] = population[j + 1];
					population[j + 1] = temp;
				}
			}
		}
	}
// end sortPopulation

//Initialize the population based on the given number of population 
	static void setup(DNA[] population, int rows, int cols, int target) {
		for (int i = 0; i < population.length; i++) {
			population[i] = new DNA(rows, cols);

		}
		sortPopulation(population, target);
		displayPopulation(population, 0, target);
	}
// end setup()

	/*
	 * Displays the population, Fitness score of each chromosome, Board score of
	 * each board Calculates and displays the average fitness score of each
	 * generation
	 */
	static void displayPopulation(DNA[] population, int generation, int target) {

		double totalfitness = 0;
		System.out.print("\n");
		DecimalFormat df = new DecimalFormat("#,###,##0.00");
		System.out.print("******  Generation:  " + generation + "  ******" + "\n");

		for (int i = 0; i < population.length; i++) {
			for (int j = 0; j < 8; j++) {
				int[] arr = population[i].getPosArr();
				System.out.print(arr[j] + "  ");
			}
			System.out.println();
			population[i].print_board();
			System.out.println();
			population[i].calcFitness(target);
			System.out.println("Board score: " + population[i].getBoardScore());
			System.out.println("Fitness score: " + df.format(population[i].getFitness()) + "\n");
			totalfitness += population[i].getFitness();
		}

		System.out.print("\nAverage fitness of this generation: ");
		double averageFitness = totalfitness / population.length;
		System.out.print(df.format(averageFitness) + "\n");
	}
// end display population

// Generates a new generation
	static void nextGen(DNA[] population, int targetScore, double mutationRate, int generation) {

		// get the fitness function
		fitnessFunction(population, targetScore);

		// sort the population based on the fitness score
		sortPopulation(population, targetScore);

		// Calculate fitness and display population
		displayPopulation(population, generation, targetScore);

		// Add candidates to matingPool
		ArrayList<DNA> matingPool = new ArrayList<DNA>();

		for (int i = 0; i < population.length; i++) {
			population[i].calcFitness(targetScore);
			// Arbitrary multiplier
			int n = (int) (population[i].getFitness() * 100);
			for (int j = 0; j < n; j++) {
				matingPool.add(population[i]);
			}
		}

		// create next generation
		for (int i = 0; i < population.length; i++) {
			DNA partnerA = selectDNA(population);
			DNA partnerB = selectDNA(population);
			DNA child = partnerA.crossover(partnerB);
			child.mutate(mutationRate);
			// get rid of the chromosome with the least fitness score in the generation
			population[population.length - 1] = child;
		}

	}
	public static void main(String[] args) {

		double mutationRate;
		int totalPopulation;
		int generation = 0;
		final int rows = 8;
		final int cols = 8;
		// This is the fitness score of a board in which no two queens attack each other
		final int targetFitnessscore = 28;

		Scanner input = new Scanner(System.in);
		System.out.print("Enter the mutation rate: ");
		mutationRate = input.nextDouble();
		System.out.print("\nEnter the total population: ");
		totalPopulation = input.nextInt();

		System.out.print("\n");
		DNA[] population = new DNA[totalPopulation];

		setup(population, rows, cols, targetFitnessscore);
		DNA best = population[0];

		for (int i = 0; i < 100; i++) {
			nextGen(population, targetFitnessscore, mutationRate, generation);
			generation++;
		}
	}
}


// A class to describe a psuedo-DNA, i.e. genotype
//   Here, a virtual organism's DNA is an array of integers (i.e position of queens)
//   Functionality:
//	      -- convert DNA into a array of integers representing the board
//		  --  convert the given integer array into a board
//	      -- print board
//        -- Calculate conflicts of queens in the board
//	      -- mate DNA with another set of DNA
//	      -- mutate DNA
//        -- calculate the board score 
//	      -- print DNA and fitness
package p04;

import java.util.*;
import java.lang.*;

public class DNA {
	private int rows;
	private int cols;
	private int[] genes;
	String[][] nBoard;
	private double fitness;
	private int boardscore;
	private int boardsum;

	// Constructor (makes a random DNA)
	DNA(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		genes = new int[8];
		for (int i = 0; i < genes.length; i++) {
			for (int j = 0; j < rows; j++) {
				Random r = new Random();
				int newpos = 1 + r.nextInt(rows);
				genes[j] = newpos;
			}
		}

	}

	// Constructor
	DNA(int rows, int cols, int[] positionArr) {
		this.rows = rows;
		this.cols = cols;
		this.genes = positionArr;
	}

	public int getRows() {
		return this.rows;
	}

	public int getCols() {
		return this.cols;
	}

	public int[] getPosArr() {
		return this.genes;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public void setPosArr(int[] posArry) {
		this.genes = posArry;
	}

	public void setboardScore(int boardScore) {
		this.boardscore = boardScore;
	}

	public int getBoardsum() {
		return this.boardsum;
	}

	public void setboardsum(int boardsum) {
		this.boardsum = boardsum;
	}

	/*
	 * board_converter - converts the int array of integers (i.e positions of
	 * queens) and converts it into a string representation of the board
	 */

	public String[][] board_converter() {
		String[][] myboard = new String[8][8];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				myboard[i][j] = "*";
			}
		}
		int n = 8;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				myboard[n - genes[i]][i] = "Q";

			}
		}
		this.nBoard = myboard;
		return myboard;
	}
	// end board_converter

	public String[][] getBoard() {
		this.nBoard = board_converter();
		return nBoard;
	}

	// print board function gets the board of the DNA and prints it
	public void print_board() {

		String[][] board = getBoard();
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {

				System.out.print(board[i][j] + "  ");
			}
			System.out.println();
		}
	}
	// end print_board()

	public double getFitness() {
		return fitness;
	}

	public void setfitness(double fitness) {
		this.fitness = fitness;
	}

	// calculate the number of conflicts of each queen on the chessboard
	public int computeConflicts(int qposrow, int qposcol) {
		String[][] board = getBoard();

		int conflicts = 0;
		int counter = 8 - qposcol;
		boolean found1 = false;

		int conflicth = 0;
		int col = qposcol - 1;

		// check horizontally (forward)
		for (int i = 0; i < counter; i++) {
			col++;
			if (col <= 7 && board[8 - (qposrow)][col] == "Q") {
				found1 = true;
			}
			if (found1) {
				conflicth++;
			}
			found1 = false;
		}

		// check upward diagonal
		int conflictdiaup = 0;
		boolean found2 = false;

		int temp1 = 8 - qposrow;
		int temp2 = qposcol;

		temp1--;

		for (int i = 0; i < counter; i++) {

			if (temp1 >= 0 && temp2 <= 7 && board[temp1][temp2] == "Q") {
				found2 = true;
			}
			temp1--;
			temp2++;
			if (found2) {
				conflictdiaup++;
			}
			found2 = false;
		}

		// check downward diagonal
		int conflictdiadown = 0;
		boolean found3 = false;
		int temp3 = 8 - qposrow;
		int temp4 = qposcol - 1;

		for (int i = 0; i < (8 - qposcol); i++) {
			temp3++;
			temp4++;

			if (temp3 <= 7 && temp4 <= 7 && board[temp3][temp4] == "Q") {
				found3 = true;
			}
			if (found3) {

				conflictdiadown++;
			}
			found3 = false;
		}

		conflicts = conflicth + conflictdiaup + conflictdiadown;

		return conflicts;

	}
// end computeConflicts()

	// gets the score of the board
	public int getBoardScore() {

		int var = 8;
		int temp = var;
		int boardscore = 0;

		for (int i = 0; i < genes.length; i++) {
			temp--;
			if (i == 7) {
				temp = 0;
			} else {
				var = temp;
				int my = computeConflicts(genes[i], (i + 1));
				var = var - my;
				boardscore += var;
			}
		}

		return this.boardscore = boardscore;

	}
	// end getBoardScore()

	/*
	 * Fitness function (returns floating point % of "correct" characters)
	 * calcFitness
	 */
	public void calcFitness(double target) {
		this.fitness = ((double) getBoardScore() / target);
	}

	//
	public DNA crossover(DNA partner) {

		// A new child

		DNA child = new DNA(rows, cols);
		int midpoint = (int) (Math.random() * (genes.length));

		for (int i = 0; i < genes.length; i++) {
			if (i > midpoint)
				child.genes[i] = genes[i];
			else
				child.genes[i] = partner.genes[i];
		}
		return child;

	}

	/*
	 * Based on a mutation probability, picks a new integer (i.e new random position
	 * of the queen
	 */
	public void mutate(double mutationRate) {
		Random r = new Random();
		int[] array = { 1, 2, 3, 4, 5, 6, 7, 8 };
		for (int i = 0; i < genes.length; i++) {
			if (Math.random() < mutationRate) {
				genes[i] = array[r.nextInt(8)];
			}
		}
	}

}

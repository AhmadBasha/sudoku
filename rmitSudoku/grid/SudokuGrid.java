/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

 package grid;

 import java.io.*;

import helper.BetterCage;


/**
 * Abstract class representing the general interface for a Sudoku grid.
 * Both standard and Killer Sudoku extend from this abstract class.
 */
public abstract class SudokuGrid
{
	
	protected int[][] grid;
	
	protected int num;
	
	protected int sqrt;
	
	protected int[] symbols;
	
	protected int[][] cageIndex;
	
	protected BetterCage[] cages;
	
	protected int largestSum;
	
	public int[][] getGrid()
	{
		return grid;
	}
	
	public int[] getSymbols()
	{
		return symbols;
	}
	
	public int getNum()
	{
		return num;
	}
	
	public int getSqrt()
	{
		return sqrt;
	}
	
	public void setGrid(int [][] grid)
	{
		this.grid = grid;
	}
	
	public void setGridPos(int i, int j, int val)
	{
		grid[i][j] = val;
	}
	
	public void setNum(int val)
	{
		num = val;
	}
	
	public BetterCage[] getCages()
	{
		return cages;
	}
	
	public int[][] getCageIndex()
	{
		return cageIndex;
	}
	
	public int getLargestSum()
	{
		return largestSum;
	}

    /**
     * Load the specified file and construct an initial grid from the contents
     * of the file.  See assignment specifications and sampleGames to see
     * more details about the format of the input files.
     *
     * @param filename Filename of the file containing the intial configuration
     *                  of the grid we will solve.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void initGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Write out the current values in the grid to file.  This must be implemented
     * in order for your assignment to be evaluated by our testing.
     *
     * @param filename Name of file to write output to.
     *
     * @throws FileNotFoundException If filename is not found.
     * @throws IOException If there are some IO exceptions when openning or closing
     *                  the files.
     */
    public abstract void outputGrid(String filename)
        throws FileNotFoundException, IOException;


    /**
     * Converts grid to a String representation.  Useful for displaying to
     * output streams.
     *
     * @return String representation of the grid.
     */
    public abstract String toString();


    /**
     * Checks and validates whether the current grid satisfies the constraints
     * of the game in question (either standard or Killer Sudoku).  Override to
     * implement game specific checking.
     *
     * @return True if grid satisfies all constraints of the game in question.
     */
    public abstract boolean validate();

} // end of abstract class SudokuGrid

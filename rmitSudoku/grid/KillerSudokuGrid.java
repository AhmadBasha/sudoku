/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.BitSet;
import java.util.Scanner;

import helper.BetterCage;
import helper.Cage;
import helper.Cell;


/**
 * Class implementing the grid for Killer Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task E and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class KillerSudokuGrid extends SudokuGrid
{
	// the number of cages .
	int cagesNum;

    public KillerSudokuGrid() {
        super();

        // TODO: any necessary initialisation at the constructor
    } // end of KillerSudokuGrid()


    /* ********************************************************* */


    @Override
    public void initGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	
		Scanner scanner = new Scanner(new File(filename));
		// first line on the file. 
		this.num = Integer.parseInt(scanner.nextLine());
		// creating the square root. 
		this.sqrt = (int) Math.sqrt(num);
		// making the grid and it is empty grid
		this.grid = new int[num][num];
		// making cage index to know the cells that need to fill with the sum. 
		this.cageIndex = new int[num][num];
		// know the number of symbol. 
		this.symbols = new int[num];
		// here to save the number of the symbols. 
		for (int i = 0; i < num; ++i)
		{
			symbols[i] = scanner.nextInt();
		}
		
		//Assign the number of cages that on the file existing . 
		scanner.nextLine();
		this.cagesNum = Integer.parseInt(scanner.nextLine());
		
		// assign cages with the number of cages
		cages = new BetterCage[cagesNum];
		largestSum = -1;
		
		
		for (int i = 0; i < cagesNum; ++i)
		{
			// taking first line
			String partOne = scanner.nextLine();
			// split it 
			String partOne_split[] = partOne.split(" ");
			// the first number is the sum 
			int sum = Integer.parseInt(partOne_split[0]);
			// taking the sum and length ( taking the length to know the cells that need to be on it) 
			cages[i] = new BetterCage(sum, partOne_split.length - 1);
			// for better cages
			if( sum > largestSum)
			{
				largestSum = sum;
			}
			// split the parts by a comma to take row and cal.
			// here j starts with one becaue if i put zero it will check the sum which is incorrect. 
			for (int j = 1; j < partOne_split.length; ++j)
			{
				
				String index_split[] = partOne_split[j].split(",");
				// assign row and col. 
				int row = Integer.parseInt(index_split[0]);
				int cal = Integer.parseInt(index_split[1]);
				// assign the index that must be equals with sum. 
				cageIndex[row][cal] = i;
				// adding the row & col to know the cells. 
				cages[i].add(row, cal);
			}
		}

		scanner.close();
    } // end of initBoard()


    @Override
    public void outputGrid(String filename)
        throws FileNotFoundException, IOException
    {
		FileWriter myWriter = new FileWriter(filename);
		myWriter.write(this.toString());
		myWriter.close();
    } // end of outputBoard()

    // here taking the current grid and display it with correcting format
    // assign the grid with resStr row by row throw the loop and divid the val by comma.
    @Override
    public String toString() {
		String retStr = "";

		for (int row[] : grid)
		{
			retStr += row[0];

			for (int i = 1; i < num; ++i)
			{
				retStr += ("," + row[i]);
			}

			retStr += "\n";
		}
		return retStr;
    } // end of toString()

    // decrease 1 to each value to make sure there is no zero
	private int bitConvert(int val)
	{
		return val - 1;
	}
	
	// Here we have used BitSet to know if the value have been assign
	// to the same row and column and if it diplucate it will go on of if onndtions
	private boolean validateHelper()
	{
		for (int i = 0; i < num; ++i)
		{
			// create two BitSet because when get a value twice it will return false.
			BitSet coverRow = new BitSet();
			BitSet coverCol = new BitSet();

			for (int j = 0; j < num; ++j)
			{
				// make sure at the start there is no value on coverRow
				if (coverRow.get(bitConvert(grid[i][j])))
				{
					return false;
				}
				// assign it 
				coverRow.set(bitConvert(grid[i][j]));
				// same for column
				if (coverCol.get(bitConvert(grid[j][i])))
				{
					return false;
				}
				// assign it . 
				coverCol.set(bitConvert(grid[j][i]));
			}
		}
		
		// here for the blocks or boxes to make sure . 
		int blockRow;
		int blockCol;
		
		// for this loop is to ckeck the boxes for instance 
		// r: 0 d: 0
		// r: 0 d: 1
		// r: 1 d: 0
		// r: 1 d: 1
		// this box it will check first , then go to the right box with the same rows. 
		for (int i = 0; i < sqrt; ++i)
		{
			blockRow = i * sqrt;

			for (int j = 0; j < sqrt; ++j)
			{
				blockCol = j * sqrt;
				// make a variable to make sure for the boxes. if it duplicate. 
				BitSet box = new BitSet();
				for (int r = blockRow; r < blockRow + sqrt; r++)
				{
					for (int d = blockCol; d < blockCol + sqrt; d++)
					{
						// System.out.println("r: "+r+" d: "+d);
						// check the box with the same way for row and column . 
						if (box.get(bitConvert(grid[r][d])))
						{
							return false;
						}
						// assign it 
						box.set(bitConvert(grid[r][d]));
					}
				}
			}
		}

		// this is will take each cage from the cages which already have been assigned , 
		for (Cage cage : cages)
		{
			int sum = 0;
			BitSet cageSet = new BitSet();
			// here to check the cells. 
			for (Cell cell : cage.cells)
			{
				sum += grid[cell.row][cell.col];
				// if it exist cage set it will return false 
				if (cageSet.get(bitConvert(grid[cell.row][cell.col])))
				{
					return false;
				}
				// if not exist , it will assign it. 
				cageSet.set(bitConvert(grid[cell.row][cell.col]));
			}
			// when the sum does not equals the same sum that already exist on the cage , it will return false. 
			if (sum != cage.sum)
			{
				return false;
			}
		}
		
		return true;
	}
	
	// here returne true or false if validate or not.
	// It goes to the validate helper to chech the validalaty. 
    @Override
    public boolean validate() {
    	
		boolean ans;
		try
		{
			ans = validateHelper();
		}
		catch (IndexOutOfBoundsException e)
		{
			ans = false;
		}

		return ans;
    } // end of validate()

} // end of class KillerSudokuGrid

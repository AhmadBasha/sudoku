/**
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package grid;

import java.io.*;
import java.util.BitSet;
import java.util.NoSuchElementException;
import java.util.Scanner;


/**
 * Class implementing the grid for standard Sudoku.
 * Extends SudokuGrid (hence implements all abstract methods in that abstract
 * class).
 * You will need to complete the implementation for this for task A and
 * subsequently use it to complete the other classes.
 * See the comments in SudokuGrid to understand what each overriden method is
 * aiming to do (and hence what you should aim for in your implementation).
 */
public class StdSudokuGrid extends SudokuGrid
{
    // TODO: Add your own attributes

    public StdSudokuGrid() {
        super();

        // TODO: any necessary initialisation at the constructor
    } // end of StdSudokuGrid()


    /* ********************************************************* */

    // Here for reading the file of grid. Assign the varibales number , square root
    // grid and symbols. row , cal and value have been saved as well 
    // basically is just to create the gird. 
    @Override
    public void initGrid(String filename)
        throws FileNotFoundException, IOException
    {
    	
		Scanner scanner = new Scanner(new File(filename));
		// first line on the file. 
		this.num = Integer.parseInt(scanner.nextLine());
		// creating the square root. 
		this.sqrt = (int) Math.sqrt(num);
		// making the grid 
		grid = new int[num][num];
		// know the number of symbol. 
		this.symbols = new int[num];
		// here to save the number of the symbols. 
		for (int i = 0; i < num; ++i)
		{
			symbols[i] = scanner.nextInt();
		}
		// to assign the row , cal and value . 
		while (scanner.hasNextLine())
		{
			String partOne;
			try
			{
				partOne = scanner.next();
			}
			// for the last empty line on the file. 
			catch (NoSuchElementException e)
			{
				break;
			}
			// split the first part by a comma to take row and cal.
			String partOne_split[] = partOne.split(",");

			int row = Integer.parseInt(partOne_split[0]);
			int cal = Integer.parseInt(partOne_split[1]);

			int val = scanner.nextInt();
			// assign the values on the gride. 
			grid[row][cal] = val;
		}

		scanner.close();
    } // end of initBoard()

    // write them on a file. 
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

} // end of class StdSudokuGrid

/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.HashSet;

import grid.SudokuGrid;
import helper.BetterCage;
import helper.Cell;
import helper.Subset;



/**
 * Your advanced solver for Killer Sudoku.
 */
public class KillerAdvancedSolver extends KillerSudokuSolver
{

	SudokuGrid grid;
	int[] symbols;

	/* making these because are needed for the cages */
	BetterCage[] cages;
	int[][] cageCells;

    public KillerAdvancedSolver() {
        // TODO: any initialisation you want to implement.
    } // end of KillerAdvancedSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	
    	
		this.grid = grid;
		this.symbols = grid.getSymbols();
		this.cages = grid.getCages();
		this.cageCells = grid.getCageIndex();
		
		Subset ss = new Subset();
		
		// taking symbols , and the number of them , the largest sum .
		ss.init(symbols, symbols.length , grid.getLargestSum());
		
		// it will take each cage from cages 
		for( BetterCage cage : cages)
		{
			// creating unique . 
			HashSet<Integer> unique = new HashSet<Integer>();
			
			// going to these finctions
			ss.clearSet();
			ss.search(cage.sum);
			
			// for unique to add a num , basically saving the set that achieve to the num
			for( ArrayList<Integer> set : ss.getSet() )
			{
				if( set.size() == cage.cells.length)
				{
					for( int num: set)
					{
						unique.add(num);
					}
				}
			}
			
//			System.out.println("num: "+ cage.sum+" set: "+ unique);
			
			cage.unique = new int[unique.size()];
			
			int count = 0;
			for( int val: unique)
			{
				cage.unique[count++] = val;
			
			}
			
		}
		
		// This to get the result of the sudoku or basically is just solve it.
		boolean ans = sudokuSolution(grid.getGrid(), grid.getNum());
		
		return ans;
    } // end of solve()
    
    
    public boolean sudokuSolution(int[][] theGrid, int n)
	{
		boolean emptyCell = false;
		int row = -1;
		int col = -1;
		// here for checking the empty , while the grid has values it will running throug the loop
		// but if there is zero it will break and doing the next part 
		for (int i = 0; i < n; i++)
		{
			for (int j = 0; j < n; j++)
			{
				// when it equals zero , it will go to this if condtion 
				// amd it will go out from the first loop
				if (theGrid[i][j] == 0)
				{
					row = i;
					col = j;
					// we still have some remaining
					// missing values in Sudoku
					emptyCell = true;
					break;
				}
			}
			// when the value is zero it will also go out from the second loop from here .
			// because empty reassigned the value to false which is correct on this condtion.
			if (emptyCell)
			{
				break;
			}
		}
		// The gird is already full And all balnck cells have been filled. 
		if (!emptyCell)
		{
			grid.setGrid(theGrid);
			return true;
		}

		// Here to check each number on the symbole  , and the main idea of this function 
		// to take row and column and check the correct symbol to pass it to the other function. 
		for (int num : cages[cageCells[row][col]].unique)
		{
			// Here to check each number if it unique or not through this function. 
			if (check(theGrid, row, col, num))
			{
				theGrid[row][col] = num;
				if (sudokuSolution(theGrid, n))
				{
					return true;
				}
				// when it come to this else , means the number is already exist on row or col or box. 
				// so it will change the previos one to 0.  
				else
				{
					// change it to 0 .
					theGrid[row][col] = 0;
				}
			}
		}
		return false;
	}
	
	// this function called when there is a number needed to be checked and to see if duplicated in row, column and boxes.
	// if the number is correct and it can be filled , it will return true 
	private boolean check(int[][] grid, int row, int col, int num)
	{
		//is check for the row if the value is exist or not if yes,it  will return false
		for (int c = 0; c < grid.length; c++)
		{
			// here row just with one value and col changing to check 
			if (grid[row][c] == num)
			{
				return false;
			}
		}
		//is check for the row if the value is exist or not if yes,it  will return false
		for (int r = 0; r < grid.length; r++)
		{
			// here col just with one value and row changing to check 
			if (grid[r][col] == num)
			{
				return false;
			}
		}
	
		// here to check the blocks 
		int subGrid = (int) Math.sqrt(grid.length);
		// row and col for the box. 
		int blockRow = row - row % subGrid;
		int blockCol = col - col % subGrid;
		
		// the firs for is row and second col , and take both of them to check it in each cell on the block
		for (int r = blockRow; r < blockRow + subGrid; r++)
		{
			// col loop . 
			for (int c = blockCol; c < blockCol + subGrid; c++)
			{
				// if the value is exist it  will return false
				if (grid[r][c] == num)
				{
					return false;
				}
			}
		}
		
		int sum = 0;
		int index = cageCells[row][col];
		// cell will take each values on the cage index.
		for (Cell cell : cages[index].cells)
		{
			sum += grid[cell.row][cell.col];
			// if the value is exist it  will return false
			if (num == grid[cell.row][cell.col])
			{
				return false;
			}
		}
		// when sum is been bigger than the number of the original sum
		if (sum + num > cages[index].sum)
		{
			return false;
		}

		
		return true;
	}

} // end of class KillerAdvancedSolver

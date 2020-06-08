/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.HashMap;

import grid.SudokuGrid;


/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver
{
	
	int sqrt, num;
	int board[][];
	int mt[][];
	int symbols[];
	
	HashMap<Integer, Integer> revSymbolIdx;
	private ArrayList<Integer> solution = new ArrayList<Integer>();
	private HashMap<Integer, Integer> deletedRow = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> deletedCol = new HashMap<Integer, Integer>();

    public AlgorXSolver() {
        // TODO: any initialisation you want to implement.
    } // end of AlgorXSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
        // TODO: your implementation of the Algorithm X solver for standard Sudoku.

		num = grid.getNum();
		sqrt = grid.getSqrt();
		symbols = grid.getSymbols();
		// [ 4, 8, 10, 16 ]
		// reverse symbol index
		// { 4 -> 1, 8 -> 2, 10 -> 3, 16 ->4 }
		revSymbolIdx = new HashMap<Integer, Integer>();
		// put the symbol values and keys.
		for (int i = 0; i < symbols.length; ++i)
		{
			revSymbolIdx.put(symbols[i], i + 1);
		}
		// 0 4 8 10 16
		// 0 1 2 3 4
		revSymbolIdx.put(0, 0);
		// making the grid
		board = new int[num][num];
		// assign board , and making the grid with symbol which is the value of revSymbolIdx
		for (int i = 0; i < num; ++i)
		{
			for (int j = 0; j < num; ++j)
			{
				board[i][j] = revSymbolIdx.get(grid.getGrid()[i][j]);
			}
		}

		// this part for creating the matrix without the sample , it be conerted later on.
		int[][] Mt = initMt(board);
		// this is will take true or false and it will return for this function
		boolean complete = solve();
		// making a new array that have the total number of index on the grid , we can later put the cell number
		int[] ans = new int[num * num];
		// for each i on the solution 
		for (int i : solution)
		{
			// need the cell number and value
			int cellNum = -1;
			int value = -1;
			// it goes with this loop which must be lower than the matrix length
			for (int j = 0; j < mt[i].length; ++j)
			{
				// when facing value one has to condtions
				if (Mt[i][j] == 1)
				{
					// when j lower it will assign the sell and index to check if the index lower or not
					if (j < 3 * num * num)
					{
						int cell = (j / (3 * num)) + 1;
						int index = j - (cell - 1) * 3 * num;
						// index condtion that i mentioned before 
						if (index < num)
						{
							value = (j / (3 * num)) + 1;
						}
					}
					// here the other condtion.
					else
					{
						cellNum = j - 3 * num * num;
					}
				}

			}
			// here to assign the cell number ot the index of answer  with the value 
			ans[cellNum] = value;
		}
		// it will increase through for. 
		int resultCounter = 0;
		// loop for the row
		for (int r = 0; r < num; r++) 
		{
			// loop for the col
			for (int c = 0; c < num; c++) 
			{
				// this is the grid to assign it with the same value of ans
				board[r][c] = ans[resultCounter];
				resultCounter++;
			}
		}
		// here to to set the value to the original values . 
		for (int i = 0; i < num; ++i)
		{
			for (int j = 0; j < num; ++j)
			{
				int val = board[i][j];

				if (val == 0)
				{
					grid.setGridPos(i, j, 0);
				}
				else
				{
					grid.setGridPos(i, j, symbols[val - 1]);
				}
			}
		}
		// done true or false 
		return complete;

    } // end of solve()
    
	private int[][] initMt(int[][] mtCreate)
	{
		// to assign the 
		int[][] sets = null;
		// the count depends on how many values are existing on the grid.
		int count = 0;
		ArrayList<int[]> list = new ArrayList<int[]>();
		// this loop to add on the list or assign. 
		for (int row = 0; row < num; row++)
		{
			for (int col = 0; col < num; col++)
			{
				// for the values that bigger than zero on the gird.
				if (mtCreate[row][col] > 0)
				{
					// adding them to the list { value, r, c }
					list.add(new int[] { mtCreate[row][col], row, col });
					count++;
				}
			}
		}
		// making the sets with the number of values that existing on the grid which is being known by count.
		sets = new int[count][];

		// to assign all of them on the first index [value][0]
		for (int i = 0; i < count; i++)
		{
			sets[i] = (int[]) list.get(i);
		}
		// here making the matrix which have [rows] [cols] 
		mt = new int[num * num * num][4 * num * num];

		// the first loop for each number or cell.
		for (int idx = 0; idx < num; idx++)
		{
			// this is for rows
			for (int rows = 0; rows < num; rows++)
			{
				// finally fot the columns . 
				for (int cols = 0; cols < num; cols++)
				{
					// it goes here when the value on the set 
					if (empty(idx, rows, cols, sets))
					{
						// here for mapping the matrix  and constrain & to know the indexes on columns 
						// assign each one of them with One. 
						int rowCon = cols+ (num * rows) + (num * num * idx);
						int boxCon = ((cols / sqrt) + ((rows / sqrt) * sqrt));
						int c= 3 * num * num + (cols + num * rows);
						int boxWithc = 3 * num * idx + 2 * num + boxCon;
						int cWithr = 3 * num * idx + rows;
						int cWithc = 3 * num * idx + num + cols;
						mt[rowCon][c] = 1;
						mt[rowCon][boxWithc] = 1;
						mt[rowCon][cWithr] = 1;
						mt[rowCon][cWithc] = 1;

					}
					
				}
				
			}
		}
		return mt;
	}

	private boolean empty(int idx, int r, int c, int[][] sets)
	{
		// assign it true at the beggigng and it will return the empty value. 
		boolean empty = true;
		if (sets != null)
		{
			for (int i = 0; i < sets.length; i++)
			{
				// the fist on each cell. 
				int cell = sets[i][0] - 1;
				int row = sets[i][1];
				int col = sets[i][2];
				// knowing the row and cols starting and ending 
				int firstRow = (r / sqrt) * sqrt;
				int lastRow = firstRow + sqrt;
				int firstCol = (c / sqrt) * sqrt;
				int lastCol = firstCol + sqrt;
				// here idx , r , c for the parametrs. 
				// the first when cell do not equal idx it will return false
				if (cell != idx && r == row && c == col)
				{
					empty = false;
				}
				// here idx must equal cell and of the row and col equlas the value , but the value doesn't equal. 
				else if ((idx == cell) && (r == row || c == col) && !(r == row && c == col))
				{
					empty = false;
				}
				// here to check the assigned values of rows and cols
				else if ((idx == cell) && (row > firstRow) && (row < lastRow)
						&& (col > firstCol) && (col < lastCol) && !(row == r && col == c))
				{
					empty = false;
				}
			}
		}
		return empty;
	}
	
	// this is true or false function 
	private boolean solve()
	{
		// if the the size equal the grid , will return true
		if (solution.size() == num * num)
		{
			return true;
		}
		// make a new value for select function
		int selected = select();

		if (selected == -1)
		{
			return false;
		}
		// these is for the row choices 
		ArrayList<Integer> rowChoices = new ArrayList<Integer>();
		// check if the row exist and equlas to one then add it 
		for (int jj = 0; jj < mt.length; ++jj)
		{
			if (!deletedRow.containsKey(jj) && mt[jj][selected] == 1)
			{
				rowChoices.add(jj);
			}
		}
		
		// for each rowchoice 
		for( int rowChoice : rowChoices)
		{
			// it will be added 
			solution.add(rowChoice);
			// deleting rows and cols 
			HashMap<Integer, Integer> locDelRows = new HashMap<Integer, Integer>();
			HashMap<Integer, Integer> locDeletCols = new HashMap<Integer, Integer>();
			for (int j = 0; j < mt[0].length; ++j)
			{
				if (!deletedCol.containsKey(j) && mt[rowChoice][j] == 1)
				{
					for (int i = 0; i < mt.length; ++i)
					{
						if (!deletedRow.containsKey(i) && mt[i][j] == 1)
						{
							deletedRow.put(i, 1);
							locDelRows.put(i, 1);
						}
					}
					deletedCol.put(j, 1);
					locDeletCols.put(j, 1);
				}
			}
			
			// doing the same function after the loop 
			if (solve())
				return true;

			else
			{
				// remving the row
				solution.remove((Integer) rowChoice);
				// deleting the row and col
				locDelRows.forEach((key, value) -> deletedRow.remove(key));
				locDeletCols.forEach((key, value) -> deletedCol.remove(key));
			}
		}
		return false;
	}

	private int select()
	{
		// these thre values are needed . 
		int sum;
		int minCol = -1;
		int minSum = Integer.MAX_VALUE;
		// going through the loop then check if the col then row are deleted or not. 
		for (int i = 0; i < mt[0].length; ++i)
		{
			// col 
			if (!deletedCol.containsKey(i))
			{
				// going the other loop
				sum = 0;
				for (int j = 0; j < mt.length; ++j)
				{
					// check row 
					if (!deletedRow.containsKey(j))
					{
						sum += mt[j][i];
					}
				}
				// sum equals or lower min sum . 
				if (sum > 0 && sum < minSum)
				{
					minCol = i;
					minSum = sum;
				}
			}
		}

		return minCol;
	}   

} // end of class AlgorXSolver

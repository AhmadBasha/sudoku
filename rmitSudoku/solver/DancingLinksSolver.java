/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import grid.SudokuGrid;
import helper.DancingLinkCol;
import helper.colCell;
import helper.node;


/**
 * Dancing links solver for standard Sudoku.
 */
public class DancingLinksSolver extends StdSudokuSolver
{


	// same as Algor x 
	int sqrt, num;
	int board[][];
	int symbols[];
	HashMap<Integer, Integer> revSymbolIdx;
	
	// these for the dancing
	private colCell firstCell = null;
	private ArrayList<node> result = new ArrayList<node>();

	@SuppressWarnings("unused")
	private colCell doubleLinkedList;

    public DancingLinksSolver() {
        // TODO: any initialisation you want to implement.
    } // end of DancingLinksSolver()


    @Override
    public boolean solve(SudokuGrid grid) {
    	// same as Algor x 
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

		// the grid will go to this function which can make the matrix and do the link list . 
		this.Start(board);
		
		// change everything to the orignal symbols. 
		for (int i = 0; i < num; ++i)
		{
			for (int j = 0; j < num; ++j)
			{
				grid.setGridPos(i, j, symbols[board[i][j] - 1]);
			}
		}

		return true;

    } // end of solve()
    
	private void Start(int[][] mtCreate)
	{
		// making a new matrix and then pass it to solve it 
		int[][] mt = initMt(mtCreate);
		dancing(mt);
//
//		for( int row[] : mt)
//		{
//			for (int cell : row)
//			{
//				System.out.print(cell);
//			}
//			System.out.println();
//		}

		find(0);
	}
	
	// create a sparse matrix for Grid
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
	  int[][] mt = new int[num * num * num][4 * num * num];

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
	
	// conver the matrix to dancing link that make to do the dancing link 
	private void dancing(int[][] mt)
	{
		// to know the fisrst cell 
		firstCell = new colCell();
		// making the heads which can be easily to perform 
		colCell headerCol = firstCell;
		// creating a new variable to save and need to map it with the header
		for (int c = 0; c < mt[0].length; c++)
		{
			// here for saving purposes. 
			DancingLinkCol colIdex = new DancingLinkCol();
			
			if (c < 3 * num * num)
			{
				// creating cell 
				int cell = (c / (3 * num)) + 1;
				colIdex.num = cell;
				// the index of the cell
				int cellIdx = c - (cell - 1) * 3 * num;
				// this is the contrain for row 
				if (cellIdx < num)
				{
					colIdex.con = 0;
					colIdex.location = cellIdx;
				}
				// this is the contrain for col
				else if (cellIdx < 2 * num)
				{
					colIdex.con = 1; 
					colIdex.location = cellIdx - num;
				}
				// this is the contrain for box.
				else
				{
					colIdex.con = 2; 
					colIdex.location = cellIdx - 2 * num;
				}
			}
			// for other cases. 
			else
			{
				colIdex.con = 3;
				colIdex.location = c - 3 * num * num;
			}
			// here to save the header with the detail. 
			headerCol.right = new colCell();
			headerCol.right.left = headerCol;
			headerCol = (colCell) headerCol.right;
			// this is the detail part and the header
			headerCol.detail = colIdex;
			headerCol.top = headerCol;
		}
		// here start from the right to be the first cell and so on . 
		headerCol.right = firstCell;
		firstCell.left = headerCol;
		/* from here all the headers have been assigned and continue for the rows */ 
		for (int r = 0; r < mt.length; r++)
		{
			// looping aroung all cols.
			headerCol = (colCell) firstCell.right;
			// here starting and ending values 
			node start = null;
			node end = null;
			for (int c = 0; c < mt[r].length; c++)
			{
				if (mt[r][c] == 1)
				{
					node temp = headerCol;
					// checking down on temp from the node 
					while (temp.down != null)
					{
						temp = temp.down;
					}
					temp.down = new node();
					// for the start part 
					if (start == null)
					{
						start = temp.down;
					}
					temp.down.up = temp;
					temp.down.left = end;
					temp.down.top = headerCol;
					// check the end if not null
					if (end != null)
					{
						temp.down.left.right = temp.down;
					}
					end = temp.down;
					headerCol.size++;
				}
				headerCol = (colCell) headerCol.right;
			}
			// to connect the first part with the last one. 
			if (end != null)
			{
				end.right = start;
				start.left = end;
			}
		}
		// assigned the header col . 
		headerCol = (colCell) firstCell.right;
		// connect the last col. 
		for (int i = 0; i < mt[0].length; i++)
		{
			node temp = headerCol;
			while (temp.down != null)
			{
				temp = temp.down;
			}
			temp.down = headerCol;
			headerCol.up = temp;
			headerCol = (colCell) headerCol.right;
		}
	}

	
	// here is to select the cell that need to be taken.
	private void find(int key)
	{
		// here to map the dancing to board. which it converted. 
		if (firstCell.right == firstCell)
		{
			convert(); 
			return;
		}
		//  exactcover the columns 
		colCell col = select(); 
		exactCover(col);
		// rows
		node row = col.down;
		while (row != col)
		{
			// adding them to the result 
			result.add(key, row); 
			// making it for exactCover
			node temp = row.right;
			while (temp != row)
			{
				exactCover(temp.top);
				temp = temp.right;
			}
			// finding again to the same loop. 
			find(key + 1); 
			
			// to use them in exact uncovering. 
			node tempTwo = (node) result.get(key);
			node tempThree = tempTwo.left;
			// if they not equal each others. 
			while (tempThree != tempTwo)
			{
				inExactCover(tempThree.top);
				tempThree = tempThree.left;
			}
			row = row.down;
		}
		inExactCover(col);
	}

	// covert the matrix to the board. 
	private void convert()
	{
		// making a new variable that have size for the whole grid
		int[] ans = new int[num * num];
		// it will stop when all values beed executed.
		for (Iterator<node> current = this.result.iterator(); current.hasNext();)
		 {
			int cell = -1;
			int num = -1;
			// value and temp will take the coming value. 
			node value = (node) current.next();
			node temp = value;
			do
			{
				// thie is for the row and the next condtion for the cell
				if (temp.top.detail.con == 0)
				{ 
					num = temp.top.detail.num;
				}
				else if (temp.top.detail.con == 3)
				{ 
					cell = temp.top.detail.location;
				}
				temp = temp.right;
				
			} 
			// to assigned the values to ans
			while (value  != temp);
			ans[cell] = num; 
		 }

		// just put them on the board from ans. 
		int ansCount = 0;
		// row
		for (int r = 0; r < num; r++) 
		{
			// cols.
			for (int c = 0; c < num; c++) 
				{
				board[r][c] = ans[ansCount];
				ansCount++;
			}
		}
	}
	
	// here to put all the values again to the dancing link 
	private void inExactCover(node col)
	{
		// latest for the row and cell is for the current loc. 
		node latest = col.up;
		while (latest != col)
		{
			node cellLocation = latest.left;
			// to add it back 
			while (cellLocation != latest)
			{
				cellLocation.top.size++;
				// adding the one back to the same location 
				cellLocation.down.up = cellLocation;
				cellLocation.up.down = cellLocation;
				cellLocation = cellLocation.left;
			}
			latest = latest.up;
		}
		// the headers 
		col.right.left = col;
		col.left.right = col;
	}


	private void exactCover(node col)
	{
		// this is an example to descripe what will happen with this one 
		// [ 1 ] <-> [ 2 ] <-> [ 3 ]
		// [ 1 ] <-> [ 2 ] -> [ 3 ]
		// <--------------
		// [ 1 ] <------------> [ 3 ]
		col.right.left = col.left;
		col.left.right = col.right;
		node latest = col.down;
		// these three are responsibale to exactcover the whole rows of the column.
		
			
		while (latest != col)
		{
			node curNode = latest.right;
			while (curNode != latest)
			{
				curNode.down.up = curNode.up;
				curNode.up.down = curNode.down;
				curNode.top.size--;
				curNode = curNode.right;
			}
			latest = latest.down;
		}
	}
		
	private colCell select()
	{
		// the best way to start with the lower value . 
		colCell next = (colCell) firstCell.right;
		// assigned the lower one and return it 
		colCell lower = next;
		
		// here to select the lower size of the cols .
		while (next.right != firstCell)
		{
			next = (colCell) next.right;
			if (next.size < lower.size)
			{
				lower = next;
			}
		}
		return lower;
	}




} // end of class DancingLinksSolver

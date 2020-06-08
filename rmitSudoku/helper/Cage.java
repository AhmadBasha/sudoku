package helper;

public class Cage
{
	// three values needed cells , sum and count
	public Cell[] cells;
	public int sum;
	static int count;
	
	// takes sum of the cage and the length to know how many cells 
	public Cage(int sum, int length)
	{
		cells = new Cell[length];
		this.sum = sum;
		count = 0;
	}

	// adding the cells with row and col. 
	public void add(int row, int col)
	{
		cells[count++] = new Cell(row, col);
	}
}


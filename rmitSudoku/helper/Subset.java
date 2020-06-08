package helper;

import java.util.ArrayList;

// some of the code from this class has been taken from 
// https://www.geeksforgeeks.org/ 
// and it has modified for the assignment purposes. 

public class Subset
{
	
	ArrayList<ArrayList<Integer>> set = new ArrayList<ArrayList<Integer>>();
	
	// these three variables are needed. 
	int length;
	int[] symbol;
	boolean[][] daynamicProgramming;
	
	
	// here to create the set and daynamicProgramming will set true. basically is creating the table
	// with possible sums. 
	public void init(int symbols[], int length, int largestSum)
	{
		
		this.symbol = symbols;
		this.length = length;
		daynamicProgramming = new boolean[length][largestSum + 1];
		
		// here to save true if it possible with i 
		for (int i = 0; i < length; ++i)
		{
			daynamicProgramming[i][0] = true;
		}

		// achievning the sume with symbol. saving all the sum on daynamicProgramming[0]
		if (symbol[0] <= largestSum)
		{
			daynamicProgramming[0][symbol[0]] = true;
		}
		// for the whole daynamicprogramming to save true or false 
		for (int i = 1; i < length; ++i)
		{
			for (int j = 0; j < largestSum + 1; ++j)
			{
				daynamicProgramming[i][j] = (symbol[i] <= j) ? (daynamicProgramming[i - 1][j] || daynamicProgramming[i - 1][j - symbol[i]]) : daynamicProgramming[i - 1][j];
			}
		}
//		System.out.print("\t");
//        
//        for( int j = 0; j< daynamicProgramming[0].length; ++j )
//        {
//            System.out.print(j+"\t");
//        }
//        System.out.println();
//        
//        for( int i = 0; i< daynamicProgramming.length; ++i)
//        {
//            System.out.print(symbol[i]+"\t");
//            for( int j = 0; j< daynamicProgramming[0].length; ++j )
//            {
//                System.out.print(daynamicProgramming[i][j]+"\t");
//            }
//            System.out.println();
//        }	
				
				
	}
	
	// to delet the set. to be a new one. 
	public void clearSet()
	{
		set.clear();
	}
	
	public void search(int sum)
	{
		// goinng to the other function and start going from the largest. 
		ArrayList<Integer> k = new ArrayList<>();
		subsets(symbol, length - 1, sum, k);
	}

	// for inserting 
	@SuppressWarnings("unchecked")
	void insertSet(ArrayList<Integer> i)
	{
		set.add((ArrayList<Integer>) i.clone());
	}
	
	// getting the set 
	public ArrayList<ArrayList<Integer>> getSet()
	{
		return set;
	}
	

	// find all subset that added to the sum 
	void subsets(int symbol[], int temp, int sum, ArrayList<Integer> k)
	{
		// 
		if (temp == 0 && sum != 0 && daynamicProgramming[0][sum])
		{
			k.add(symbol[temp]);
			insertSet(k);
			k.clear();
			return;
		}

		// here when the sum is zero . which mean in the end of the symbol. 
		if (temp == 0 && sum == 0)
		{
			insertSet(k);
			k.clear();
			return;
		}

		// saving the path with vector and achieving the sum when it stop looking for the current element. 
		if (daynamicProgramming[temp - 1][sum])
		{
			ArrayList<Integer> vector = new ArrayList<>();
			vector.addAll(k);
			subsets(symbol, temp - 1, sum, vector);
		}

		// starting ftom the largest element to the smallest one . 
		if (sum >= symbol[temp] && daynamicProgramming[temp - 1][sum - symbol[temp]])
		{
			k.add(symbol[temp]);
			subsets(symbol, temp - 1, sum - symbol[temp], k);
		}
	}

	




}


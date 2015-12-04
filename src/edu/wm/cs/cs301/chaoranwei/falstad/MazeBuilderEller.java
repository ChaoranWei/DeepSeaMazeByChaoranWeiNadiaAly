package edu.wm.cs.cs301.chaoranwei.falstad;


import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
/**
 * This class has the responsibility to create a maze of given dimensions (width, height) together with a solution based on a distance matrix.
 * The Maze class depends on it. The MazeBuilder performs its calculations within its own separate thread such that communication between 
 * Maze and MazeBuilder operates as follows. Maze calls the build() method and provides width and height. Maze has a call back method newMaze that
 * this class calls to communicate a new maze and a BSP root node and a solution. 
 * 
 * The maze is built with Eller's algorithm. Eller's algorithm builds a maze one row at a time and is only needs to look at one row at a time. 
 * Eller's algorithm introduces sets to keep track of which cells share a path between them. 
 * 
 * 
 */


public class MazeBuilderEller extends MazeBuilder implements Runnable {
	
	public MazeBuilderEller() {
		super(); 

	}
	public MazeBuilderEller(boolean det) {
		super(det); 
	}	

/**
 * This class has the responsibility to create a maze of given dimensions (width, height) together with a solution based on a distance matrix.
 * The Maze class depends on it. The MazeBuilder performs its calculations within its own separate thread such that communication between 
 * Maze and MazeBuilder operates as follows. Maze calls the build() method and provides width and height. Maze has a call back method newMaze that
 * this class calls to communicate a new maze and a BSP root node and a solution.
 * 
 * The maze is built with Eller's algorithm. The algorithm starts at the top row, moving left to right between cells. 
 *   
 * @author Nadia Aly & CHaoran Wei
 */
	@Override
	protected void generatePathways() {
		
		List<HashSet<Integer>> ListOfSet = new ArrayList<HashSet<Integer>>();
		ListOfSet = InitializeSet(ListOfSet, width);
		
		for (int y = 0; y < height - 1; y++) {  //-1 because all sets will join in the last row
			//delete wall by joining the set
			for (int x = 0; x < width - 1; x++) {
				if (!cells.hasNoWallOnRight(x,y)) {
				    int temp = random.nextIntWithinInterval(0, 100);
				
                    if (temp < 30) {
                    	int index = getSetWithCertainIndex(ListOfSet, x);
			            
                    	int index2 = getSetWithCertainIndex(ListOfSet, x + 1);
			         
			            HashSet<Integer> a = ListOfSet.get(index2);
			            //System.out.println(x + 1000);
			            //System.out.println(index);
			            ListOfSet.get(index).addAll(a);
			            
			            cells.deleteWall(x, y, 1, 0); 
                    }
				}
			}
			
			//dont need to mark it as visited because this row will be useless then
			//randomly expand to the next row
		for (HashSet<Integer> item: ListOfSet) {
			//System.out.println(item);
			HashSet<Integer> set = randomlySelectFromSet(item);
			//System.out.println(set);
			item.clear();
			for (int element: set) {
				cells.deleteWall(element, y, 0, 1);
				item.add(element);
			}
		}
		//System.out.println(ListOfSet);
		ListOfSet = removeDuplicates(ListOfSet);
		//initialize rest of next row
		HashSet<Integer> NewList = getAllIndexInSets(ListOfSet, ListOfSet.size());
		
		for (int j = 0; j < width; j++) {
			if (!NewList.contains(j)) {
				HashSet<Integer> temp = new HashSet<Integer>();
				temp.add(j);
				ListOfSet.add(temp); 
			}
		}
		}
	//delete all walls from last row
	for (int k = 0; k < width - 1;k++){
		cells.deleteWall(k, height - 1, 1, 0);//k=x coordinate, height-1 (y coordinate of last row),1-delete wall to the righ
		}
	}
	

	/**
	 * InitializeSet initializes a List of HashSet's where each HashSet represents a set in the maze row. Start at 0 and 
	 * add set for the length of the maze.
	 */
	
	private List<HashSet<Integer>> InitializeSet(List<HashSet<Integer>> ListOfSet, int width) {
		for (Integer i = 0; i < width; i ++) {
			if (cells.hasWallOnRight(i, 0)) {
			    HashSet<Integer> temp = new HashSet<Integer>();
			    temp.add(i);
			    ListOfSet.add(temp); 
			}
		}
		return ListOfSet;
	}
	
	/**
	 * getAllIndexInSets: Iterate across the row, find the element at each position (index) across the row,
	 * Then inserts the elements in the set into the list NewList, starting at the beginning of the row. Returns
	 * consequently, returns the index of all elements in each set. 
	 */
	private HashSet<Integer> getAllIndexInSets(List<HashSet<Integer>> ListOfSet, int width) { 
		HashSet<Integer> NewList = new HashSet<Integer>();
		for (int i = 0; i < width; i++) {
			NewList.addAll(ListOfSet.get(i)); 
		}
		return NewList;
	}
	
	/**
	 * removeDuplicate, loops over the row and stores the unique sets in the hash set list, Also Flesh out 
	 * the next row by putting any remaining cells into their own sets. If the set doesn't contain the 
	 * item, adds it to the result.  
	 */
	static List<HashSet<Integer>> removeDuplicates(List<HashSet<Integer>> listOfSet) {
		List<HashSet<Integer>> result = new ArrayList<>();// Store unique items in result.
		HashSet<HashSet<Integer>> set = new HashSet<>();// Record encountered Strings in HashSet.
		// Loop over argument list.
		for (HashSet<Integer> item : listOfSet) {
			// If String is not in set, add it to the list and the set.
		    if (!set.contains(item)) {
			result.add(item);
			set.add(item);
		    }
		}
		return result;
	    }
	/**
	 * randomlySelectFromSet, finds the index of the first cell within a set. 
	 */
	private int getSetWithCertainIndex(List<HashSet<Integer>> ListOfSet, int index) {
		int a = 0;
		for (int i = 0; i < ListOfSet.size(); i ++) {
			for (int j: ListOfSet.get(i)) {
			    if (j == index) {
				    a = i;
			    }
			}
		}
	    return a;
	}
	
	
	/**
	 * randomlySelectFromSet, finds the index of the first cell within a set. 
	 */
	private HashSet<Integer> randomlySelectFromSet(HashSet<Integer> item) {
		
		int index = random.nextIntWithinInterval(0, item.size() - 1);
		//System.out.println(index);
		HashSet<Integer> set = new HashSet<Integer>();
		int i = 0;
		for (int element: item) {
			if (index == i) {
				set.add(element);
				
			}
			i++;
		}
		
		for (int element: item) {
			int WhetherToExpand = random.nextIntWithinInterval(0, 100);
			if (WhetherToExpand < 30) {
				set.add(element);
			}
		}
		return set;
		
	}
	
}


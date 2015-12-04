
package edu.wm.cs.cs301.chaoranwei.falstad;



import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.util.Log;
import edu.wm.cs.cs301.chaoranwei.falstad.Robot.Turn;
import edu.wm.cs.cs301.chaoranwei.ui.PlayActivity;

import java.util.Collections;


public class Wizard implements RobotDriver {

	private Maze maze;
	private Robot robot;
	private int Width, Height;
	private int steps = 0;
    private int[][] distMatrix;
    private Distance dist;
	private boolean hitted=false;
	private PlayActivity play;
	
    public Wizard() {
		super();
	}
	
	
	
	/**Set robot to the driver
	 */
	@Override
	public void setRobot(Robot r) {
		robot = r;
		r.setBatteryLevel(2500);
		maze = robot.getMaze();
		
	}
	
	public void setActivity(PlayActivity p) {
		play = p;
	}
		
	/**Set dimension of the maze driver operates on
	 */
	@Override
	public void setDimensions(int width, int height) {
		Width = width;
		Height = height;
	}

	/**
	 * Return reference to field distMatrix
	 * @return distMatrix
	 */
	public int[] [] getdistMatrix(){
		return distMatrix;
	}
	/**set distance matrix of the maze driver operates on
	 */
	@Override
	public void setDistance(Distance distance) {
		dist = distance;
		
		distMatrix = dist.getDists();
		
	}
	
	/**Drive to exit by following the minimum distance in all four directions 
	 * from current position to exit 
	 */
	@Override
	public boolean drive2Exit() throws Exception {
	   // while (!robot.isAtGoal()){
	    	play.finishGame();
	    	play.energythread.run();
	    	//play.driverHandler.postDelayed(play.DriverRunnable, 200);
				if (robot.hasStopped()){
					if (robot.getBatteryLevel() <= 0) { 
						initializeLose();
						play.finishGame();
					} else 
					{
					initializeLose();
					play.finishGame();
					}
				}
				Integer forward = CalcDistForward();
				Integer backward = CalcDistBackward();
				Integer right =  CalcDistRight();
				Integer left =  CalcDistLeft();
				Set<Integer> Directions = new HashSet<Integer>();
				if (forward != 0) {
				Directions.add(forward);
				}
				if (backward != 0) {
				Directions.add(backward);
				}
				if (right != 0) {
				Directions.add(right);
				}
				if (left != 0) {
				Directions.add(left);
				}
				
				int nearest = Collections.min(Directions);
				
				if (forward == nearest) {
					try {
						robot.move(1);
						steps++;
					} catch (Exception e) {
						initializeLose();
						play.finishGame();
					}
				} else if (backward == nearest) {
					try {
						robot.rotate(Turn.AROUND);
						robot.move(1);
						steps++;
					} catch (Exception e) {
						initializeLose();
						play.finishGame();
					}
				} else if (right == nearest) {
					try {
						robot.rotate(Turn.RIGHT);
						robot.move(1);
						steps++;
					} catch (Exception e) {
						initializeLose();
						play.finishGame();
					} 
				} else if (left == nearest) {
					try {
						robot.rotate(Turn.LEFT);
						robot.move(1);
						steps++;
					} catch (Exception e) {
						initializeLose();
						play.finishGame();
					}
				}
				
	    //}
	    play.finishGame();
	    return false;
	}
	
	/**
	 * the method initializeLose is a helper method to avoid reptitive code since this is often used
	 * in the drive2exit function
	 */
	private void initializeLose(){
		hitted = true;
		maze.state=Constants.STATE_LOSE;
		maze.notifyViewerRedraw();
	}
	
	
	private int CalcDistForward() {
		int[] curDir = robot.getCurrentDirection();
		int forwarddist = 0;
		int[] p = {0,0};
		try {
			p = robot.getCurrentPosition();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ((curDir[0] == 0 && curDir[1] == 1 && maze.mazecells.hasNoWallOnBottom(p[0], p[1])) ||
				(curDir[0] == 0 && curDir[1] == -1 && maze.mazecells.hasNoWallOnTop(p[0], p[1])) ||
				(curDir[0] == 1 && curDir[1] == 0 && maze.mazecells.hasNoWallOnRight(p[0], p[1])) ||
		    (curDir[0] == -1 && curDir[1] == 0 && maze.mazecells.hasNoWallOnLeft(p[0], p[1]))) {
			forwarddist = distMatrix[p[0] + curDir[0]][p[1] + curDir[1]];
		}
		return forwarddist;
	}
	
	private int CalcDistBackward() {
		int[] curDir = robot.getCurrentDirection();
		int[] p = {0,0};
		try {
			p = robot.getCurrentPosition();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int forwarddist = 0;
		if ((curDir[0] == 0 && curDir[1] == 1 && maze.mazecells.hasNoWallOnTop(p[0], p[1])) ||
				(curDir[0] == 0 && curDir[1] == -1 && maze.mazecells.hasNoWallOnBottom(p[0], p[1])) ||
				(curDir[0] == 1 && curDir[1] == 0 && maze.mazecells.hasNoWallOnLeft(p[0], p[1])) ||
		    (curDir[0] == -1 && curDir[1] == 0 && maze.mazecells.hasNoWallOnRight(p[0], p[1]))) {
			forwarddist = distMatrix[p[0] - curDir[0]][p[1] - curDir[1]];
		}
		
		return forwarddist;
	}
	
	private int CalcDistRight() {
		int[] curDir = robot.getCurrentDirection();
		int forwarddist = 0;
		int[] p = {0,0};
		try {
			p = robot.getCurrentPosition();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ((curDir[0] == 0 && curDir[1] == 1 && maze.mazecells.hasNoWallOnRight(p[0], p[1])) ||
				(curDir[0] == 0 && curDir[1] == -1 && maze.mazecells.hasNoWallOnLeft(p[0], p[1])) ||
				(curDir[0] == 1 && curDir[1] == 0 && maze.mazecells.hasNoWallOnTop(p[0], p[1])) ||
		    (curDir[0] == -1 && curDir[1] == 0 && maze.mazecells.hasNoWallOnBottom(p[0], p[1]))) {
			forwarddist = distMatrix[p[0] + curDir[1]][p[1] - curDir[0]];
		}
		
		return forwarddist;
	}
	
	private int CalcDistLeft() {
		int[] curDir = robot.getCurrentDirection();
		int forwarddist = 0;
		int[] p = {0,0};
		try {
			p = robot.getCurrentPosition();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if ((curDir[0] == 0 && curDir[1] == 1 && maze.mazecells.hasNoWallOnLeft(p[0], p[1])) ||
				(curDir[0] == 0 && curDir[1] == -1 && maze.mazecells.hasNoWallOnRight(p[0], p[1])) ||
				(curDir[0] == 1 && curDir[1] == 0 && maze.mazecells.hasNoWallOnBottom(p[0], p[1])) ||
		    (curDir[0] == -1 && curDir[1] == 0 && maze.mazecells.hasNoWallOnTop(p[0], p[1]))) {
			forwarddist = distMatrix[p[0] - curDir[1]][p[1] + curDir[0]];
		}
		
		return forwarddist;
	}
	
	/**Get energy consumed by the driver so far
	 */
	@Override
	public float getEnergyConsumption(){
		return 2500 - robot.getBatteryLevel();
	}
	
	/**Get the path length taken by driver
	 */
	@Override
	public int getPathLength() {
		return steps;
	}

	/**Controls keyboard input of the driver
	 */
	@Override
	public void robotKeyDown(int key) {

		// if user explores maze, 
		// react to input for directions and interrupt signal (ESCAPE key)	
		// react to input for displaying a map of the current path or of the overall maze (on/off toggle switch)
		// react to input to display solution (on/off toggle switch)
		// react to input to increase/reduce map scale
			if(robot.hasStopped()){
				//Maze maze = robot.getMaze();
				maze.state=Constants.STATE_LOSE;
				maze.notifyViewerRedraw();
				
			}
			switch (key) {
			case 1004: case 'k': case '8':
				try {
					robot.move(1);
					steps = steps + 1;
					maze.panel.invalidate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1006: case 'h': case '4':
				try {
					robot.rotate(Turn.LEFT);
					maze.panel.invalidate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1007: case 'l': case '6':
				try {
					robot.rotate(Turn.RIGHT);
					maze.panel.invalidate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 1005: case 'j': case '2':
				try {
					robot.rotate(Turn.AROUND);
					maze.panel.invalidate();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			} 
			
	}
	
	
	
	/**
	 * methods to access fields
	 */
	
	/**
	 * Width is a private field, method getWidth returns the width 
	 */
	public int getWidth(){
		return Width;
	}
	
	/**
	 * Height is a private field, method getWidth returns the height
	 */
	public int getHeight(){
		return Height;
	}
	
	/**
	 * maze is a private field, method getMaze returns the maze reference
	 */
	public Maze getMaze(){
		return maze;
	}
	
	/**
	 * robot is a private field, method getMaze returns the robot reference 
	 */
	public Robot getRobot(){
		return robot;
	}
	
	/**
	 * The distance is a private field, method getMarker returns the marker (keep track of visited cells) 
	 */
	public Distance getDistance(){
		return dist;
	}
}
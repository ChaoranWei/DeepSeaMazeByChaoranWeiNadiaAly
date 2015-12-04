package edu.wm.cs.cs301.chaoranwei.falstad;

import edu.wm.cs.cs301.chaoranwei.falstad.Robot.Turn;

public class ManualDriver implements RobotDriver{
	
	public ManualDriver() {
		super();
		// TODO Auto-generated constructor stub
	}


	public static final String maze = null;
	Robot robot;
	int Width, Height;
	Distance dist;
	
	
	boolean hasPower;
	public int steps= 0;

	

	
	@Override
	public void setRobot(Robot r) {
		r.setBatteryLevel(2500);
		hasPower = true;
		robot = r;
		
	}

	@Override
	public void setDimensions(int width, int height) {
		Width = width;
		Height = height;
	}

	/**
	 * Provides the robot driver with information on the distance to the exit.
	 * Only some drivers such as the wizard rely on this information to find the exit.
	 * @param distance gives the length of path from current position to the exit.
	 * @precondition null != distance, a full functional distance object for the current maze.
	 */

	@Override
	public void setDistance(Distance distance) {
		assert(distance != null);
		dist = distance;
		
	}

	/**
	 * Return false, i.e. fake drive2Exit method because this is a manuel driver and does not have an automatic mode
	 */
	@Override
	public boolean drive2Exit() throws Exception {
		return false;	
		}
    
	
	@Override
	public float getEnergyConsumption(){
		return 2500 - robot.getBatteryLevel();
	}
	
	@Override
	public int getPathLength() {
		return steps;
	}

	
	
	public void robotKeyDown(int key) {

		// if user explores maze, 
		// react to input for directions and interrupt signal (ESCAPE key)	
		// react to input for displaying a map of the current path or of the overall maze (on/off toggle switch)
		// react to input to display solution (on/off toggle switch)
		// react to input to increase/reduce map scale
		Maze maze = robot.getMaze();
			if(robot.hasStopped()){
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
	
	
}

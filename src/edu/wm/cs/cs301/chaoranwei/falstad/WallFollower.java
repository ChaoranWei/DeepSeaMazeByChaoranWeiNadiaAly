package edu.wm.cs.cs301.chaoranwei.falstad;

import edu.wm.cs.cs301.chaoranwei.falstad.Robot.Direction;
import edu.wm.cs.cs301.chaoranwei.falstad.Robot.Turn;
import edu.wm.cs.cs301.chaoranwei.ui.PlayActivity;

public class WallFollower implements RobotDriver {

private Maze maze;
private Robot robot;
private int Width, Height;
private int steps = 0;
private int[][] marker;
private Distance dist;
private PlayActivity play;

		public WallFollower() {
			super();
		}
	
		public void setActivity(PlayActivity p) {
			play = p;
		}

	@Override
	public void setRobot(Robot r) {
		r.setBatteryLevel(2500);
		robot = r;
		maze = robot.getMaze();
	
		
	}
	
	/*
	 * Width is a private field, method getWidth returns the width 
	 */
	public int getWidth(){
		return Width;
	}
	
	/*
	 * Height is a private field, method getWidth returns the height
	 */
	public int getHeight(){
		return Height;
	}
	
	/*
	 * maze is a private field, method getMaze returns the maze reference
	 */
	public Maze getMaze(){
		return maze;
	}
	
	/*
	 * robot is a private field, method getMaze returns the robot reference 
	 */
	public Robot getRobot(){
		return robot;
	}
	
	/*
	 * Marker is a private field, method getMarker returns the marker (keep track of visited cells) 
	 */
	public int[][] getMarker(){
		return marker;
	}
	
	/*
	 * The distance is a private field, method getMarker returns the marker (keep track of visited cells) 
	 */
	public Distance getDistance(){
		return dist;
	}
	
	
	@Override
	public void setDimensions(int width, int height) {
		Width = width;
		Height = height;
		marker = new int[Width][Height];
		for (int i = 0 ; i < Width ; i++){
			for (int j = 0; j<Height; j++){
				marker[i][j] = 0;
			}
		}
	}

	@Override
	public void setDistance(Distance distance) {
		dist = distance;
		
	}


	
	@Override
	public boolean drive2Exit() {
		//check if the robot starts inside of room
		if(robot.isInsideRoom() && !robot.hasStopped()){
			if(robot.distanceToObstacle(Direction.LEFT)>0){
				int ditToOb=robot.distanceToObstacle(Direction.LEFT);
				try {
					robot.rotate(Turn.LEFT);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					robot.move(ditToOb);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				steps= steps+ditToOb;
				marker[maze.px][maze.py] += 1;
				try {
					drive2ExitHelper();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
		}
		else{
			try {
				drive2ExitHelper();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		play.finishGame();
		return true;
	}

	
	private void drive2ExitHelper() throws Exception{
		//while (!robot.isAtGoal()){
			play.finishGame();
	    	play.energythread.run();
	    	//play.driverHandler.postDelayed(play.DriverRunnable, 200);
			if (robot.hasStopped()){
				return;
			}
				else{
					if(robot.distanceToObstacle(Direction.LEFT)>0){
						robot.rotate(Turn.LEFT);
						robot.move(1);
						marker[maze.px][maze.py] += 1;
						steps+=1;
						//drive2ExitHelper();
					}
					else if(robot.distanceToObstacle(Direction.FORWARD)>0){
						robot.move(1);
						marker[maze.px][maze.py] += 1;
						steps+=1;
						//drive2ExitHelper();

					}
					else{
						robot.rotate(Turn.RIGHT);
						marker[maze.px][maze.py] += 1;
						//drive2ExitHelper();

						}
				}
				
			//}
		return;
	}
		
	
	@Override
	public float getEnergyConsumption(){
		return 2500 - robot.getBatteryLevel();
	}
	
	@Override
	public int getPathLength() {
		return steps;
	}

	@Override
	public void robotKeyDown(int key) {
		// if user explores maze, 
		// react to input for directions and interrupt signal (ESCAPE key)	
		// react to input for displaying a map of the current path or of the overall maze (on/off toggle switch)
		// react to input to display solution (on/off toggle switch)
		// react to input to increase/reduce map scale
			if(robot.hasStopped()){
				Maze maze = robot.getMaze();
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


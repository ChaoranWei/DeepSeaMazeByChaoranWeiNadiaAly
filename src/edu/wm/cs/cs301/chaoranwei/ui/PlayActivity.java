package edu.wm.cs.cs301.chaoranwei.ui;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import edu.wm.cs.cs301.chaoranwei.falstad.BasicRobot;
import edu.wm.cs.cs301.chaoranwei.falstad.Constants;
import edu.wm.cs.cs301.chaoranwei.falstad.CuriousMouse;
import edu.wm.cs.cs301.chaoranwei.falstad.GraphicsWrapper;
import edu.wm.cs.cs301.chaoranwei.falstad.GraphicsWrapper.color;
import edu.wm.cs.cs301.chaoranwei.falstad.ManualDriver;
import edu.wm.cs.cs301.chaoranwei.falstad.Maze;
import edu.wm.cs.cs301.chaoranwei.falstad.WallFollower;
import edu.wm.cs.cs301.chaoranwei.falstad.Wizard;
import android.util.Log;
import edu.wm.cs.cs301.chaoranwei.falstad.Distance;
import edu.wm.cs.cs301.chaoranwei.falstad.GlobalMaze;
import edu.wm.cs.cs301.chaoranwei.falstad.Cells;
import edu.wm.cs.cs301.chaoranwei.falstad.BSPNode;

/**
 * Class PlayActivity is for the state play. 
 * State Play: is responsible to display the maze and let the user either watch a robot exploring 
 * the maze or allow the user to manually navigate the robot through the maze. Also displays the
 * remaining energy using a ProgressBar. The class needs to provide a feature to toggle visibility 
 * of the map plus functionality to toggle visibility of the solution on the map. 
 * @author nadia aly, Chaoran Wei
 *
 */
@SuppressLint("NewApi")
public class PlayActivity extends Activity {
	private int energy;
	private int steps;
	private String driver;
	private int skill;
	private String perspective;
	private String tag = "PlayActivity";
	private TextView energy_text;
	private Maze maze;
	private int hitwall;
	private Thread driverThread;
	public Runnable DriverRunnable;
	Bundle bundle;
	Handler handler = new Handler();
	ProgressBar EnergyBar;
	BasicRobot robot;
	public Thread energythread;
	public Handler driverHandler = new Handler();
	boolean flag = true;
	MediaPlayer player;
	String music;
		
	/**
	 * Method name: onCreate
	 * This is the main method in the class. Receives input from users choice on how to display map. If 
	 * no input previously provided will display visible walls with manual driver. Get the skill from the 
	 * Title state, defaults to 0. Also receives input on how to operate driver (manual, curious mouse, 
	 * wizard, wall follower) and operates accordingly. 
	 * Display progress bar with amount of energy the robot driver has to operate. 
	 * Supports the following choices to toggle/visibility:
	 * 1) show the whole maze from top or not (toggle).
	 * 2) show the solution in the maze or not (toggle).
	 * 3) show the currently visible walls or not (toggle)
	 * If in manual exploration mode: screen provides navigation buttons (up, down, left, right). If in
	 *  robot exploration mode: screen provides a start/pause button to start the exploration and a pause 
	 *  the animation
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		//maze = new Maze();
		//maze.init();
		
		if (savedInstanceState == null) {
		    bundle = getIntent().getExtras();
		    music = bundle.getString("music");
		    skill= bundle.getInt("skill");
		    driver = bundle.getString("driver");
		    perspective = bundle.getString("perspective");
		    //maze= (Maze) bundle.getSerializable("maze");
		    //InitializeMaze();
		   
		} else {
		    skill= savedInstanceState.getInt("skill");
		}
		maze = GlobalMaze.maze;
		Log.v(tag, "Got intent. ");
		Log.v(tag, "skill is " + Integer.toString(skill));
		Log.v(tag, "driver is " + driver);
		Log.v(tag, "perspective is " + perspective);
		Log.v(tag, Integer.toString(maze.mazeh)); //to make sure maze is not null
		
		if (driver.equals("Manual")) {
			maze.driver = new ManualDriver();
		} else if (driver.equals("Curious Mouse")) {
			maze.driver = new CuriousMouse();
			((CuriousMouse) maze.driver).setActivity(this);
		} else if (driver.equals("Wizard")) {
			maze.driver = new Wizard();
			((Wizard) maze.driver).setActivity(this);
		} else if (driver.equals("Wall Follower")) {
			maze.driver = new WallFollower();
			((WallFollower) maze.driver).setActivity(this);
		}
		robot = new BasicRobot(maze);
		maze.driver.setRobot(robot);
		maze.driver.setDistance(maze.mazedists);
		maze.driver.setDimensions(maze.mazew, maze.mazeh);
		Log.v(tag, "maze set robot and driver. ");
		
		GraphicsWrapper panel = (GraphicsWrapper) findViewById(R.id.panel);
		maze.panel = panel;
		maze.addViews();
		if (perspective.equals("From top")) {
			maze.mapMode = !maze.mapMode; 
			maze.showMaze = !maze.showMaze;
		} else if (perspective.equals("Visible walls")) {
			maze.mapMode = !maze.mapMode; 		
		} else {
			maze.mapMode = !maze.mapMode; 	
			maze.showSolution = !maze.showSolution;
		}

		maze.panel.update();
		maze.notifyViewerRedraw();
		maze.panel.invalidate();
		
		EnergyBar = (ProgressBar) findViewById(R.id.energy_progress);
		EnergyBar.setProgress(2500);
		EnergyBar.setMax(2500);
		Log.v(tag, "Energy progress bar ");
		energy_text = (TextView) findViewById(R.id.energy_text);
		//energy_text.setText(maze.robot.getBatteryLevel() +"/"+2500);
		
		energythread = new Thread(new Runnable() {
			@Override
			public void run() {
			       handler.post(new Runnable() {

			       @Override
			       public void run() {
			           EnergyBar.setProgress((int) robot.getBatteryLevel());
			           energy_text.setText((int) robot.getBatteryLevel() +"/"+2500);
			           Log.v(tag, "running");
			    	   
			           }
			        });
			     }

			});
		
		Button visiblewall = (Button) findViewById(R.id.visiblewall);
		visiblewall.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	Log.v(tag, "perspective: visible wall");
	        	maze.mapMode = !maze.mapMode; 		
				maze.notifyViewerRedraw() ; 
				maze.panel.invalidate();
			}
		});
		Log.v(tag, "visible wall button. ");
		Button solution = (Button) findViewById(R.id.solution);
		solution.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	Log.v(tag, "perspective: solution shown");
	        	maze.showSolution = !maze.showSolution; 		
				maze.notifyViewerRedraw() ; 
				maze.panel.invalidate();
			}
		});
		Log.v(tag, "solution button. ");
		Button topdown = (Button) findViewById(R.id.topdown);
		topdown.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	Log.v(tag, "perspective: top down");
	        	maze.showMaze = !maze.showMaze; 		
				maze.notifyViewerRedraw() ; 
				maze.panel.invalidate();
			}
		});
		Log.v(tag, "top down button. ");
		
		Button start_driver = (Button) findViewById(R.id.start_driver);
		start_driver.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	Log.v(tag, "starting driver...");
	        	flag = true;
	        	DriverRunnable = new DriverRunnable();
	        	driverHandler.postDelayed(DriverRunnable, 200);
	        	driverThread = new Thread(DriverRunnable);
	        	driverThread.run();
			}
		});
		Log.v(tag, "start driver. ");
		Button pause = (Button) findViewById(R.id.pause);
		pause.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	Log.v(tag, "pause driver");
	        	flag = false;
			}
		});
		Log.v(tag, "pause ");
		
		Button up = (Button) findViewById(R.id.Up);
		up.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	maze.driver.robotKeyDown(1004);
	        	Log.v(tag, "Going up button is pressed. ");
	        	energythread.run();
	        	finishGame();
			}
		});
		Log.v(tag, "Go up. ");
		Button down = (Button) findViewById(R.id.Down);
		down.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	maze.driver.robotKeyDown(1005);
	        	//Log.v(tag, Float.toString(robot.getBatteryLevel()));
	        	Log.v(tag, "Going down button is pressed. ");
	        	energythread.run();
	        	finishGame();
			}
		});
		Log.v(tag, "Go down. ");
		Button left = (Button) findViewById(R.id.Left);
		left.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	maze.driver.robotKeyDown(1006);
	        Log.v(tag, "Going left button is pressed. ");
	        energythread.run();
	        finishGame();
			}
		});
		Log.v(tag, "Go left. ");
		Button right = (Button) findViewById(R.id.Right);
		right.setOnClickListener(new View.OnClickListener() {
			/**
			 * execute the action when item is clicked
			 */
	        public void onClick(View v) {
	        	maze.driver.robotKeyDown(1007);
	        	Log.v(tag, "Going right button is pressed. ");
	        	energythread.run();
	        	finishGame();
			}
		});
		Log.v(tag, "Go right. ");
	    if (!driver.equals("Manual")) {
	    	//Log.v(tag, driver);
	    	up.setVisibility(View.INVISIBLE);
	    	down.setVisibility(View.INVISIBLE);
	    	right.setVisibility(View.INVISIBLE);
	    	left.setVisibility(View.INVISIBLE);
	    } else {
	    	start_driver.setVisibility(View.INVISIBLE);
	    	pause.setVisibility(View.INVISIBLE);
	    }
	    
	    Button restartFromPlay = (Button) findViewById(R.id.restartFromPlay);
	    restartFromPlay.setOnClickListener(new View.OnClickListener() {
	    	
	    	/**
	    	 *  method name: onClick
	    	 * Pressing the back button returns to State Title to allow the user to choose different
			 * parameter settings and restart.
	    	 */
	        public void onClick(View v) {
	        	Context activity = getApplicationContext();
            	Intent intent = new Intent(activity, AMazeActivity.class);
            	player.stop();
            	Log.v(tag, "restart game. ");
            	startActivity(intent);
			}
		});
	    Log.v(tag, "restart button. ");
	    
	    AssetFileDescriptor afd;
	    try {
			afd = getAssets().openFd("play.mp3");
			player = new MediaPlayer();
			player.setLooping(true);
			player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			player.prepare();
			if (music.equals("Music On")) {
			player.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.v(tag, "playing the game music ");
	}

	/**
	 * Method: onCreateOptionsMenu- Inflate the menu; this adds items to the action bar if it is present.
	 * @param: menu
	 * @return true
	 */
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	/**
	 * Method: onOptionsItemSelected
	 * @param: MenuItem
	 * @return: true or anOptionItemSelected(item): If the user's action was not recognized invoke the 
	 * superclass to handle it. 
	 * User choose the "Settings" item, show the app settings UI...Handle action bar item clicks here.
	 * The action bar will automatically handle clicks on the Home/Up button, so long as you specify a 
	 * parent activity in AndroidManifest.xml.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
        case R.id.action_settings:
            // User chose the "Settings" item, show the app settings UI...
            return true;

        default:
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            return super.onOptionsItemSelected(item);
		}

    }
	
	private class DriverRunnable implements Runnable{

		@Override
		public void run() {
			try {
				// do 1 step towards exit
				// reschedule this runnable
				if (flag == true) {
				maze.driver.drive2Exit();
				driverHandler.postDelayed(this, 200);
				runOnUiThread(new Runnable(){
					@Override
					public void run() {
						maze.panel.invalidate();
					}
				});
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void finishGame() {
		if ((maze.endx == maze.px && maze.endy == maze.py) || robot.hitted == true || robot.getBatteryLevel() <= 0) {
		//	maze.robot.isAtGoal()
		flag = false;
		//	maze.isEndPosition(maze.px,maze.py)
		Intent intent = new Intent(this, FinishActivity.class);
    	Log.v(tag, "finish the game. ");
    	intent.putExtra("hitwall", robot.hitted);
    	intent.putExtra("Path", maze.driver.getPathLength());
		intent.putExtra("energy", robot.getBatteryLevel());
		player.stop();
    	startActivity(intent);
	    }
	}
}

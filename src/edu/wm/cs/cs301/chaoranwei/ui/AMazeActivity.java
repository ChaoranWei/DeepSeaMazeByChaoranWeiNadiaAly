package edu.wm.cs.cs301.chaoranwei.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;

/**
 * The AMazeActivity class is the class for state title. This state displays the welcome page and 
 * takes parameter settings to start with the maze generation. The Inputs the user can select are:
 * skill-level, the size of the maze (default value of 0).  The user can select either generating 
 * a random maze or loading a maze from file. For the random generation, the user can select the
 * maze generation algorithm: (Backtracking, Prim's, and Eller’s algorithm). For loading the maze 
 * from file,  1 stored maze is offered per skill level.
 * The state title must also have a start button to start with either the random generation of a maze 
 * or loading a maze from file once the user selects the input.
 * Supported Navigation: Possible transitions are to state Generating (via parameter settings
 *  and clicking on the start button). 
 * 
 * @author Nadia Aly and Chaoran Wei
 *
 */

public class AMazeActivity extends Activity {
	private int skill = 0;
	private String algorithm;
	private TextView text;
	private int max;
	private String tag = "AMazeActivity";

	/**
	 * Method onCreate 
	 * @param savedInstanceState
	 * Displays welcome on title page, displays seek bar to select skill level for maze. Sets the max value
	 * of the skill level to 15, default value of 0. 
	 *  with the help of Log.v() message in the Eclipse Logcat window to demonstrate that it correctly received 
	 *  user input.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amaze);
		Log.v(tag, "Getting started. ");
		
		TextView welcome = (TextView) findViewById(R.id.welcome);
		Log.v(tag, "Display welcome. ");
		
		//seekbar skill level
		SeekBar skill_level = (SeekBar) findViewById(R.id.seekbar);
		skill_level.setProgress(0);
	    skill_level.setMax(15);
	    max = 15;
	    text = (TextView) findViewById(R.id.skill_level);
	    
	    skill_level.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
	    	/**
	    	 * Method Name: onProgressChanged
	    	 * @param: seekbar
	    	 * @param: progress
	    	 * @param: fromUser
	    	 * Change the skill level for the maze when the user drags the bar from left to right 
	    	 */
			@Override 
			public void onProgressChanged(SeekBar seekbar, int Progress, boolean fromUser){
				skill = Progress;
				Toast.makeText(getApplicationContext(), "Changing the skill level to" + Integer.toString(skill), Toast.LENGTH_SHORT).show();
				//setProgress(skill);
				text.setText("skill level chosen: " + Integer.toString(skill) + "/" + 15);
			}

			/**
			 * Method: onStartTracking: 
			 * Use SeekBar-The user can touch the thumb and drag left or right to set level or use the arrow keys. Start tracking. 
			 */
			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
			}

			/**
			 * Method: onStopTrackingTouch:  
			 * Use SeekBar-The user can touch the thumb and drag left or right to set level or use the arrow keys. Stop tracking. 
			 */
			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
			}
		});
	    Log.v(tag, "Seekbar to choose skill level ");
	    
	    //spinner for algorithm
	    Spinner spinner = (Spinner) findViewById(R.id.start_spinner);
	    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	            R.array.algorithms_array, android.R.layout.simple_spinner_item);
	    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    spinner.setAdapter(adapter);
	    
	    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
	    	/**
			 * execute the action when item is selected
			 */
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
				algorithm = parent.getItemAtPosition(pos).toString();
				 Log.v(tag, "Algorithm chosen is: " + algorithm);
			}
			/**
			 * execute the action when nothing is selected
			 */
			public void onNothingSelected(AdapterView<?> parent){
				//use default
			}
		});
		
	    Log.v(tag, "Spinner to choose algorithms. ");
	    /**
	     * Method onClick: 
	     * @param view
	     * The start button configuration: after user clicks to start with either the random 
	     * generation of a maze or loading a maze from file. Takes inputs for skill level and algorithm and
	     * switch to generating state (shows progress of the maze algorithm being built).
	     *  
	     */
	    //start button
	    Button start_button = (Button) findViewById(R.id.start_button);
	    start_button.setOnClickListener(new View.OnClickListener() {
	    	/**
			 * execute the action when item is clicked
			 */
            public void onClick(View v) {
            	Context activity = getApplicationContext();
            	Intent intent = new Intent(activity, GeneratingActivity.class);
    			intent.putExtra("algorithm", algorithm);
    			intent.putExtra("skill", skill);
    			Log.v(tag, "start button pressed. ");
    			startActivity(intent);
			}
		});
	    
	    Log.v(tag, "Start button. ");
	
	}

	/**
	 * Method: onCreateOptionsMenu- Inflate the menu; this adds items to the action bar if it is present.
	 * @param: menu
	 * @return true
	 */
	@Override 
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.amaze, menu);
		return true;
	}

	
	/**
	 * Method: onOptionsItemSelected
	 * @param: MenuItem
	 * @return: anOptionItemSelected(item)
	 *  Handle action bar item clicks here. The action bar will automatically handle clicks on the Home/Up button, 
	 *  so long as you specify a parent activity in AndroidManifest.xml.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

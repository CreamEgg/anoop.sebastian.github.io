package com.sebasta2.expensedairy;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

public class SplashActivity extends Activity {
	MediaPlayer player;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		player = MediaPlayer.create(SplashActivity.this, R.raw.splashsound);
		player.start();
		Thread timer = new Thread() {
    	public void run()
    	{
    		try {	
    			sleep(5000); /*sleep for 5000ms */	}
    		
    		catch(InterruptedException e){	e.printStackTrace();	}	
    		
    		finally {
    			Intent callmain  = new Intent(SplashActivity.this, ParentActivity.class);
    			startActivity(callmain);
    		}
    	}};
    
    	timer.start();
	}
	
	protected void onPause() {
		super.onPause();
		player.release();
		finish();
	}
}

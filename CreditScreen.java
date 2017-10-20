package com.tdh.tetris;

import com.tdh.Sup.MediaPlayerServices;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class CreditScreen extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  //Set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.creditlayout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	}
	
	@Override
	public void onUserLeaveHint()
	{
		MediaPlayerServices.keeplaying=false;
		stopService(new Intent(this, MediaPlayerServices.class));
	}
	
	@Override
	protected void onResume() {
		MediaPlayerServices.keeplaying=false;
		startService(new Intent(this, MediaPlayerServices.class));
		super.onResume();
	}
	
	
}

package com.tdh.tetris;

import com.tdh.Sup.MediaPlayerServices;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {
	
	protected int _splashTime = 5000;
	Handler handlersplash;
	Runnable runcablesplash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        
        startService(new Intent(getApplicationContext(), MediaPlayerServices.class));
        
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.splashlayout);
     
        handlersplash = new Handler();
        runcablesplash = new Runnable() {
            public void run() {
                finish();
                Intent intent = new Intent(SplashScreen.this, MenuScreen.class);
                SplashScreen.this.startActivity(intent);
            }
        };
        
       
        handlersplash.postDelayed(runcablesplash, 5000);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
       if (event.getAction() == MotionEvent.ACTION_DOWN) {
    	   handlersplash.removeCallbacks(runcablesplash);
    	   finish();
    	   Intent intent = new Intent(SplashScreen.this, MenuScreen.class);
           SplashScreen.this.startActivity(intent);
        }
       return true;
    }

   
}

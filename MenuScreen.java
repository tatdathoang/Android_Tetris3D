package com.tdh.tetris;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.tdh.Sup.MediaPlayerServices;
import com.tdh.Sup.Options;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;


public class MenuScreen extends Activity {

	ImageButton play;
	ImageButton option;
	ImageButton credit;
	ImageButton exit;
	ImageButton ava;
	ImageButton help;
	
	Options gop = new Options();
	
	private static final int CAMERA_REQUEST=1888; 
	Vibrator vb; 
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		//Set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.mainmenulayout);
	    
	    ava = (ImageButton)findViewById(R.id.imgbtt_mainmn_ava);
	    play = (ImageButton)findViewById(R.id.imgbtt_mainmn_Play);
        option = (ImageButton)findViewById(R.id.imgbtt_mainmn_Option);
        credit = (ImageButton)findViewById(R.id.imgbtt_mainmn_Credit);
        exit = (ImageButton)findViewById(R.id.imgbtt_mainmn_Exit);
        help= (ImageButton)findViewById(R.id.imgbtt_mainmn_Help);
        vb= (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
        
        
        setAva();
        
        ava.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				vb.vibrate(100);
				stopService(new Intent(getApplicationContext(), MediaPlayerServices.class));
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
			}
		});
        ava.setOnLongClickListener(new OnLongClickListener() {
			public boolean onLongClick(View v) {
				vb.vibrate(150);
				resetAva();
				return true;
			}
		});
    
        play.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					play.setImageResource(R.drawable.play2);
					vb.vibrate(100);
					return true;
				}
				
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					play.setImageResource(R.drawable.play1);
					stopService(new Intent(getApplicationContext(), MediaPlayerServices.class));
					Intent SwapToPlay = new Intent(getApplicationContext(), PlayScreen.class);
					Bundle bundelOption=new Bundle();
					bundelOption.putSerializable("Options", gop);
					SwapToPlay.putExtras(bundelOption);
					startActivity(SwapToPlay);
					return true;
				}
				
				return false;
			}
		});
        
        option.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					option.setImageResource(R.drawable.option2);
					vb.vibrate(100);
					return true;
				}
				
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					option.setImageResource(R.drawable.option1);
					MediaPlayerServices.keeplaying=true;
					Intent optionsIntent = new Intent(MenuScreen.this, OptionScreen.class); 					
					Bundle bundelToSend=new Bundle();
					bundelToSend.putSerializable("someOptions", gop);
					optionsIntent.putExtras(bundelToSend);
					startActivityForResult(optionsIntent,1); 		
					
					return true;
				}
				return false;
			}
         });
        
        credit.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					vb.vibrate(100);
					credit.setImageResource(R.drawable.credit2);
					return true;
				}
				
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					credit.setImageResource(R.drawable.credit1);
					MediaPlayerServices.keeplaying=true;
					Intent SwapToCredit = new Intent(getApplicationContext(), CreditScreen.class);
					startActivity(SwapToCredit);
					return true;
				}
				return false;
			}
         });
        
        exit.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {

				if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					vb.vibrate(100);
					exit.setImageResource(R.drawable.exit2);
					return true;
				}
				
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					exit.setImageResource(R.drawable.exit1);
					finish();
					stopService(new Intent(getApplicationContext(), MediaPlayerServices.class));
					return true;
				}
				return false;
			}
         });
        
        help.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				vb.vibrate(100);
				stopService(new Intent(getApplicationContext(), MediaPlayerServices.class));
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=jIVxfgDUFP8")));
			}
		});
	    
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data){

		if(requestCode ==1)
		{
			if (resultCode == RESULT_OK) 
			{
				gop=(Options) data.getSerializableExtra("Options");
			}
		}
		if (requestCode == CAMERA_REQUEST)
		{
			if (resultCode == RESULT_OK) 
			{
	            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	            saveImage(photo);
	            setAva();
			}
		}
	}
	
	private void saveImage(Bitmap photo)
    {
    	//write the bytes in file
    	try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "Tetris_ava.jpg");
			f.createNewFile();
			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();
		} 
    	catch (IOException e)
    	{
			e.printStackTrace();
		}
    }
    
	private Bitmap loadImage(){
        Bitmap photo = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "Tetris_ava.jpg");
        if(photo!=null)
        	return photo;
        return null;
    }
   
	private void setAva()
   {
	   Bitmap bitava = loadImage();
	   if(bitava!=null)
		   ava.setImageBitmap(bitava);
	   else 
		   ava.setImageResource(R.drawable.defaultava);
   }
   
    private boolean resetAva()
	{
		try
		{
	    	File file = new File(Environment.getExternalStorageDirectory() + File.separator + "Tetris_ava.jpg");
	    	file.delete();
	    	setAva();
	    	return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

    @Override
	protected void onPause() {
    	if(!MediaPlayerServices.keeplaying)
    		stopService(new Intent(this, MediaPlayerServices.class));
    	super.onPause();
	}
    
    @Override
	protected void onResume() {
    	MediaPlayerServices.keeplaying=false;
		startService(new Intent(this, MediaPlayerServices.class));
		super.onResume();
	}
	
}

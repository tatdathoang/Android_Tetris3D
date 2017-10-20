package com.tdh.tetris;

import com.tdh.Sup.*;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;


public class OptionScreen extends Activity {
	ImageButton save;
	private Integer[] array_level_spinner;
	private String[] array_difficult_spinner;
	Spinner spn_df;
	Spinner spn_slv;
	CheckBox cbxMusic;
	CheckBox cbxSFX;
	CheckBox cbxVibrate;
	
	Options gop = new Options();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.optionlayout);
	    
	    //Config for Spinners
	    array_level_spinner=new Integer[10];
        array_level_spinner[0]=1;
        array_level_spinner[1]=2;
        array_level_spinner[2]=3;
        array_level_spinner[3]=4;
        array_level_spinner[4]=5;
        array_level_spinner[5]=6;
        array_level_spinner[6]=7;
        array_level_spinner[7]=8;
        array_level_spinner[8]=9;
        array_level_spinner[9]=10;
        
        array_difficult_spinner=new String[3];
        array_difficult_spinner[0]="Normal";
        array_difficult_spinner[1]="Expert";
        array_difficult_spinner[2]="Easy";
        
        spn_df = (Spinner)findViewById(R.id.spinner_opt_dif);
        ArrayAdapter<String> adapter_df = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, array_difficult_spinner);
        spn_df.setAdapter(adapter_df);
        
        spn_slv = (Spinner)findViewById(R.id.spinner_opt_strlv);
        ArrayAdapter<Integer> adapter_lv = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, array_level_spinner);
        spn_slv.setAdapter(adapter_lv);
        
        //Config for checkBoxes
        cbxMusic = (CheckBox) findViewById(R.id.cbx_opt_music);
        cbxSFX = (CheckBox) findViewById(R.id.cbx_opt_sfx);
        cbxVibrate = (CheckBox) findViewById(R.id.cbx_opt_vib);   
        
        
	    
        try 
        {
        	Bundle bundleReciveOPfromMN=this.getIntent().getExtras();	
            gop=(Options)bundleReciveOPfromMN.getSerializable("someOptions");
            loadOption();
		} 
        catch (Exception e) 
		{
		}
        
        save = (ImageButton)findViewById(R.id.imgbtt_opt_save);
	    save.setOnTouchListener(new OnTouchListener() 
	    {
	    	public boolean onTouch(View v, MotionEvent event) 
			{
	    		if(event.getAction()==MotionEvent.ACTION_DOWN)
				{
					save.setImageResource(R.drawable.op_save2);
					return true;
				}
				
				if(event.getAction()==MotionEvent.ACTION_UP)
				{
					save.setImageResource(R.drawable.op_save1);
		    		saveOption();
		    		Bundle bundelToSend=new Bundle();
					bundelToSend.putSerializable("Options", gop);			
					Intent gotoMainMenu = new Intent(OptionScreen.this,MenuScreen.class); 
					gotoMainMenu.putExtras(bundelToSend);
					
					setResult(RESULT_OK,gotoMainMenu);
					finish();
					return true;
				}
				return false;
			}
	    	
		});
	    
	}
	
	private void saveOption()
	{
		gop.setDifficultyLevel(spn_df.getSelectedItemPosition());
		gop.setStartingLevel(spn_slv.getSelectedItemPosition()+1);
		
		gop.setMusicEnabled(cbxMusic.isChecked());
		gop.setSoundEnabled(cbxSFX.isChecked());
		gop.setEnableVibrate(cbxVibrate.isChecked());
		
	}

	private void loadOption()
	{
		spn_df.setSelection(gop.getDifficultyLevel());
		spn_slv.setSelection(gop.getStartingLevel()-1);
		
		cbxMusic.setChecked(gop.isMusicEnabled());
		cbxSFX.setChecked(gop.isSoundEnabled());
		cbxVibrate.setChecked(gop.isEnableVibrate());
	}
	
	@Override
	public void onBackPressed() {
		save.setImageResource(R.drawable.op_save1);
		saveOption();
		Bundle bundelToSend=new Bundle();
		bundelToSend.putSerializable("Options", gop);			
		Intent gotoMainMenu = new Intent(OptionScreen.this,MenuScreen.class); 
		gotoMainMenu.putExtras(bundelToSend);
		
		setResult(RESULT_OK,gotoMainMenu);
		finish();
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

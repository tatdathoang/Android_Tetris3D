package com.tdh.tetris;

import com.tdh.Sup.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.*;

@SuppressLint("WorldReadableFiles")
public class PlayScreen extends Activity
{
	private GLGameSurfaceView gsf;
    private GameOverlay overlay;
    private View gameHudView;
    private Options options;
    private Options OptionRecives;
    
    private Game game;
    public SharedPreferences.Editor prefsEditor;
    public SharedPreferences prefs;

    @Override
    public void onCreate(Bundle icicle) 
    {
    	//Set full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        super.onCreate(icicle);
        prefs = this.getSharedPreferences("SAVEGAMEDATA",android.content.Context.MODE_WORLD_READABLE);
        prefsEditor = prefs.edit();
        initActivity();
    }

    public void initActivity()
    {       
        
        game = new TetricGameData();        
        
        try
        {
        	Bundle bundleReciveFromMN=this.getIntent().getExtras();
        	setOptionRecives((Options)bundleReciveFromMN.getSerializable("Options"));
        }
        catch (Exception e) 
        {}
        
        //Option int
        options = new Options(game,getOptionRecives().getDifficultyLevel(),getOptionRecives().getStartingLevel(),getOptionRecives().isMusicEnabled(),getOptionRecives().isSoundEnabled(),getOptionRecives().isEnableVibrate());
        
        options.setSkins(getOptionRecives().getSkins());
        
        overlay = new GameOverlay(this,options);
        overlay.setVisibility(View.VISIBLE);
        overlay.setOverlayType(GameOverlay.OVERLAY_TYPE_INTRO);
        
        gameHudView = View.inflate(this, R.layout.gamelayout, null);
        gsf = new GLGameSurfaceView(this,overlay);
        
        setContentView(gsf);
        gsf.setVisibility(View.VISIBLE); 

        this.addContentView(overlay,new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
        this.addContentView(gameHudView, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT,android.view.ViewGroup.LayoutParams.FILL_PARENT));
        
        gameHudView.findViewById(R.id.ImageButtonL).setOnClickListener(
        		new View.OnClickListener()
				{
					public void onClick(View view)
					{
						try
			        	{
			        		gsf.queueEvent(new Runnable(){
			                    public void run() {
			                        gsf.getRenderer().setAction(GameRenderer.MSG_MOVE_LEFT, 0, 0);
			                    }});	
			        	}
			        	catch(Exception e)
			        	{}
					}
				}
			);		
        gameHudView.findViewById(R.id.ImageButtonR).setOnClickListener(
        		new View.OnClickListener()
				{
					public void onClick(View view)
					{
						try
			        	{
			        		gsf.queueEvent(new Runnable(){
			                    public void run() {
			                        gsf.getRenderer().setAction(GameRenderer.MSG_MOVE_RIGHT, 0, 0);
			                    }});        		
			        	}
			        	catch(Exception e)
			        	{}
					}
				}
			);	
        gameHudView.findViewById(R.id.ImageButtonRT).setOnClickListener(
        		new View.OnClickListener()
				{
					public void onClick(View view)
					{
						try
			        	{
			        		gsf.queueEvent(new Runnable(){
			                    public void run() {
			                        gsf.getRenderer().setAction(GameRenderer.MSG_ROTATE, 0, 0);
			                    }});	
			        	}
			        	catch(Exception e)
			        	{}
					}
				}
			);		
        
        gameHudView.findViewById(R.id.ImageButtonD).setOnClickListener(
        		new View.OnClickListener()
				{
					public void onClick(View view)
					{
						try
			        	{
			        		gsf.queueEvent(new Runnable(){
			                    public void run() {
			                        gsf.getRenderer().setAction(GameRenderer.MSG_MOVE_DOWN, 0, 0);
			                    }});	
			        	}
			        	catch(Exception e)
			        	{}
					}
				}
			);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	gsf.onResume();
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	gsf.onPause();
    	//System.exit(0);
    }
 
    public void playGame()
    {	
		gsf.initGame(GameRenderer.VIEW_GAME);			
    }
   
    public void showMenu()
    {   
    	Intent menuIntent = new Intent(PlayScreen.this, CreditScreen.class); 
    	PlayScreen.this.startActivity(menuIntent);
    	PlayScreen.this.finish(); 
    }

    public void exitApplication()
    {
			finish();
    }
    
    public void exitNotAcceptedApplication()
    {
			finish();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
        case R.id.newgame:
            playGame();
            return true;
        case R.id.mainmenu:
        	showMenu();
        	return true;   
        case R.id.savegame:
        	saveGame();
        	return true;
        case R.id.loadgame:
        	loadGame();
        	return true;
        }       
        return super.onOptionsItemSelected(item);
    }
    
    private void loadGame() 
    {
		gsf.initGame(GameRenderer.VIEW_GAME);			
		this.gsf.getRenderer().game.loadGame(this.prefs);
	}

	private void saveGame() 
    {		
		this.gsf.getRenderer().game.saveGame(this.prefs,prefsEditor);	
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
	{
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ingamemenu, menu);    
               
        return true;
    }

	public void setOptionRecives(Options optionRecives) {
		OptionRecives = optionRecives;
	}

	public Options getOptionRecives() {
		return OptionRecives;
	}          

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent SwapToCredit = new Intent(getApplicationContext(), CreditScreen.class);
		startActivity(SwapToCredit);
	}
}
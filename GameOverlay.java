package com.tdh.Sup;

import com.tdh.tetris.R;

import android.view.View;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;

import android.content.res.Resources;;
@SuppressLint("ViewConstructor")
public class GameOverlay extends View 
{	
	private Paint gameOverPaint;
	private Paint statusTextPaint1;
	private Paint statusTextPaint2;
	private int gameOverXPos;
	private Resources res;
	private int goalpha;
	private int direction;
	private long lastDrawTime;
	private Options options;
	int currentCharacter=0;
	int currentCharacterPosition=0;
	
	public static final int OVERLAY_TYPE_INTRO = 0;
	public static final int OVERLAY_TYPE_GAME=1;
	public static final int OVERLAY_TYPE_OPTIONS=2;
	
	public static final int DRAW_NORMAL=0;
	public static final int DRAW_NAME_ENTRY=1;
	
	
	public GameOverlay(Context context, Options options)
	{
		super(context);
		this.options = options;
		this.overlayType = OVERLAY_TYPE_GAME;
		this.drawType = DRAW_NORMAL;
		res = context.getResources();
		gameOverPaint = new Paint();
        gameOverPaint.setARGB(255, 0, 255, 0);
        gameOverPaint.setTextSize(36);
        statusTextPaint1 = new Paint();
        statusTextPaint2 = new Paint();
        statusTextPaint1.setARGB(200, 255, 0, 0);
        statusTextPaint1.setTextSize(14);
        statusTextPaint2.setTextSize(14);
        statusTextPaint2.setARGB(255, 128, 128, 128);
        score="0";
        level="1";
        lines="0";
        goalpha=0;
        direction=1;
        
        this.lastDrawTime = System.currentTimeMillis();
	}
	
	@Override protected void onDraw(Canvas canvas)
	{		
		goalpha=goalpha+direction;
		if(goalpha>255)
		{
			direction=-4;
			goalpha=goalpha+direction;
		}
		if(goalpha<16)
		{
			direction=4;
			goalpha=goalpha+direction;
		}
		
		this.gameOverPaint.setAlpha(goalpha);
        this.gameOverXPos = getTextWidth(res.getString( R.string.s_game_over),gameOverPaint);
		
        switch (overlayType)
		{
		case OVERLAY_TYPE_INTRO:
			drawIntroOverlay(canvas);
			break;
		case OVERLAY_TYPE_GAME:
			drawTetricGameOverlay(canvas);
			break;
		}
        
	}
	
    public int getTextWidth(String str,Paint paint)
    {
    	float widths[] =new float[str.length()];
    	paint.getTextWidths(str,widths); 
    	float totalwidth=0;
    	for(int i=0;i<widths.length;i++)
    	{
    		totalwidth+=widths[i];
    	}
    	return (int)totalwidth;
    }
   
	private void drawIntroOverlay(Canvas canvas)
	{
		
		Long now = System.currentTimeMillis();
		int index = (int)((now-this.lastDrawTime)%20000);
		
		String logo=res.getString(R.string.app_name);
		
		if (index<9000)
		{
			logo = res.getString(R.string.s_press_menu_to_play);
		}
		
		if(index>9000 && index<11000)
		{
			//break time
			logo = "";
		}
						
		else
		{
			int textxpos = this.getTextWidth(logo,this.gameOverPaint);
			canvas.drawText(logo, (canvas.getWidth()-textxpos)/2, (canvas.getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
		}
	}
	
    public void drawString(Canvas canvas,String str,int x,int y)
    {
    	canvas.drawText(str, x+1, y+1, statusTextPaint2);
    	canvas.drawText(str, x, y, statusTextPaint1);
   }
    
	private void drawTetricGameOverlay(Canvas canvas)
	{
        this.drawString(canvas, res.getString(R.string.s_score), 10, 14);
        this.drawString(canvas,""+score, 10, 34);
        this.drawString(canvas,res.getString(R.string.s_level), 10, 54);
        this.drawString(canvas,""+level, 10, 74);
        this.drawString(canvas,res.getString(R.string.s_lines), 10, 94);
        this.drawString(canvas,""+lines,10,114);
        if(message.equals("Game Over"))
        {
        	canvas.drawText(res.getString(R.string.s_game_over), (getWidth()-this.gameOverXPos)/2, (getHeight()-gameOverPaint.getTextSize())/2, gameOverPaint);
        }
	}
	
	private String score;

	public void setScore(String score)
	{
		this.score = score;
	}
	
	private String level;
	
	public void setLevel(String level)
	{
		this.level = level;
	}
	
	private String lines;
	
	public void setLines(String lines)
	{
		this.lines = lines;
	}
	
	private String message;
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	private int overlayType;
	
	public void setOverlayType(int overlayType)
	{
		this.overlayType = overlayType;
	}
	
	@SuppressWarnings("unused")
	private int drawType;
	
	public void setDrawType(int drawType)
	{
		if(drawType == DRAW_NAME_ENTRY)
		{
			this.currentCharacter = 0;
			this.currentCharacterPosition=0;
		}
		this.drawType = drawType;
	}

	public Options getOptions()
	{
		return this.options;
	}
}

package com.tdh.Sup;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.tdh.tetris.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView.Renderer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;

public class GameRenderer implements Renderer 
{
	public static final int VIEW_INTRO=0;
	public static final int VIEW_GAME=1;
	public static final int MSG_DO_NOTHING=-1;
	public static final int MSG_ROTATE=0;
	public static final int MSG_MOVE_LEFT=1;
	public static final int MSG_MOVE_RIGHT=2;
	public static final int MSG_MOVE_DOWN=3;
	public static final int MSG_ROTATE_PLAYFIELD=4;

	public SoundPoolManager spm;
	private MediaPlayer mdp;
	public int place, rotate, speech, gameover, ingame, exploding1, exploding2, exploding3, exploding4, moveleft, moveright, movedown;
	public Game	game;
    public VibrateManager vbm;
    private int status;
    private Starfield		mStarfield;
    private Cube[]          mCube;
    private Cube 			mPlayfieldCube;
    private Square			mMoon;
    
    private Square			mExplosionRing;
    
	@SuppressWarnings("unused")
	private float           mAngle;
    private float			rangle;
    
    public float zx=0.0f;
    public float zy=0.0f;
    public float xy=0.0f;
    private float xoff;
    private float yoff;
    private float zoff;
    private long now;
    private long lastcalltime;
    public boolean running;
    private int viewType;
    public String message;
    
    private java.util.LinkedList<ExplodingRing> explodingRings;
    private java.util.LinkedList<ExplodingCube> explodingCubes;
    
    public static final float Y_ACCELERATION=-0.3f;
    public static final float Z_ACCELERATION=0.3f;
    
    public static final int MAX_EXPLOSION_FRAME=100;
    
    public int mWidth;
    public int mHeight;
    public boolean mSizeChanged = true;
    
    private long lastdrawtime;
    private boolean backgroundInitialized;
    
    private GameOverlay overlay;
	private java.util.Random randomgen;
	Vibrator vb;
    public int action;
    private android.content.Context context;
    private LinearInterpolator[][][] linearInterpolators;
    private boolean sayGameOver;
    private GLTextures textures;
    private long startGameTime;
    private long timeaccumulator;
    private int w;
    private int h;
    private boolean isPaused;
  
	public GameRenderer( android.content.Context context,GameOverlay overlay, Options op)
	{	
		isPaused = false;
		this.overlay = overlay;
		this.context = context;

		if(op.isMusicEnabled()||op.isSoundEnabled())
		{
			initSound();
		}
		
		vbm = new VibrateManager(context);
		
        mCube = new Cube[8];

        for(int i = 0; i <7 ; i++)
        {
        	mCube[i] = new Cube(op.getSkincolor(i).getRed(),op.getSkincolor(i).getGreen(),op.getSkincolor(i).getBlue(),0xffff);
        }
        
        mStarfield = new Starfield(100,90.0f);	
        mMoon = new Square(0xffff,0xffff,0xffff,0xffff);
        
        mExplosionRing = new Square(0xffff,0x0ffff,0xffff,0xffff);
        mExplosionRing = new Square(0xffff,0x0ffff,0xffff,0xffff,true,true);
       

        this.mPlayfieldCube = new Cube(0xf000,0xf000,0xf000,0xf000);
        running=false;
        
        this.explodingCubes = new java.util.LinkedList<ExplodingCube>();
        this.explodingRings = new java.util.LinkedList<ExplodingRing>();
        
        randomgen = new java.util.Random(SystemClock.uptimeMillis());

        new java.util.Random(SystemClock.uptimeMillis());
        this.game = overlay.getOptions().getGame();
        action = MSG_DO_NOTHING;
        this.initLinearInterpolators();		
		vb = (Vibrator) this.overlay.getContext().getSystemService(Context.VIBRATOR_SERVICE);
		
	}
		
	public void initSound()
	{
		
		if(this.overlay.getOptions().isSoundEnabled())
  		{
			this.spm = new SoundPoolManager(context);
			
			place = spm.load(R.raw.place);
			rotate = spm.load(R.raw.rotate);
			speech = spm.load(R.raw.speech);
			gameover = spm.load(R.raw.gameover);
			exploding1 = spm.load(R.raw.exploding1);
			exploding2 = spm.load(R.raw.exploding2);
			exploding3 = spm.load(R.raw.exploding3);
			exploding4 = spm.load(R.raw.exploding4);
			moveleft = spm.load(R.raw.moveleft);
			moveright = spm.load(R.raw.moveright);
			movedown = spm.load(R.raw.movedown);
  		}
		
		if(this.overlay.getOptions().isMusicEnabled())
  		{
			mdp= MediaPlayer.create(context, R.raw.ingame);
			mdp.setLooping(true);
			mdp.setVolume(0.3f, 0.3f);
			mdp.start();
  		}
	}
	
	public void onDrawFrame(GL10 gl) 
	{
		if(!this.isPaused)
		{
			this.drawFrame(gl);
		}
	}

	public void onPause()
	{
		this.isPaused = true;

		if(this.mdp!=null)
		{
			mdp.stop();
		}
	   
	}
	
	public void onResume()
	{
		this.isPaused = false;
		this.lastcalltime = System.currentTimeMillis();
		if(overlay.getOptions().isMusicEnabled())
		{
			mdp.start();
		}
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) 
	{
		Log.d("GameRenderer", "onSurfaceChanged");
		w=width;
		h=height;
	    gl.glViewport(0, 0, w, h);	
	}
	
	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{
	    Log.d("GameRenderer", "onSurfaceCreated");
	    
		if(this.textures!=null)
		{
			this.textures = null;
		}
		this.textures = new GLTextures(gl,this.context);
		this.textures.add(R.drawable.moon2small);
		this.textures.add(R.drawable.ringsmall);
		this.textures.loadTextures();
		
		mMoon.setTextureId(R.drawable.moon2small);
		mMoon.setTextures(this.textures);
		
		mExplosionRing.setTextureId(R.drawable.ringsmall);
		mExplosionRing.setTextures(textures);
		this.reinit();
        
	}
	
	public void setViewType(int viewtype)
	{
		this.viewType = viewtype;
	}
	
	public void reinit()
	{
        zx=0.0f;
        zy=0.0f;
        xy=0.0f;
        xoff = -10.0f;
    	yoff = 21.0f;
    	zoff = -50.0f;
        
    	this.sayGameOver = true;
        game = overlay.getOptions().getGame();
        
        game.initGame(this.overlay.getOptions().getStartingLevel());
        game.setStatus(Game.STATUS_PLAYING);
        
        this.lastcalltime = SystemClock.uptimeMillis();
        this.startGameTime = this.lastcalltime;
        rangle=0;	
        this.running = true;
       
        this.timeaccumulator = 0;
        
        if(this.overlay.getOptions().isSoundEnabled())
		{
			spm.play(speech);
		}
        if(this.overlay.getOptions().isMusicEnabled())
        {
	        if(!mdp.isPlaying())
	        	mdp.start();
        }
	}
	
    public synchronized void doMoveDown()
    {
    	if(game.getStatus()!=Game.STATUS_PLAYING)
    	{
    		return;
    	}
    	if(this.overlay.getOptions().isSoundEnabled())
  		{
  			spm.play(movedown);
  		}
    	game.moveBlockDown();
    	
    	game.gameLoop();
    	
    	
    	if(game.isBlockPlaced())
    	{
    		if(this.overlay.getOptions().isSoundEnabled())
    		spm.play(place);
		}
		
    	game.flagCompletedLines();
    	
    	switch (game.getClearedLineCount()) 
    	{
			case 1:
				if(overlay.getOptions().isSoundEnabled())
					spm.play(exploding1);
				if(overlay.getOptions().isEnableVibrate())
					vbm.Vibtime(100);
				break;
			case 2:
				if(overlay.getOptions().isSoundEnabled())
					spm.play(exploding2);
				if(overlay.getOptions().isEnableVibrate())
					vbm.Vibtime(100);
				break;
			case 3:
				if(overlay.getOptions().isSoundEnabled())
					spm.play(exploding3);
				if(overlay.getOptions().isEnableVibrate())
					vbm.Vibtime(100);
				break;
			case 4:
				if(overlay.getOptions().isSoundEnabled())
					spm.play(exploding4);
				if(overlay.getOptions().isEnableVibrate())
					vbm.Vibtime(100);
				break;
			default:
				break;
		}

    	this.createExplosions(game);
    }
    
    public synchronized void doMoveLeft()
    {
    	if(game.getStatus()!=Game.STATUS_PLAYING)
    	{
    		return;
    	}
    	if(overlay.getOptions().isSoundEnabled())
		{
			spm.play(moveleft);
		}
		game.moveBlockLeft();
    }
    
    public synchronized void doMoveRight()
    {
    	if(game.getStatus()!=Game.STATUS_PLAYING)
    	{
    		return;
    	}
    	
    	if(overlay.getOptions().isSoundEnabled())
		{
			spm.play(moveright);
		}
    	game.moveBlockRight();
    }
    
    public synchronized void doRotateBlock()
    {
    	if(game.getStatus()!=Game.STATUS_PLAYING)
    	{
    		return;
    	}
		if(overlay.getOptions().isSoundEnabled())
		{
			spm.play(rotate);
		}
    	game.rotateCurrentBlockClockwise();
    }
    
    protected void drawCubeExplosion(GL10 gl)
    {
        java.util.ListIterator<ExplodingCube > iter=this.explodingCubes.listIterator();
        ExplodingCube c = null;
        long now = System.currentTimeMillis();
        if(iter!=null)
        {
                
                while(iter.hasNext())
                {
                        
                        c=iter.next();
                        if(c.frame>MAX_EXPLOSION_FRAME)
                        {
                                
                                iter.remove();
                        }
                        else
                        {
                                float elapsedTime = (now- c.startTime)/1000.0f;
                                drawExplodingCube(gl, c);
                                switch(c.explosionType)
                                {
                                case 1:
                                        c.x = c.x + c.ux*elapsedTime;
                                        c.y = c.y + c.uy*elapsedTime;
                                        c.z = c.z + c.uz*elapsedTime;
                                        c.uy = c.uy+elapsedTime*Y_ACCELERATION;
                                        break;
                                case 2:
                                        c.x = c.x + c.ux*elapsedTime;
                                        c.y = c.y + c.uy*elapsedTime;
                                        c.z = c.z + c.uz*elapsedTime;
                                        c.uy = c.uy+elapsedTime*Y_ACCELERATION;
                                        
                                        break;
                                case 3:
                                        c.x = c.x + c.ux*elapsedTime;
                                        c.y = c.y + c.uy*elapsedTime;
                                        c.z = c.z + c.uz*elapsedTime;
                                        c.uy = c.uy+elapsedTime*Y_ACCELERATION;
                                        break;
                                case 4:
                                        c.x = c.x + c.ux*elapsedTime;
                                        c.y = c.y + c.uy*elapsedTime;
                                        c.z = c.z + c.uz*elapsedTime;
                                        c.uy = c.uy+elapsedTime*Y_ACCELERATION;
                                        break;

                                        default:
                                        c.x = c.x + c.ux*elapsedTime;
                                        c.y = c.y + c.uy*elapsedTime;
                                        c.z = c.z + c.uz*elapsedTime;
                                        c.uy = c.uy+elapsedTime*Y_ACCELERATION;

                                                break;
                                }
                                c.frame++;
                        }
                }
        }
    }
    
    protected void drawExplodingCube(GL10 gl, ExplodingCube c)
    {
        Cube dc = this.mCube[c.blocktype];
        gl.glLoadIdentity();
        dc.setPosition(c.x, c.y, c.z);
        dc.draw(gl);
    }
    
    public synchronized void doRotatePlayfield(int zx,int zy)
    {
    	this.zx = zx;
    	this.zy = zy;
    }
    
    public synchronized void doRotatePlayfieldExpert(long time)
    {
    	long offset = time-startGameTime;
    	long chooser = offset%60000;
    	xy=this.linearInterpolators[1][0][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    	zy=this.linearInterpolators[1][1][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    	zx=this.linearInterpolators[1][2][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    }
    
    public synchronized void doRotatePlayfieldNormal(long time)
    {
    	long offset = time-startGameTime;
    	long chooser = offset%60000;
    	xy=this.linearInterpolators[0][0][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    	zy=this.linearInterpolators[0][1][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    	zx=this.linearInterpolators[0][2][overlay.getOptions().getGame().getLevel()-1].getValue((int)chooser);
    }    
    
    protected void drawNextPiece(GL10 gl)
    {    	    	
    	if(game.getNextBlock().color>=0 && game.getNextBlock().color<this.mCube.length)
    	{
    		gl.glLoadIdentity();
    		Cube c = this.mCube[game.getNextBlock().color];
    		gl.glTranslatef(18.0f, 0.0f, 0.0f);
    		gl.glRotatef(rangle, 0.0f, 0.0f, 2.0f);
    		gl.glTranslatef(-8.0f,-2.0f ,0.0f );
    		for(int i=0;i<4;i++)
    		{
    			gl.glPushMatrix();
    			c.setPosition((game.getNextBlock().xPos+game.getNextBlock().subblocks[i].xpos)*2.0f,-(game.getNextBlock().yPos+game.getNextBlock().subblocks[i].ypos)*2.0f,zoff);
    			c.draw(gl);
    			gl.glPopMatrix();
    		}
    	}
    }
    
    protected void drawBlocks(GL10 gl,Game thegame)
    {
    	for(int y=0;y<20;y++)
    	{
    		for(int x=0;x<10;x++)
    		{
    			if(thegame.getGridValue(x, y)!=-1)
    			{
    				Cube c = this.mCube[thegame.getGridValue(x, y)];
    				gl.glLoadIdentity();
    				c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
    				c.draw(gl);
    			}
    		}
    	}
    }
      
    protected void drawBlocks(GL10 gl)
    {
    	for(int y=0;y<20;y++){
	    	for(int x=0;x<10;x++){
	    		if(game.getGridValue(x, y)!=-1){
	    			Cube c = this.mCube[game.getGridValue(x, y)];
	    			gl.glLoadIdentity();
	    			c.setPosition(xoff+x*2.0f, yoff-y*2.0f, zoff);
	    			c.draw(gl);
	    		}
	    	}
    	}
    }    
    
    protected void drawFallingBlock(GL10 gl, int result)
    {
    	float offset = 0.0f;

    	offset = (float)(this.timeaccumulator%game.getTimer())/(float)game.getTimer();
    	
    	float ystart=21.0f;
    	if(game.getCurrentBlock().color>=0 && game.getCurrentBlock().color<this.mCube.length)
    	{
    		Cube c = this.mCube[game.getCurrentBlock().color];
    	
    		for(int i=0;i<4;i++)
    		{
    			gl.glLoadIdentity();
    			if(result!=0 && game.canMoveBlockDown())
    			{
    				c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart-offset*2.0f,zoff);
    			}
    			else
    			{
    				c.setPosition(-10.0f+(game.getCurrentBlock().xPos+game.getCurrentBlock().subblocks[i].xpos)*2.0f,-(game.getCurrentBlock().yPos+game.getCurrentBlock().subblocks[i].ypos)*2.0f+ystart,zoff);
    			}
    			c.draw(gl);
    		}
    	}
    }    
    
    protected void drawRingExplosions(GL10 gl)
    {
    	java.util.ListIterator<ExplodingRing> iter=this.explodingRings.listIterator();
        ExplodingRing r = null;
        if(iter!=null)
        {
            while(iter.hasNext())
            {
                r=iter.next();
                if(r.frame>7)
                {
                    iter.remove();
                }
                else
                {
                    this.mExplosionRing.setPosition(r.x, r.y, r.z);
                    float scale= r.frame*2.4f;
                    mExplosionRing.draw(gl,scale,1.0f,scale,90.0f);
                    r.frame=r.frame+1;
                }
            }
        }    	
    }
    
    public void createExplosions(Game game)
    {
    	 int[] clearedLines = game.getClearedLines();
         int linecount=game.getClearedLineCount();
         
         for (int y=0;y<clearedLines.length;y++)
         {
             if (clearedLines[y]==1)
             {
                 for(int x=0;x<10;x++)
                 {
                     ExplodingCube c = new ExplodingCube
                     (
                         xoff+x*2.0f,
                         yoff-y*2.0f,
                         zoff,
                         (randomgen.nextFloat()-0.5f)/4.0f,
                         (randomgen.nextFloat()-0.5f)*8,
                         (randomgen.nextFloat()-0.5f)*4,
                         game.getGridValue(x, y),
                         0
                                     
                     );
                     switch(linecount)
                     {
                     case 1:
	                     c.explosionType=0;                                         
	                     break;
                     case 2:
                         c.explosionType=1;                                                                         
                         break;
                     case 3:
                         c.explosionType=2;
                         break;
                     case 4:
                         c.explosionType=3;                                         
                         break;
                     default:
                         c.explosionType=0;                                         
                         break;
                     }
                     c.explosionType=0;
                     
                     this.explodingCubes.add(c);
                     ExplodingRing r = new ExplodingRing(xoff+9.0f,yoff-y*2.0f,zoff);
                     this.explodingRings.add(r);
                 }
             }
         }
    }

    protected void drawPlayfield(GL10 gl)
    {
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff-2.0f,yoff-9.5f*2.0f, zoff);
		mPlayfieldCube.draw(gl,1.0f, 20.0f,1.0f);
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff+2.0f*10,yoff-9.5f*2.0f, zoff);
		mPlayfieldCube.draw(gl,1.0f,20.0f,1.0f);
    	gl.glLoadIdentity();
		mPlayfieldCube.setPosition(xoff+4.5f*2.0f,yoff-(20*2.0f), zoff);
		mPlayfieldCube.draw(gl,10.0f,1.0f,1.0f);
    }
       
    private void drawFrame(GL10 gl)
	{
        float ratio = (float)w / h;
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        
        android.opengl.GLU.gluPerspective(gl, 60, ratio, 1, 130);

		long current = SystemClock.uptimeMillis();
		
		rangle=rangle+((current-lastdrawtime)/1000.0f)*2.0f;
     	if(rangle>360.0f)
     	{
     		rangle=0.0f;
     	}			
     	lastdrawtime=current;
     	overlay.postInvalidate(); 

        gl.glDisable(GL10.GL_DITHER);       
        gl.glClearColor(0,0,0,0);
        gl.glEnable(GL10.GL_SCISSOR_TEST);
        gl.glScissor(0, 0, w, h);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_DEPTH_TEST);            

        gl.glScalef(0.5f, 0.5f, 0.5f);
        gl.glTranslatef(0, 0, zoff);
        
        gl.glRotatef(zx, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(zy, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(xy, 0.0f, 0.0f, 1.0f);
        gl.glTranslatef(0,0,-zoff);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();
        
        gl.glColor4f(0.7f, 0.7f, 0.7f, 1.0f);
      	gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
      	gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        if(!this.backgroundInitialized)
        {
        	this.backgroundInitialized = true;
        }
        
        switch(this.viewType)
        {
        	case VIEW_GAME:
	            switch (game.getStatus())
	            {
        			case Game.STATUS_GAME_OVER:
        			
		            	this.overlay.setMessage("Game Over");
		            	if(this.sayGameOver)
		            	{
		            		 if(this.overlay.getOptions().isMusicEnabled())
	            		        if(mdp.isPlaying())
	            		        	mdp.pause();
		            		 if(this.overlay.getOptions().isSoundEnabled())
		            			 spm.play(gameover);
		            		this.sayGameOver = false;
		            	}
		            	break;		            
		            default:
		            	this.overlay.setMessage("");
		            break;
	            	}
	            
            	if (action == MSG_ROTATE)
        		{
        			action=MSG_DO_NOTHING;
        			doRotateBlock();
        		}
        		if (action == MSG_MOVE_LEFT)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveLeft();
        		}
        		if (action == MSG_MOVE_RIGHT)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveRight();
        		}
        		if (action == MSG_MOVE_DOWN)
        		{
        			action=MSG_DO_NOTHING;
        			doMoveDown();
        		} 
        		
        		
        		if(this.overlay.getOptions().getDifficultyLevel() == Options.DIFFICULTY_EXPERT)
        		{
        			this.doRotatePlayfieldExpert(current);
        		}
        		if(this.overlay.getOptions().getDifficultyLevel()==Options.DIFFICULTY_NORMAL)
        		{
        			this.doRotatePlayfieldNormal(current);
        		}
        		
        		gl.glLoadIdentity();	
            	
        		mMoon.setPosition(xoff+13,yoff-22, zoff-50);
        		mMoon.draw(gl,18.0f,18.0f,1.0f);
        		gl.glLoadIdentity();
        		gl.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            	gl.glLoadIdentity();
            	mStarfield.draw(gl,0,rangle);        		
        		drawPlayfield(gl);

                drawNextPiece(gl);

        		this.overlay.setLevel(this.overlay.getOptions().getGame().getLevelName());
        		this.overlay.setScore(""+this.overlay.getOptions().getGame().getScore());
        		this.overlay.setLines(""+this.overlay.getOptions().getGame().getLines());

        		
	            Cube c = this.mCube[0];
	            c.setPosition(0.0f, 0.0f, -30.0f);
	            mAngle += 1.2f;
	            
	            now = SystemClock.uptimeMillis();
	            long deltatime = now-lastcalltime;
	            timeaccumulator+=deltatime;
	            long simsteps = timeaccumulator/game.getTimer();
	            long remainder = timeaccumulator%game.getTimer();
	            int blockoffset = 0;
	            
            	if(game.getStatus()==Game.STATUS_PLAYING)
            	{
            		if(simsteps>0)
            		{
            			for(long i=0;i<simsteps;i++)
            			{		
            				game.gameLoop();
            				if(game.isBlockPlaced())
            				{   
            					if(this.overlay.getOptions().isSoundEnabled())
            			    	spm.play(place);
            				}
            				game.flagCompletedLines();
            				switch (game.getClearedLineCount()) 
            		    	{
            		    	case 1:
            					if(overlay.getOptions().isSoundEnabled())
            						spm.play(exploding1);
            					if(overlay.getOptions().isEnableVibrate())
            						vbm.Vibtime(100);
            					break;
            				case 2:
            					if(overlay.getOptions().isSoundEnabled())
            						spm.play(exploding2);
            					if(overlay.getOptions().isEnableVibrate())
            						vbm.Vibtime(100);
            					break;
            				case 3:
            					if(overlay.getOptions().isSoundEnabled())
            						spm.play(exploding3);
            					if(overlay.getOptions().isEnableVibrate())
            						vbm.Vibtime(100);
            					break;
            				case 4:
            					if(overlay.getOptions().isSoundEnabled())
            						spm.play(exploding4);
            					if(overlay.getOptions().isEnableVibrate())
            						vbm.Vibtime(100);
            					break;
            				default:            					
            					break;
            				}
            				this.createExplosions(game);
            				
            			}
            			timeaccumulator=remainder;
            		}
            		blockoffset=((int)remainder*10)/game.getTimer();            		
            	}
            	
            	lastcalltime = now;
            	drawFallingBlock(gl,blockoffset);
                drawBlocks(gl);	
                if(game.getStatus()==Game.STATUS_PLAYING)
                {
                    this.drawCubeExplosion(gl);
                    this.drawRingExplosions(gl);
                }
        	break;
       	}          
        	gl.glPopMatrix();
    }
    
    public GameOverlay getGameOverlay()
    {
    	return this.overlay;
    }
    
    public void setAction(int action,int arg1, int arg2)
    {
    	this.action = action;    			
    	if(action==MSG_ROTATE_PLAYFIELD)
    	{
			if(getGameOverlay().getOptions().getDifficultyLevel()== Options.DIFFICULTY_EXPERT)
			{
			}
			else
			{
				doRotatePlayfield(arg1,arg2);
			}   		
    	}
    }

    public void initLinearInterpolators()
    {
    	linearInterpolators = new LinearInterpolator[2][3][10];
    	
    	for(int a=0;a<2;a++)
    	{
    		for(int b=0;b<3;b++)
    		{
    			for(int c=0;c<10;c++)
    			{
    				linearInterpolators[a][b][c] = new LinearInterpolator();
    			}
    		}
    	}
    	

    	linearInterpolators[0][0][0].addValue(0, 0);        //level 1 Normal
    	linearInterpolators[0][0][0].addValue(0, 60000);
    	linearInterpolators[0][1][0].addValue(0,0);
    	linearInterpolators[0][1][0].addValue(60, 30000);
    	linearInterpolators[0][1][0].addValue(0, 60000);
    	linearInterpolators[0][2][0].addValue(0, 0);
    	linearInterpolators[0][2][0].addValue(0, 60000);

    	
    	linearInterpolators[0][0][1].addValue(0, 0);         //level 2 Normal 
    	linearInterpolators[0][0][1].addValue(60, 15000);
    	linearInterpolators[0][0][1].addValue(0, 30000);
    	linearInterpolators[0][0][1].addValue(-60,45000);
    	linearInterpolators[0][0][1].addValue(0,60000);
    	linearInterpolators[0][1][1].addValue(0, 0);
    	linearInterpolators[0][1][1].addValue(0, 60000);
    	linearInterpolators[0][2][1].addValue(0, 0);
    	linearInterpolators[0][2][1].addValue(0, 60000);

    	
    	linearInterpolators[0][0][2].addValue(0, 0);        //level 3 Normal
    	linearInterpolators[0][0][2].addValue(0, 60000);
    	linearInterpolators[0][1][2].addValue(0,0);
    	linearInterpolators[0][1][2].addValue(0, 60000);
    	linearInterpolators[0][2][2].addValue(0, 0);
    	linearInterpolators[0][2][2].addValue(30, 15000);
    	linearInterpolators[0][2][2].addValue(0, 30000);
    	linearInterpolators[0][2][2].addValue(-30, 45000);
    	linearInterpolators[0][2][2].addValue(0, 60000);
    	
    	linearInterpolators[0][0][3].addValue(0, 0);        //level 4 Normal
    	linearInterpolators[0][0][3].addValue(0, 60000);
    	linearInterpolators[0][1][3].addValue(0,0);
    	linearInterpolators[0][1][3].addValue(60, 15000);
    	linearInterpolators[0][1][3].addValue(0, 30000);
    	linearInterpolators[0][1][3].addValue(-60, 45000);
    	linearInterpolators[0][1][3].addValue(0, 60000);
    	linearInterpolators[0][2][3].addValue(0, 0);
    	linearInterpolators[0][2][3].addValue(0, 60000);
    	
    	linearInterpolators[0][0][4].addValue(0, 0);         //level 5 Normal 
    	linearInterpolators[0][0][4].addValue(90, 15000);
    	linearInterpolators[0][0][4].addValue(0,30000);
    	linearInterpolators[0][0][4].addValue(-90,45000);
    	linearInterpolators[0][0][4].addValue(0,60000);
    	linearInterpolators[0][1][4].addValue(0, 0);
    	linearInterpolators[0][1][4].addValue(0, 60000);
    	linearInterpolators[0][2][4].addValue(0, 0);
    	linearInterpolators[0][2][4].addValue(0, 60000);
    	
    	linearInterpolators[0][0][5].addValue(0, 0);         //level 6 Normal 
    	linearInterpolators[0][0][5].addValue(60, 15000);
    	linearInterpolators[0][0][5].addValue(0,30000);
    	linearInterpolators[0][0][5].addValue(-60,45000);
    	linearInterpolators[0][0][5].addValue(0,60000);
    	linearInterpolators[0][1][5].addValue(0,0);
    	linearInterpolators[0][1][5].addValue(30, 15000);
    	linearInterpolators[0][1][5].addValue(0, 30000);
    	linearInterpolators[0][1][5].addValue(-30, 45000);
    	linearInterpolators[0][1][5].addValue(0, 60000);
    	linearInterpolators[0][2][5].addValue(0, 0);
    	linearInterpolators[0][2][5].addValue(0, 60000);
    	
    	linearInterpolators[0][0][6].addValue(0, 0);         //level 7 Normal 
    	linearInterpolators[0][0][6].addValue(60, 15000);
    	linearInterpolators[0][0][6].addValue(0,30000);
    	linearInterpolators[0][0][6].addValue(-60,45000);
    	linearInterpolators[0][0][6].addValue(0,60000);
    	linearInterpolators[0][1][6].addValue(0,0);
    	linearInterpolators[0][1][6].addValue(60, 15000);
    	linearInterpolators[0][1][6].addValue(0, 30000);
    	linearInterpolators[0][1][6].addValue(-60, 45000);
    	linearInterpolators[0][1][6].addValue(0, 60000);
    	linearInterpolators[0][2][6].addValue(0, 0);
    	linearInterpolators[0][2][6].addValue(0, 60000);
    	
    	linearInterpolators[0][0][7].addValue(0, 0);         //level 8 Normal 
    	linearInterpolators[0][0][7].addValue(60, 15000);
    	linearInterpolators[0][0][7].addValue(0,30000);
    	linearInterpolators[0][0][7].addValue(-60,45000);
    	linearInterpolators[0][0][7].addValue(0,60000);
    	linearInterpolators[0][1][7].addValue(0,0);
    	linearInterpolators[0][1][7].addValue(60, 15000);
    	linearInterpolators[0][1][7].addValue(0, 30000);
    	linearInterpolators[0][1][7].addValue(-60, 45000);
    	linearInterpolators[0][1][7].addValue(0, 60000);
    	linearInterpolators[0][2][7].addValue(0, 0);
    	linearInterpolators[0][2][7].addValue(15, 30000);
    	linearInterpolators[0][2][7].addValue(0, 60000);
    	
    	linearInterpolators[0][0][8].addValue(0, 0);         //level 9 Normal 
    	linearInterpolators[0][0][8].addValue(60, 15000);
    	linearInterpolators[0][0][8].addValue(0,30000);
    	linearInterpolators[0][0][8].addValue(-60,45000);
    	linearInterpolators[0][0][8].addValue(0,60000);
    	linearInterpolators[0][1][8].addValue(0,0);
    	linearInterpolators[0][1][8].addValue(60, 15000);
    	linearInterpolators[0][1][8].addValue(0, 30000);
    	linearInterpolators[0][1][8].addValue(-60, 45000);
    	linearInterpolators[0][1][8].addValue(0, 60000);
    	linearInterpolators[0][2][8].addValue(0, 0);
    	linearInterpolators[0][2][8].addValue(15, 30000);
    	linearInterpolators[0][2][8].addValue(0, 60000);

    	linearInterpolators[0][0][9].addValue(0, 0);         //level 10 Normal 
    	linearInterpolators[0][0][9].addValue(-60, 15000);
    	linearInterpolators[0][0][9].addValue(0,30000);
    	linearInterpolators[0][0][9].addValue(60,45000);
    	linearInterpolators[0][0][9].addValue(0,60000);
    	linearInterpolators[0][1][9].addValue(0,0);
    	linearInterpolators[0][1][9].addValue(-60, 15000);
    	linearInterpolators[0][1][9].addValue(0, 30000);
    	linearInterpolators[0][1][9].addValue(60, 45000);
    	linearInterpolators[0][1][9].addValue(0, 60000);
    	linearInterpolators[0][2][9].addValue(0, 0);
    	linearInterpolators[0][2][9].addValue(15, 30000);
    	linearInterpolators[0][2][9].addValue(0, 60000);
    	
    	
    	
    	linearInterpolators[1][0][0].addValue(0, 0);        //level 1 Expert
    	linearInterpolators[1][0][0].addValue(0, 60000);
    	linearInterpolators[1][1][0].addValue(0,0);
    	linearInterpolators[1][1][0].addValue(180, 30000);
    	linearInterpolators[1][1][0].addValue(0, 60000);
    	linearInterpolators[1][2][0].addValue(0, 0);
    	linearInterpolators[1][2][0].addValue(0, 60000);

    	
    	linearInterpolators[1][0][1].addValue(0, 0);         //level 2 Expert 
    	linearInterpolators[1][0][1].addValue(180, 15000);
    	linearInterpolators[1][0][1].addValue(0, 30000);
    	linearInterpolators[1][0][1].addValue(-180,45000);
    	linearInterpolators[1][0][1].addValue(0,60000);
    	linearInterpolators[1][1][1].addValue(0, 0);
    	linearInterpolators[1][1][1].addValue(0, 60000);
    	linearInterpolators[1][2][1].addValue(0, 0);
    	linearInterpolators[1][2][1].addValue(0, 60000);

    	
    	linearInterpolators[1][0][2].addValue(0, 0);        //level 3 Expert
    	linearInterpolators[1][0][2].addValue(0, 60000);
    	linearInterpolators[1][1][2].addValue(0,0);
    	linearInterpolators[1][1][2].addValue(0, 60000);
    	linearInterpolators[1][2][2].addValue(0, 0);
    	linearInterpolators[1][2][2].addValue(180, 15000);
    	linearInterpolators[1][2][2].addValue(0, 30000);
    	linearInterpolators[1][2][2].addValue(-180, 45000);
    	linearInterpolators[1][2][2].addValue(0, 60000);
    	
    	linearInterpolators[1][0][3].addValue(0, 0);        //level 4 Expert
    	linearInterpolators[1][0][3].addValue(0, 60000);
    	linearInterpolators[1][1][3].addValue(0,0);
    	linearInterpolators[1][1][3].addValue(180, 15000);
    	linearInterpolators[1][1][3].addValue(0, 30000);
    	linearInterpolators[1][1][3].addValue(-180, 45000);
    	linearInterpolators[1][1][3].addValue(0, 60000);
    	linearInterpolators[1][2][3].addValue(0, 0);
    	linearInterpolators[1][2][3].addValue(0, 60000);
    	
    	linearInterpolators[1][0][4].addValue(0, 0);         //level 5 Expert 
    	linearInterpolators[1][0][4].addValue(360, 15000);
    	linearInterpolators[1][0][4].addValue(0,30000);
    	linearInterpolators[1][0][4].addValue(-360,45000);
    	linearInterpolators[1][0][4].addValue(0,60000);
    	linearInterpolators[1][1][4].addValue(0, 0);
    	linearInterpolators[1][1][4].addValue(0, 60000);
    	linearInterpolators[1][2][4].addValue(0, 0);
    	linearInterpolators[1][2][4].addValue(0, 60000);
    
    	linearInterpolators[1][0][5].addValue(0, 0);         //level 6 Expert 
    	linearInterpolators[1][0][5].addValue(180, 15000);
    	linearInterpolators[1][0][5].addValue(0,30000);
    	linearInterpolators[1][0][5].addValue(-180,45000);
    	linearInterpolators[1][0][5].addValue(0,60000);
    	linearInterpolators[1][1][5].addValue(0,0);
    	linearInterpolators[1][1][5].addValue(30, 15000);
    	linearInterpolators[1][1][5].addValue(0, 30000);
    	linearInterpolators[1][1][5].addValue(-30, 45000);
    	linearInterpolators[1][1][5].addValue(0, 60000);
    	linearInterpolators[1][2][5].addValue(0, 0);
    	linearInterpolators[1][2][5].addValue(0, 60000);
    	
    	linearInterpolators[1][0][6].addValue(0, 0);         //level 7 Expert 
    	linearInterpolators[1][0][6].addValue(180, 15000);
    	linearInterpolators[1][0][6].addValue(0,30000);
    	linearInterpolators[1][0][6].addValue(-180,45000);
    	linearInterpolators[1][0][6].addValue(0,60000);
    	linearInterpolators[1][1][6].addValue(0,0);
    	linearInterpolators[1][1][6].addValue(180, 15000);
    	linearInterpolators[1][1][6].addValue(0, 30000);
    	linearInterpolators[1][1][6].addValue(-180, 45000);
    	linearInterpolators[1][1][6].addValue(0, 60000);
    	linearInterpolators[1][2][6].addValue(0, 0);
    	linearInterpolators[1][2][6].addValue(0, 60000);
    	
    	linearInterpolators[1][0][7].addValue(0, 0);         //level 8 Expert 
    	linearInterpolators[1][0][7].addValue(180, 15000);
    	linearInterpolators[1][0][7].addValue(0,30000);
    	linearInterpolators[1][0][7].addValue(-180,45000);
    	linearInterpolators[1][0][7].addValue(0,60000);
    	linearInterpolators[1][1][7].addValue(0,0);
    	linearInterpolators[1][1][7].addValue(180, 15000);
    	linearInterpolators[1][1][7].addValue(0, 30000);
    	linearInterpolators[1][1][7].addValue(-180, 45000);
    	linearInterpolators[1][1][7].addValue(0, 60000);
    	linearInterpolators[1][2][7].addValue(0, 0);
    	linearInterpolators[1][2][7].addValue(15, 30000);
    	linearInterpolators[1][2][7].addValue(0, 60000);
    
    	linearInterpolators[1][0][8].addValue(0, 0);         //level 9 Expert 
    	linearInterpolators[1][0][8].addValue(360, 15000);
    	linearInterpolators[1][0][8].addValue(0,30000);
    	linearInterpolators[1][0][8].addValue(-360,45000);
    	linearInterpolators[1][0][8].addValue(0,60000);
    	linearInterpolators[1][1][8].addValue(0,0);
    	linearInterpolators[1][1][8].addValue(180, 15000);
    	linearInterpolators[1][1][8].addValue(0, 30000);
    	linearInterpolators[1][1][8].addValue(-180, 45000);
    	linearInterpolators[1][1][8].addValue(0, 60000);
    	linearInterpolators[1][2][8].addValue(0, 0);
    	linearInterpolators[1][2][8].addValue(15, 30000);
    	linearInterpolators[1][2][8].addValue(0, 60000);

    	linearInterpolators[1][0][9].addValue(0, 0);         //level 10 Expert 
    	linearInterpolators[1][0][9].addValue(-360, 15000);
    	linearInterpolators[1][0][9].addValue(0,30000);
    	linearInterpolators[1][0][9].addValue(360,45000);
    	linearInterpolators[1][0][9].addValue(0,60000);
    	linearInterpolators[1][1][9].addValue(0,0);
    	linearInterpolators[1][1][9].addValue(-360, 15000);
    	linearInterpolators[1][1][9].addValue(0, 30000);
    	linearInterpolators[1][1][9].addValue(360, 45000);
    	linearInterpolators[1][1][9].addValue(0, 60000);
    	linearInterpolators[1][2][9].addValue(0, 0);
    	linearInterpolators[1][2][9].addValue(15, 30000);
    	linearInterpolators[1][2][9].addValue(0, 60000);  
    }
    
	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
}


    
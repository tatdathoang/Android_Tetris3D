package com.tdh.Sup;
import java.io.Serializable;


public class Options implements Serializable
{
	private static final long serialVersionUID = 1L;
	private boolean enabledmusic;
	private boolean enableVibrate;
	private int difficultyLevel;
	
	public static final int DIFFICULTY_NORMAL = 0;
	public static final int DIFFICULTY_EXPERT = 1;
	public static final int DIFFICULTY_EASY=2;

	public static final int OPTION_DIFFICULTY = 1;
	public static final int OPTION_STARTING_LEVEL = 2;
	public static final int OPTION_MUSIC = 3;
	public static final int OPTION_SOUND = 4;
	public static int[] ALLOPTIONS = {
										OPTION_DIFFICULTY,
										OPTION_STARTING_LEVEL,
										OPTION_MUSIC,
										OPTION_SOUND,
										}; 
	
	private Game game;
	private Game newGame;
	private int startingLevel;	
	public ColorSkins[] mSkins;
	
	public Options(Game currentGame,int dif,int lv, boolean music, boolean sound, boolean vibrate)
	{
		this.setMusicEnabled(true);
		this.setSoundEnabled(true);
		this.game = currentGame;
		
		this.difficultyLevel = dif;
		this.startingLevel = lv;
		this.enabledmusic = music;
		this.enabledsound = sound;
		this.enableVibrate= vibrate;
		
		this.game = new TetricGameData();
		this.game.initGame(this.startingLevel);
		
		this.newGame = new TetricGameData();
		this.newGame.setLevel(lv);

		this.mSkins= new ColorSkins[7];
		
		mSkins[0]=new ColorSkins(0xff000,0,0);
		mSkins[1]=new ColorSkins(0,0xffff0,0);
		mSkins[2]=new ColorSkins(0,0,0xff000);
		mSkins[3]=new ColorSkins(0xfff00,0xfff00,0);
		mSkins[4]=new ColorSkins(0xff000,0,0xff000);
		mSkins[5]=new ColorSkins(0,0xff000,0xfff00);
		mSkins[6]=new ColorSkins(0xf0000,0xf00000,0xffff0);
		
	}
	
	public Options() 
	{
		this.setMusicEnabled(true);
		this.setSoundEnabled(true);
		this.setEnableVibrate(true);
		this.startingLevel=1;
		this.difficultyLevel=0;	
		
		
		this.mSkins= new ColorSkins[7];
		
		mSkins[0]=new ColorSkins(0xff00,0,0);
		mSkins[1]=new ColorSkins(0,0xff00,0);
		mSkins[2]=new ColorSkins(0,0,0xff00);
		mSkins[3]=new ColorSkins(0xffff0,0xffff,0);
		mSkins[4]=new ColorSkins(0xffff,0,0xffff0);
		mSkins[5]=new ColorSkins(0,0xff0f,0xfff00);     
		mSkins[6]=new ColorSkins(0xffff0,0xffff,0xffff);


	}
	
	public void setDifficultyLevel(int difficultyLevel)
	{
		this.difficultyLevel = difficultyLevel;
	}
	public int getDifficultyLevel()
	{
		return this.difficultyLevel;
	}
	public void setMusicEnabled(boolean flag)
	{
		this.enabledmusic = flag;
	}
	public boolean isMusicEnabled()
	{
		return this.enabledmusic;
	}
	private boolean enabledsound;
	public void setSoundEnabled(boolean flag)
	{
		this.enabledsound = flag;
	}
	public boolean isSoundEnabled()
	{
		return this.enabledsound;
	}
	public void setGame(Game game)
	{
		this.game= game;
	}
	public void setNewGame(Game game)
	{
		this.newGame = game;
	}
	public Game getGame()
	{
		return this.game;
	}
	
	public void setStartingLevel(int startingLevel)
	{
		this.startingLevel = startingLevel;
	}
	public int getStartingLevel()
	{
		return this.startingLevel;
	}

	@SuppressWarnings("unused")
	private void initNewGame()
	{
		newGame = new TetricGameData();
		newGame.setLevel(game.getLevel());

	}

	public void setSkins(ColorSkins[] skins) 
	{
		mSkins = skins;
	}

	public ColorSkins[] getSkins() 
	{
		return mSkins;
	}
	
	public void setSkinscolor(int pos, int red, int green, int blue)
	{
		mSkins[pos].setRed(red);
		mSkins[pos].setGreen(green);
		mSkins[pos].setBlue(blue);

	}
	public ColorSkins getSkincolor(int pos)
	{
		return this.mSkins[pos];
	}

	public void setEnableVibrate(boolean enableVibrate) {
		this.enableVibrate = enableVibrate;
	}

	public boolean isEnableVibrate() {
		return enableVibrate;
	}
	
	


	
}

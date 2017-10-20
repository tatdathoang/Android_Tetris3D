package com.tdh.Sup;

public interface Game
{
	public static final int STATUS_STARTUP=0;
	public static final int STATUS_PLAYING=1;
	public static final int STATUS_PAUSE=2;
	public static final int STATUS_GAME_OVER=3;
	
	/*  BLOCK ACTIVITY   */
	public void gameLoop();
	public void moveBlockLeft();
	public void moveBlockRight();
	public boolean moveBlockDown();
	public boolean canMoveBlockDown();
	public void rotateCurrentBlockClockwise();
	public Block getCurrentBlock();
	public Block getNextBlock();
	public boolean isBlockPlaced();
	
	/*   MATRIX CALC   */
	public int getGridValue(int x, int y);
	public int[] getClearedLines();
	public int getClearedLineCount();
	public void flagCompletedLines();
	
	/*   GAME METHOD   */
	public void initGame(int theStartingLevel);
	public void setStatus(int status);
	public int getStatus();
	
	
	/*  INTRO GROUP  */
	public int getScore();
	public void setScore(int score);
	public void setLevel(int level);
	public void setLines(int lines);
	public int getLevel();
	public String getLevelName();
	public int getLines();

	
	/*   TIME GROUP   */
	public int getTimer();
	
	
	public void saveGame(android.content.SharedPreferences preferences,android.content.SharedPreferences.Editor editor);
	public void loadGame(android.content.SharedPreferences preferences);


}

package com.tdh.Sup;


public class TetricGameData implements Game
{
	private int[][] grid;
	private int[] clearedLines;
	private int status;
	public int score;
	public int lines;
	public int level;
	public int timer;
	public Block currentBlock;
	public Block nextBlock;
	
	int newLevel;
	int startingLevel;
	public int gridMaxHeight;
	public int gridMaxWidth;
	
	private boolean blockPlaced;
	
	public TetricGameData()
	{
		this.score =0;
		this.lines =0;
		this.level =1;
		this.timer = 1000;
		this.newLevel = 1;
		this.startingLevel = 1;
		this.gridMaxWidth = 10;
		this.gridMaxHeight = 20;
		this.grid = new int[gridMaxWidth][gridMaxHeight];
		new java.util.Random();
		this.blockPlaced = false;
		
		for(int y=0;y<gridMaxHeight;y++)
		{
			for(int x=0;x<gridMaxWidth;x++)
			{
				this.grid[x][y]=-1;
			}
		}
		
		this.clearedLines = new int[gridMaxHeight];
		for(int y=0;y<gridMaxHeight;y++)
		{
			this.clearedLines[y]=0;
		}
	}
	
	public void clearGrid()
	{
		for(int x=0;x<gridMaxWidth;x++)
		{
			for(int y=0;y<gridMaxHeight;y++)
			{
				this.grid[x][y]=-1;
			}
		}
		for(int y=0;y<gridMaxHeight;y++)
		{
			this.clearedLines[y]=0;
		}
	}
	
	public void initGame(int theStartingLevel)
	{
		this.score = 0;
		this.lines = 0;
		this.level = startingLevel;
		this.newLevel = startingLevel;
		this.startingLevel = theStartingLevel;
		Block newblock = new Block();
		this.currentBlock  = newblock;
		Block newnextblock = new Block();
		this.nextBlock = newnextblock;
		
		switch(theStartingLevel)
		{
		case 1:
			this.timer = 500;
			break;
		case 2:
			this.timer = 450;
			break;
		case 3:
			this.timer = 400;
			break;
		case 4:
			this.timer = 350;
			break;
		case 5:
			this.timer = 300;
			break;
		case 6:
			this.timer = 250;
			break;
		case 7:
			this.timer = 200;
			break;
		case 8:
			this.timer = 166;
			break;
		case 9:
			this.timer = 133;
			break;
		case 10:
			this.timer = 100;
			break;
		}			
	}
	
	public void setStatus(int status)
	{
		this.status = status;
	}
	
	public int getStatus()
	{
		return this.status;
	}
	
	public int getGridValue(int x, int y)
	{
		return grid[x][y];
	}
	
	public boolean isGridEmpty()
	{		
		for(int y=0;y<gridMaxHeight;y++)
		{
			for(int x=0;x<gridMaxWidth;x++)
			{
				if(getGridValue(x, y)!=-1)
				{
					return false;
				}
			}
		}
		return true;		
	}

	public void gameLoop()
	{
		blockPlaced=false;
			if(this.moveBlockDown())
			{
				this.clearCompleteLines();
				if(this.newLevel!=this.level && this.newLevel>this.level)
				{
					level=newLevel;
				}
			}
			else
			{
				if (this.grid[this.nextBlock.subblocks[0].xpos+this.nextBlock.xPos][this.currentBlock.subblocks[0].ypos+this.nextBlock.yPos]!=-1)
				{
					this.setStatus(STATUS_GAME_OVER);
					return;
				}
				this.currentBlock=this.nextBlock;
				Block bl = new Block();
				blockPlaced=true;
				this.nextBlock= bl;
			}
		
	}
	
	public void moveBlockLeft()
	{
		if(
				(this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos>0) &&
				(this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos>0) &&
				(this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos>0) &&
				(this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos>0)
			)
			{
				if(
					(this.grid[this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos-1][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos]==-1) &&
					(this.grid[this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos-1][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos]==-1) &&
					(this.grid[this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos-1][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos]==-1) &&
					(this.grid[this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos-1][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos]==-1) 
				)
				{
					this.currentBlock.xPos--;
				}
			}			
	}

	public void moveBlockRight()
	{
		if(
				(this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos<gridMaxWidth-1) &&
				(this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos<gridMaxWidth-1) &&
				(this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos<gridMaxWidth-1) &&
				(this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos<gridMaxWidth-1)
			)
			{
				if(
					(this.grid[this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos+1][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos]==-1) &&
					(this.grid[this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos+1][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos]==-1) &&
					(this.grid[this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos+1][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos]==-1) &&
					(this.grid[this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos+1][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos]==-1) 
				)
				{
					this.currentBlock.xPos++;
				}
			}			
	}
	
	public boolean canMoveBlockDown()
	{
		for(int i=0;i<4;i++)
		{
			if (this.currentBlock.subblocks[i].ypos+this.currentBlock.yPos>gridMaxHeight+2)
			{
				return false;

			}
		}
		if(this.currentBlock.height+this.currentBlock.yPos<gridMaxHeight)
		{
			if(
				(grid[currentBlock.subblocks[0].xpos+currentBlock.xPos][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos+1]==-1) &&
				(grid[currentBlock.subblocks[1].xpos+currentBlock.xPos][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos+1]==-1) &&
				(grid[currentBlock.subblocks[2].xpos+currentBlock.xPos][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos+1]==-1) &&
				(grid[currentBlock.subblocks[3].xpos+currentBlock.xPos][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos+1]==-1) 
			)
			{
				return true;
			}
		}
		return false;
		
	}
	
	public boolean moveBlockDown()
	{
		for(int i=0;i<4;i++)
		{
			if (this.currentBlock.subblocks[i].ypos+this.currentBlock.yPos>gridMaxHeight+2)
			{
				this.grid[this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos]=this.currentBlock.color;
				this.grid[this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos]=this.currentBlock.color;
				this.grid[this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos]=this.currentBlock.color;
				this.grid[this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos]=this.currentBlock.color;
				return false;
			}
		}
		if(this.currentBlock.height+this.currentBlock.yPos<gridMaxHeight)
		{
			if
			(
				(grid[currentBlock.subblocks[0].xpos+currentBlock.xPos][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos+1]==-1) &&
				(grid[currentBlock.subblocks[1].xpos+currentBlock.xPos][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos+1]==-1) &&
				(grid[currentBlock.subblocks[2].xpos+currentBlock.xPos][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos+1]==-1) &&
				(grid[currentBlock.subblocks[3].xpos+currentBlock.xPos][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos+1]==-1) 
			)
			{
				this.currentBlock.yPos++;		
				return true;
			}
		}
		
		score++;
		this.grid[this.currentBlock.subblocks[0].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[0].ypos+this.currentBlock.yPos]=this.currentBlock.color;
		this.grid[this.currentBlock.subblocks[1].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[1].ypos+this.currentBlock.yPos]=this.currentBlock.color;
		this.grid[this.currentBlock.subblocks[2].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[2].ypos+this.currentBlock.yPos]=this.currentBlock.color;
		this.grid[this.currentBlock.subblocks[3].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[3].ypos+this.currentBlock.yPos]=this.currentBlock.color;
		return false;
	}

	public void setTimer(int time)
	{
		this.timer = time;
	}

	public void clearCompleteLines()
	{
		int currentline =gridMaxHeight-1;
		int linescleared = 0;
		this.newLevel=this.level;
		while(currentline>0)
		{
			boolean linecomplete=true;
			for (int x=0;x<gridMaxWidth;x++)
			{
				if(grid[x][currentline]==-1)
				{
					linecomplete = false;
				}
			}
			if (linecomplete)
			{
				for (int y=currentline;y>0;y--)
				{
					for (int x=0;x<gridMaxWidth;x++)
					{
						if (y>0)
						{
							grid[x][y]=grid[x][y-1];
						}
						else
						{
							grid[x][y]=-1;
						}
					}
				}
				linescleared++;
			}
			else
			{
				currentline--;
			}
		}
		
		switch(linescleared)
		{
			case 0:
				break;
			case 1:
				score = score + 25;
				break;
			case 2:
				score = score + 75;
				break;	
			case 3:
				score = score + 100;
				break;
			case 4:
				score = score + 200;
				break;
			default:
				break;
		}
		
		lines += linescleared;
		if (lines<=20)
		{
			this.newLevel = 1;
			timer = 400;
			return;
		}
		if (lines>20 && lines<=40)
		{
			this.newLevel = 2;
			timer = 450;
			return;
		}
		if (lines>40 && lines<=60)
		{
			this.newLevel = 3;
			timer = 400;
			return;
		}
		if (lines>60 && lines<=80)
		{
			this.newLevel = 4;
			timer = 350;
			return;
		}
		if (lines>80 && lines<=100)
		{
			this.newLevel = 5;
			timer = 300;
			return;
		}
		if (lines>100 && lines<=120)
		{
			this.newLevel = 6;
			timer = 250;
			return;
		}
		if (lines>120 && lines<=140)
		{
			this.newLevel = 7;
			timer = 200;
			return;
		}
		if (lines>140 && lines<=180)
		{
			this.newLevel = 8;
			timer = 150;
			return;
		}
		if (lines>180 && lines<=200)
		{
			this.newLevel = 9;
			timer = 100;
			return;
		}
		if (lines>200 && lines<=250)
		{
			this.newLevel = 10;
			timer = 50;
			return;
		}
		
	}
	
	public void rotateCurrentBlockClockwise()
	{
		int currentorientation = this.currentBlock.orientation;
		this.currentBlock.rotateClockwise();
		this.currentBlock.recalcBlockOrientation();
		for(int i=0;i<4;i++)
		{
			if (this.currentBlock.xPos+this.currentBlock.subblocks[i].xpos>=this.gridMaxWidth)
			{
				this.currentBlock.orientation = currentorientation;
				this.currentBlock.recalcBlockOrientation();
				return;
			}
			if (this.currentBlock.yPos+this.currentBlock.subblocks[i].ypos>= this.gridMaxHeight)
			{
				this.currentBlock.orientation = currentorientation;
				this.currentBlock.recalcBlockOrientation();
				return;

			}
			if (grid[this.currentBlock.subblocks[i].xpos+this.currentBlock.xPos][this.currentBlock.subblocks[i].ypos+this.currentBlock.yPos]!=-1)
			{
				this.currentBlock.orientation = currentorientation;
				this.currentBlock.recalcBlockOrientation();
				return;

			}
		}			
	}
	
	public void setScore(int score)
	{
		this.score = score;
	}
	
	public int getScore()
	{
		return this.score;
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public int getLevel()
	{
		return this.level;
	}

	public String getLevelName()
	{
		String str = ""+this.level;
		while(str.length()<2)
		{
			str="0"+str;
		}
		str = ""+str;
		return str;
	}
	
	public void setLines(int lines)
	{
		this.lines = lines;
	}

	public int getLines()
	{
		return this.lines;
	}
	
	public Block getCurrentBlock()
	{
		return this.currentBlock;
	}

	public Block getNextBlock()
	{
		return this.nextBlock;
	}
	
	public int getTimer()
	{
		return this.timer;
	}

	public int[] getClearedLines()
	{
		return this.clearedLines;
	}

	public boolean isBlockPlaced()
	{
		return blockPlaced;
	}

	public void loadGame(android.content.SharedPreferences preferences)
     {
		try
		{
			this.score = preferences.getInt("TetricGameData.score", 0);
			this.lines = preferences.getInt("TetricGameData.lines", 0);
			this.timer = preferences.getInt("TetricGameData.timer", 1000);
			this.startingLevel = preferences.getInt("TetricGameData.startingLevel", 1);
			
			this.level= this.startingLevel;
			this.newLevel= this.startingLevel;
			
			this.grid = new int[gridMaxWidth][gridMaxHeight];
			new java.util.Random();
			this.blockPlaced = false;
			Block newblock = new Block();
			this.currentBlock  = newblock;
			for(int y=0;y<gridMaxHeight;y++)
			{
				String line = preferences.getString("TetricGameData.line"+y, "");
				for(int x=0;x<gridMaxWidth;x++)
				{
					int currentSubblock=-1;
					if(line.length()<gridMaxWidth)
					{
						currentSubblock=-1;
					}
					else
					{
						if(line.substring(x,x+1).equals(" "))
						{
							currentSubblock=-1;
						}
						else 
						{
							int subblock = Integer.parseInt(line.substring(x, x+1));
							if(subblock<0 || subblock>6)
							{
								currentSubblock=-1;
							}
							else
							{
								currentSubblock=subblock;
							}
						}
					}
					this.grid[x][y]=currentSubblock;
				}
			}
			this.clearedLines = new int[gridMaxHeight];
			for(int y=0;y<gridMaxHeight;y++)
			{
				this.clearedLines[y]=0;
			}
		}
		catch (Exception e) 
		{
			e.notify();
		}
	}
    
	public void saveGame(android.content.SharedPreferences preferences,android.content.SharedPreferences.Editor editor )
     {		
		editor.putInt("TetricGameData.score", this.score);
		editor.putInt("TetricGameData.lines", this.lines);
		editor.putInt("TetricGameData.timer", this.timer);
		editor.putInt("TetricGameData.startingLevel", this.startingLevel);
		
		for(int y=0;y<gridMaxHeight;y++)
		{
			String line = "";
			String currentChar = "";
			for(int x=0;x<gridMaxWidth;x++)
			{
				if(this.grid[x][y]==-1){
					currentChar=" ";
				}
				else{
					currentChar=""+this.grid[x][y];
				}
				line = line+currentChar;
			}
			editor.putString("TetricGameData.line"+y, line);			
		}
        editor.commit();
    }
	
	public int getClearedLineCount() 
	{
		int cleared=0;		
        for(int line=0;line<gridMaxHeight;line++)
        {
                if(this.clearedLines[line]!=0)
                {
                        cleared++;
                }
        }
        return cleared;  
	}
	
	public void flagCompletedLines() 
	{
		for(int y=0;y<gridMaxHeight;y++)
        {

                boolean linecleared=true;
                for(int x=0;x<gridMaxWidth;x++)
                {
                        if(this.grid[x][y]==-1)
                        {
                                linecleared = false;
                                break;
                        }
                }
                if(linecleared)
                {
                        this.clearedLines[y]=1;
                }
                else
                {
                        this.clearedLines[y]=0;
                }
        }  
		
	}
}

package com.tdh.Sup;

public class Block
{
	public static final int BLOCKTYPE_STICK=0;
	public static final int BLOCKTYPE_SQUARE=1;
	public static final int BLOCKTYPE_LETTERT=2;
	public static final int BLOCKTYPE_LETTERS=3;
	public static final int BLOCKTYPE_NUMBER2=4;
	public static final int BLOCKTYPE_LETTERL=5;
	public static final int BLOCKTYPE_LETTERLINV=6;	
	
	public SubBlock[] subblocks;
	public int blocktype;
	public int orientation;
	public int xPos;
	public int yPos;
	public int height;
	public int color;
	public static java.util.Random rd = new java.util.Random();

	
	public Block()
	{
		this.subblocks = new SubBlock[4];
		this.subblocks[0] =new SubBlock();
		this.subblocks[1] =new SubBlock();
		this.subblocks[2] =new SubBlock();
		this.subblocks[3] =new SubBlock();
		this.xPos = 3;
		this.yPos = 0;
		int blocktype = rd.nextInt(7);		
		this.orientation = 0;
		
		switch(blocktype)
		{
		case 0:
			this.blocktype = BLOCKTYPE_STICK;
			this.color = 0;
			break;
		case 1:
			this.blocktype = BLOCKTYPE_SQUARE;
			this.color = 1;
			break;
		case 2:
			this.blocktype = BLOCKTYPE_LETTERT;
			this.color = 2;
			break;
		case 3:
			this.blocktype = BLOCKTYPE_LETTERS;
			this.color = 3;

			break;
		case 4:
			this.blocktype = BLOCKTYPE_NUMBER2;
			this.color = 4;
			break;
		case 5:
			this.blocktype = BLOCKTYPE_LETTERL;
			this.color = 5;
			break;
		case 6:
			this.blocktype = BLOCKTYPE_LETTERLINV;
			this.color = 6;
			break;
		}
		this.recalcBlockOrientation();
	}
	
	public Block(int theType,int xPos, int yPos)
	{
		this.subblocks = new SubBlock[4];
		this.subblocks[0] =new SubBlock();
		this.subblocks[1] =new SubBlock();
		this.subblocks[2] =new SubBlock();
		this.subblocks[3] =new SubBlock();		
		this.blocktype= theType;
		this.orientation= 0;
		switch(this.blocktype)
		{
			case BLOCKTYPE_STICK:
				this.color=0;				
			break;
			
			case BLOCKTYPE_SQUARE:
				this.color = 1;
			break;
			
			case BLOCKTYPE_LETTERT:
				this.color = 2;
			break;
			
			case BLOCKTYPE_LETTERS:
				this.color = 3;
			break;
			
			case BLOCKTYPE_NUMBER2:
				this.color = 4;				
			break;
			
			case BLOCKTYPE_LETTERL:
				this.color = 5;			
			break;
			
			case BLOCKTYPE_LETTERLINV:
				this.color = 6;				
			break;
		}
		this.recalcBlockOrientation();			
	}
	
	public void rotateClockwise()
	{
		if (orientation <3)
		{
			orientation++;
		}
		else
		{
			this.orientation = 0;
		}
		recalcBlockOrientation();		
	}
	
	public void recalcBlockOrientation()
	{
		switch(this.blocktype)
		{
		case BLOCKTYPE_STICK:
			if (this.orientation==0 || this.orientation==2)
			{			
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =1; 
				this.subblocks[1].ypos =0; 
				this.subblocks[2].xpos =2; 
				this.subblocks[2].ypos =0; 
				this.subblocks[3].xpos =3; 
				this.subblocks[3].ypos =0;
				this.height = 1;
			}
			else
			{
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =0; 
				this.subblocks[1].ypos =1; 
				this.subblocks[2].xpos =0; 
				this.subblocks[2].ypos =2; 
				this.subblocks[3].xpos =0; 
				this.subblocks[3].ypos =3;
				this.height = 4;
			}			
		break;
		
		case BLOCKTYPE_SQUARE:
			this.subblocks[0].xpos =0;
			this.subblocks[0].ypos =0;
			this.subblocks[1].xpos =1; 
			this.subblocks[1].ypos =0; 
			this.subblocks[2].xpos =0; 
			this.subblocks[2].ypos =1; 
			this.subblocks[3].xpos =1; 
			this.subblocks[3].ypos =1;
			this.height = 2;
		break;
		
		case BLOCKTYPE_LETTERT:
			switch (this.orientation)
			{
				case 0:
					this.subblocks[0].xpos =0;
					this.subblocks[0].ypos =0;
					this.subblocks[1].xpos =1; 
					this.subblocks[1].ypos =0; 
					this.subblocks[2].xpos =2; 
					this.subblocks[2].ypos =0; 
					this.subblocks[3].xpos =1; 
					this.subblocks[3].ypos =1;
					this.height = 2;
				break;
				case 1:
					this.subblocks[0].xpos =1;
					this.subblocks[0].ypos =0;
					this.subblocks[1].xpos =0; 
					this.subblocks[1].ypos =1; 
					this.subblocks[2].xpos =1; 
					this.subblocks[2].ypos =1; 
					this.subblocks[3].xpos =1; 
					this.subblocks[3].ypos =2;
					this.height = 3;
				break;
				case 2:
					this.subblocks[0].xpos =1;
					this.subblocks[0].ypos =0;
					this.subblocks[1].xpos =0; 
					this.subblocks[1].ypos =1; 
					this.subblocks[2].xpos =1; 
					this.subblocks[2].ypos =1; 
					this.subblocks[3].xpos =2; 
					this.subblocks[3].ypos =1;
					this.height = 2;
				break;
				case 3:
					this.subblocks[0].xpos =0;
					this.subblocks[0].ypos =0;
					this.subblocks[1].xpos =0; 
					this.subblocks[1].ypos =1; 
					this.subblocks[2].xpos =1; 
					this.subblocks[2].ypos =1; 
					this.subblocks[3].xpos =0; 
					this.subblocks[3].ypos =2;
					this.height = 3;
				break;
			}
			
		break;
		case BLOCKTYPE_LETTERS:
			if (this.orientation==0 || this.orientation==2)
			{			
				this.subblocks[0].xpos =1;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =2; 
				this.subblocks[1].ypos =0; 
				this.subblocks[2].xpos =0; 
				this.subblocks[2].ypos =1; 
				this.subblocks[3].xpos =1; 
				this.subblocks[3].ypos =1;
				this.height = 2;
			}
			else
			{
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =0; 
				this.subblocks[1].ypos =1; 
				this.subblocks[2].xpos =1; 
				this.subblocks[2].ypos =1; 
				this.subblocks[3].xpos =1; 
				this.subblocks[3].ypos =2;
				this.height = 3;
			}
			
		break;
		case BLOCKTYPE_NUMBER2:
			if (this.orientation==0 || this.orientation ==2)
			{			
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =1; 
				this.subblocks[1].ypos =0; 
				this.subblocks[2].xpos =1; 
				this.subblocks[2].ypos =1; 
				this.subblocks[3].xpos =2; 
				this.subblocks[3].ypos =1;
				this.height = 2;
			}
			else
			{
				this.subblocks[0].xpos =1;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =0; 
				this.subblocks[1].ypos =1; 
				this.subblocks[2].xpos =1; 
				this.subblocks[2].ypos =1; 
				this.subblocks[3].xpos =0; 
				this.subblocks[3].ypos =2;
				this.height = 3;
			}

		break;
		case BLOCKTYPE_LETTERL:
			switch(this.orientation)
			{
			case 0:
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =0; 
				this.subblocks[1].ypos =1; 
				this.subblocks[2].xpos =1; 
				this.subblocks[2].ypos =1; 
				this.subblocks[3].xpos =2; 
				this.subblocks[3].ypos =1;
				this.height = 2;
				break;
			case 1:
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =1; 
				this.subblocks[1].ypos =0; 
				this.subblocks[2].xpos =0; 
				this.subblocks[2].ypos =1; 
				this.subblocks[3].xpos =0; 
				this.subblocks[3].ypos =2;
				this.height = 3;
				break;
			case 2:
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =1; 
				this.subblocks[1].ypos =0; 
				this.subblocks[2].xpos =2; 
				this.subblocks[2].ypos =0; 
				this.subblocks[3].xpos =2; 
				this.subblocks[3].ypos =1;
				this.height = 2;
				break;
			case 3:
				this.subblocks[0].xpos =1;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =1; 
				this.subblocks[1].ypos =1; 
				this.subblocks[2].xpos =0; 
				this.subblocks[2].ypos =2; 
				this.subblocks[3].xpos =1; 
				this.subblocks[3].ypos =2;
				this.height = 3;
				break;
			}
		break;
		
		case BLOCKTYPE_LETTERLINV:
			switch(this.orientation)
			{
			case 0:
				this.subblocks[0].xpos =2;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =0; 
				this.subblocks[1].ypos =1; 
				this.subblocks[2].xpos =1; 
				this.subblocks[2].ypos =1; 
				this.subblocks[3].xpos =2; 
				this.subblocks[3].ypos =1;
				this.height = 2;
				break;
			case 1:
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =0; 
				this.subblocks[1].ypos =1; 
				this.subblocks[2].xpos =0; 
				this.subblocks[2].ypos =2; 
				this.subblocks[3].xpos =1; 
				this.subblocks[3].ypos =2;
				this.height = 3;
				break;
			case 2:
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =1; 
				this.subblocks[1].ypos =0; 
				this.subblocks[2].xpos =2; 
				this.subblocks[2].ypos =0; 
				this.subblocks[3].xpos =0; 
				this.subblocks[3].ypos =1;
				this.height = 2;
				break;
			case 3:
				this.subblocks[0].xpos =0;
				this.subblocks[0].ypos =0;
				this.subblocks[1].xpos =1; 
				this.subblocks[1].ypos =0; 
				this.subblocks[2].xpos =1; 
				this.subblocks[2].ypos =1; 
				this.subblocks[3].xpos =1; 
				this.subblocks[3].ypos =2;
				this.height = 3;
				break;
			}
		break;
		}
	}			

}




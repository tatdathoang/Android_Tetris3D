package com.tdh.Sup;

public class ExplodingCube 
{

	public static final int EXPLOSION_TYPE_NORMAL=0;
	public static final int EXPLOSION_TYPE_SINGLE=1;
	public static final int EXPLOSION_TYPE_SPIRAL=2;
	public static final int EXPLOSION_TYPE_SPHERE=3;
	
	public float x;
	public float y;
	public float z;
	public float ux;
	public float uy;
	public float uz;
	public float anglex; //rotation around x axis;
	public float angley; //rotation around y axis;
	public float anglez; //rotation around z axis;
	public float uax; 
	public float uay; 
	public float uaz; 
	public int blocktype;
	public int frame;
	public int explosionType;
	public long startTime;
	
	public ExplodingCube(float x,float y,float z,float ux,float uy, float uz, int blocktype,int frame)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.ux = ux;
		this.uy = uy;
		this.uz = uz;
		this.blocktype = blocktype;
		this.frame = frame;
		this.anglex = 0.0f;
		this.angley = 0.0f;
		this.anglez = 0.0f;
		this.uax = 0.0f;
		this.uay = 0.0f;
		this.uaz = 0.0f;
		this.explosionType = EXPLOSION_TYPE_NORMAL;
		this.startTime = System.currentTimeMillis();
	}
}


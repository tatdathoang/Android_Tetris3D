package com.tdh.Sup;

import java.io.Serializable;

public class ColorSkins implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private int red;
	private int green;
	private int blue;
	
	public ColorSkins()
	{
		this.red=0xffff;
		this.green=0xffff;
		this.blue=0xffff;
	}
	
	
	public ColorSkins(int red, int green, int blue)
	{
		this.setRed(red);
		this.setGreen(green);
		this.setBlue(blue);
	}
	
	
	public void setRed(int red) {
		this.red = red;
	}

	public int getRed() {
		return red;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getGreen() {
		return green;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public int getBlue() {
		return blue;
	}
}

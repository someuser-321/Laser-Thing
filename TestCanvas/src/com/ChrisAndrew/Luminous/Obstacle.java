package com.ChrisAndrew.Luminous;

import android.graphics.Bitmap;


public class Obstacle {
	
	public String type;
	public String colour;
	
	public float x;
	public float y;
	
	public float width;
	public float height;
	
	public float rotation;
	
	public Bitmap bmp;
	
	
	public Obstacle(String type_, float x_, float y_, float width_, float height_, float rotation_, Bitmap bmp_){
		
		type = type_;
		
		x = x_;
		y = y_;
		
		width = width_;
		height = height_;
		
		rotation = rotation_;
		bmp = bmp_;
		
	}
	
	public Obstacle(float x_, float y_, String colour_, Bitmap bmp_){
		
		type = "source";
		
		x = x_;
		y = y_;
		
		colour = colour_;
		
		
	}

}

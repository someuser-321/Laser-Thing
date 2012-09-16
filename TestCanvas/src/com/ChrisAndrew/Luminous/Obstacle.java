package com.ChrisAndrew.Luminous;

import android.graphics.Bitmap;


public class Obstacle {
	
	public String type;
	public String colour;
	
	public float x, x_;
	public float y, y_;
	
	public float width;
	public float height;
	
	public float rotation;
	public float[] normal = {-1.0f, 0.0f};
	
	public Bitmap bmp;
	
	
	public Obstacle(String type_, float _x, float _y, float width_, float height_, float rotation_, Bitmap bmp_){
		
		type = type_;
		
		x = _x;
		y = _y;
		
		width = width_;
		height = height_;
		
		rotation = rotation_;
		
		x_ = x + (float)Math.cos(rotation)*width;
		y_ = y + (float)Math.sin(rotation)*height;
		
		Vector n;
		
		bmp = bmp_;
		
	}
	
	public Obstacle(float x_, float y_, String colour_, Bitmap bmp_){
		
		type = "source";
		
		x = x_;
		y = y_;
		
		colour = colour_;
		
		
	}

}

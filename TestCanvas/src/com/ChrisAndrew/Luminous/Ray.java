package com.ChrisAndrew.Luminous;


public class Ray {

	public float x;
	public float y;
	
	public float x_;
	public float y_;
	
	public float colour;
	public Ray next;
	
	
	public Ray(float x_, float y_, float x__, float y__){
		
		x = x_;
		y = y_;
		x_ = x__;
		y_ = y__;
		
	}
	
	public boolean intercept(float x, float y){
		
		return false;
		
	}
	
}

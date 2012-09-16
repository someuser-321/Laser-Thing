package com.ChrisAndrew.Luminous;


public class Ray {

	public Vector v;
	public float colour;
	
	
	public Ray(float x_, float y_, float x__, float y__){
		
		v.x = x_;
		v.y = y_;
		v.x_ = x__;
		v.y_ = y__;
		
	}
	
	public boolean intercept(float x1, float y1, float x2, float y2){
	
		boolean f1 = false;
		boolean f2 = false;
		
		if ((v.x - x1)/v.x_ < (v.x - x2)/v.x_)
			f1 = true;
		if ((v.y - y1)/v.y_ < (v.y - y2)/v.y_)
			f2 = true;
		
		if ( f1 && f2 )
			return true;
		else
			return false;
		
	}
	
}

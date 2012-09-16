package com.ChrisAndrew.Luminous;


public class Vector {
	
	public float x, y, x_, y_;
	
	
	public Vector(float _x, float _y, float _x_, float _y_){
		x = _x;
		y = _y;
		x_ = _x_;
		y_ = _y_;
	
	}
	
	static public float dot(Vector a, Vector b){
		return a.x_*b.x_ + a.y_*b.y_;
	}
	
	static public Vector mul(Vector a, float mag){
		return new Vector(a.x, a.y, a.x_*mag, a.y_*mag);
	}
	
	static public Vector add(Vector a, Vector b){
		return new Vector(a.x, a.y, a.x_+b.x_, a.y_+b.y_);
	}
	
	static public Vector sub(Vector a, Vector b){
		return new Vector(a.x, a.y, a.x_-b.x_, a.y_-b.y_);
	}

}

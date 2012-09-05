package com.ChrisAndrew.Luminous;


import android.graphics.Bitmap;


public class Image {

	public float x, y;
	public float width, height;
	
	public Bitmap bmp;
	
	public Image(float x_, float y_, float width_, float height_, Bitmap bmp_){
		
		x = x_;
		y = y_;
		width = width_;
		height = height_;
		bmp = Bitmap.createScaledBitmap(bmp, (int)width, (int)height, false);
		
	}
	
	
}

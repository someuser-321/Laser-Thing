package com.ChrisAndrew.Luminous;


import android.content.res.AssetManager;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;


public class Text {
	
	public float x, y;
	public String text;
	
	public float size;
	public Paint paint;
	public int r, g, b;
	
	
	public Text(){
		
	}
	
	public Text(float x_, float y_, String text_, int size_, String align_, int r_, int g_, int b_, AssetManager assets_){
		
		x = x_;
		y = y_;
		
		text = text_;
		size = size_;
		
		
		paint = new Paint();
		paint.setTextSize(size);
		paint.setARGB(255, r_, g_, b_);
		
		if ( align_.equals("right") ){
			paint.setTextAlign(Align.RIGHT);
		} else if ( align_.equals("center") ){
			paint.setTextAlign(Align.CENTER);
		} else {
			paint.setTextAlign(Align.LEFT);
		}
		
		paint.setTypeface(Typeface.createFromAsset(assets_, "fonts/laserfont.ttf"));
		paint.setAntiAlias(true);
		
	}

	public void setPaint(int r, int g, int b, float size, String align_, AssetManager assets){
		
		paint = new Paint();
		
		paint.setTextSize(size);
		paint.setARGB(255, r, g, b);
		
		if ( align_.equals("right") ){
			paint.setTextAlign(Align.RIGHT);
		} else if ( align_.equals("center") ){
			paint.setTextAlign(Align.CENTER);
		} else {
			paint.setTextAlign(Align.LEFT);
		}
		
		paint.setTypeface(Typeface.createFromAsset(assets, "fonts/laserfont.ttf"));
		paint.setAntiAlias(true);
		
	}
	
}

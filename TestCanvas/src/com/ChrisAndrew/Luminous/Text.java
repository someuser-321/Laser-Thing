package com.ChrisAndrew.Luminous;

import android.graphics.Paint;
import android.graphics.Paint.Align;

public class Text {
	
	public int x, y;
	public String text;
	
	public int size;
	public Paint.Align align;
	public int r, g, b;
	
	
	public Text(){
		
	}
	
	public Text(int x_, int y_, String text_, int size_, String align_, int r_, int g_, int b_){
		
		x = x_;
		y = y_;
		text = text_;
		size = size_;
		

		if ( align_.equals("left") ){
			align = Align.LEFT;
		} else if ( align_.equals("right") ){
			align = Align.RIGHT;
		} else if ( align_.equals("center") ){
			align = Align.CENTER;
		} else {
			align = Align.LEFT;
		}
		
		r = r_;
		g = g_;
		b = b_;
		
	}

}

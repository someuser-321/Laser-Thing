package com.ChrisAndrew.Luminous;

import android.graphics.Bitmap;

public class Button {

	public int x_min, x_max;
	public int y_min, y_max;
	String action;
	String text;
	float textsize;
	Bitmap normal;
	
	
	public Button(){
		
	}
	
	public Button(int x_min_, int y_min_, int x_max_, int y_max_, String action_, String text_){
		
		x_min = x_min_;
		y_min = y_min_;
		x_max = x_max_;
		y_max = y_max_;
		
		action = action_;
		text = text_;

	}
	
	public boolean press(GameView view){ 	
		return view.changeScreen(action);
	}

	
}

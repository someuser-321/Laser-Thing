package com.ChrisAndrew.Luminous;

import android.graphics.Bitmap;

public class Button {

	public int x_min, x_max;
	public int y_min, y_max;
	String action;
	String text;
	float textsize;
	Bitmap normal, pressed;
	
	
	public Button(){
		
	}
	
	public Button(int x_min_, int y_min_, int x_max_, int y_max_, String action_, Bitmap button_bmp, Bitmap pressed_bmp){
		
		x_min = x_min_;
		y_min = y_min_;
		x_max = x_max_;
		y_max = y_max_;
		
		action = action_;
		normal = button_bmp;
		pressed = pressed_bmp;

	}
	
	private void toggleimage(){
		
		Bitmap tmp = this.normal;
		
		this.normal = this.pressed;
		this.pressed = tmp;
		
	}
	
	public boolean press(GameView view){ 
		
		toggleimage();
		
		return view.changeScreen(action);
	}

	
}

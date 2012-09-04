package com.ChrisAndrew.Luminous;


import android.graphics.Bitmap;
import android.graphics.Paint;


public class Button {

	public int x_min, x_max;
	public int y_min, y_max;
	
	public String action;
	public String text;
	public float textsize;
	
	public Paint paint;
	
	public Bitmap bmp;
	
	
	
	public Button(){
		
	}
	
	public Button(int x_min_, int y_min_, int x_max_, int y_max_, String action_, String text_){
		
		x_min = x_min_;
		y_min = y_min_;
		x_max = x_max_;
		y_max = y_max_;
		
		action = action_;
		text = text_;
		
		System.out.println("Button text = " + text );
		System.out.println("Button action = " + action );

	}
	
	public boolean press(GameView view){
		System.out.println("Button press text = " + text);
		System.out.println("Button press action = " + action);
		return view.changeScreen(action);
	}

	
}

package com.ChrisAndrew.Luminous;


import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;


public class Button {

	public float x_min, x_max;
	public float y_min, y_max;
	
	public String action;
	public String text;
	public float textsize;
	
	public Paint paint;
	
	public Bitmap bmp;
	
	
	public Button(AssetManager assets){

		paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		paint.setTextAlign(Align.CENTER);
		paint.setTypeface(Typeface.createFromAsset(assets, "fonts/laserfont.ttf"));
		paint.setAntiAlias(true);
		
	}
	
	public Button(Image image, String action_, String text_, AssetManager assets){
		
		x_min = image.x;
		x_max = image.x + image.width;
		y_min = image.y;
		y_max = image.y + image.height;
		
		bmp = image.bmp;
		
		text = text_;
		action = action_;
		
		paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		paint.setTextAlign(Align.CENTER);
		paint.setTypeface(Typeface.createFromAsset(assets, "fonts/laserfont.ttf"));
		paint.setAntiAlias(true);
		
	}
	
	public Button(float x_min_, float y_min_, float x_max_, float y_max_, String action_, String text_, Bitmap bmp_, int size, AssetManager assets){
		
		x_min = x_min_;
		y_min = y_min_;
		x_max = x_max_;
		y_max = y_max_;
		
		action = action_;
		text = text_;
		
		bmp = bmp_;
		
		paint = new Paint();
		paint.setARGB(255, 255, 255, 255);
		paint.setTextAlign(Align.CENTER);
		paint.setTypeface(Typeface.createFromAsset(assets, "fonts/laserfont.ttf"));
		paint.setAntiAlias(true);
		paint.setTextSize(size);
		
		Debug.log("Button text = " + text, 0);
		Debug.log("Button action = " + action, 0);

	}
	
	public boolean press(GameView view){
		
		Debug.log("Button press text = " + text, 0);
		Debug.log("Button press action = " + action, 0);
		
		return view.changeScreen(action);
	}

	
}

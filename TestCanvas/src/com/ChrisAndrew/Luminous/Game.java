package com.ChrisAndrew.Luminous;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Game {
	
	private GameView gameView;
	private Button[] buttons;
	private Bitmap bmp_button;
	private Bitmap bmp_mirror;
	
	public Game(GameView view){
		this.gameView = view;
		this.init();
	}
	
	private boolean init(){
		
		boolean success = true;
		
		//bmp_button = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.button);
		//bmp_mirror = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.mirror);
		
		
		return success;
	}
	
	public int tick(){
		
		if (input_tick(gameView.touch) | physics_tick() | render_tick())
			return 1;
		
		return 0;
	}
	
	private boolean input_tick(TouchPoint touch){
		
		for ( Button b : buttons){
			if ( test_intercept(touch, b.x_min, b.x_max, b.y_min, b.y_max) )
				b.press();
		}

		return false;
		
	}
	
	private boolean physics_tick(){
		
		
		return false;
		
	}
	
	private boolean render_tick(){
		
	
		return false;
		
	}

	private boolean test_intercept(TouchPoint touchpoint, float x_min, float x_max, float y_min, float y_max){
		
		if (x_min < touchpoint.x && touchpoint.x < x_max && y_min < touchpoint.y && touchpoint.y < y_max)
			return true;

		return false;
		
	}

}

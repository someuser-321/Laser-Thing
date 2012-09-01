package com.ChrisAndrew.Luminous;

import android.graphics.Bitmap;


public class Game {
	
	private GameView gameView;
	private Button btn_play;//, btn_options, btn_help;
	private Bitmap bmp_button, bmp_button_;//, bmp_mirror, bmp_prism, bmp_rope, bmp_lightbulb;
	private Button[] buttons = new Button[4];
	
	
	public Game(GameView view){
		this.gameView = view;
		this.init();
	}
	
	private boolean init(){
		
		boolean success = true;
		
		
		bmp_button = gameView.config.bmp_button;
		bmp_button_ = gameView.config.bmp_button_;
		
		btn_play = new Button(400, 800, 400, 500, "menu", bmp_button, bmp_button_);
		
		buttons[0] = btn_play;
		
		return success;
	}
	
	public int tick(){
		
		if (input_tick(gameView.touch) | physics_tick() | render_tick())
			return 1;
		
		return 0;
	}
	
	private boolean input_tick(TouchPoint touch){
		
		for ( Button b : buttons){
			if ( b != null && test_intercept(touch, b.x_min, b.x_max, b.y_min, b.y_max) ){
				b.press(gameView);
				return true;
			}
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

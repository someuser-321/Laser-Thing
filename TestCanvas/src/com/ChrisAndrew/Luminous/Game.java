package com.ChrisAndrew.Luminous;


public class Game {
	
	private GameView gameView;
	
	
	public Game(GameView view){
		this.gameView = view;
	}
	
	public int tick(){
		
		if ( input_tick(gameView.touch) | render_tick() )
			return 1;
		if ( gameView.needsUpdate ){
			gameView.needsUpdate = false;
			if ( physics_tick() )
				return 1;
		}
		
		
		return 0;
	}
	
	private boolean input_tick(TouchPoint touch){
		
		for ( Button b : gameView.buttons){
			if ( b != null && test_intercept(touch, b.x_min, b.x_max, b.y_min, b.y_max) ){
				b.press(gameView);
				return true;
			}
		}

		return false;
	}
	
	private boolean physics_tick(){
		
		if ( gameView.currentScreen.type.equals("game") ){
		
			gameView.rays = trace_beams(gameView.source, gameView.obstacles);
			
		}
		
		return false;
	}
	
	private boolean render_tick(){
		
	
		return false;
	}

	private boolean test_intercept(TouchPoint touchpoint, float x_min, float x_max, float y_min, float y_max){
		
		if (x_min < touchpoint.x && touchpoint.x < x_max && y_min < touchpoint.y && touchpoint.y < y_max && touchpoint.down == false )
			return true;

		return false;
	}
	
	private Ray[] trace_beams(Obstacle source_, Obstacle[] obstacles_){
		
		Ray[] ret = new Ray[1];
		
		
		return ret;
	
	}

}

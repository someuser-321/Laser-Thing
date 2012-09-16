package com.ChrisAndrew.Luminous;


import java.math.*;


public class Game {
	
	private GameView gameView;
	
	private final int NUM_RAYS = 100;
	private final double PI = Math.PI;
	
	private Obstacle[] obstacles;
	
	
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
		
			gameView.rays = trace_rays(gameView.source, gameView.obstacles);
			
		}
		
		return false;
	}
	
	private boolean render_tick(){
		
	
		return false;
	}

	private boolean test_intercept(TouchPoint touchpoint, double x_min, double x_max, double y_min, double y_max){
		
		if (x_min < touchpoint.x && touchpoint.x < x_max && y_min < touchpoint.y && touchpoint.y < y_max && touchpoint.down == false )
			return true;

		return false;
	}
	
	private Ray[] trace_rays(Obstacle source_, Obstacle[] obstacles_){
		
		Ray[] ret = new Ray[NUM_RAYS];
		obstacles = obstacles_;
		
		float x = source_.x;
		float y = source_.y;
		
		double angle_const = PI*NUM_RAYS*2;
		
		for ( int i=0 ; i<NUM_RAYS ; i++ ){
			ret[i] = new Ray(x, y, (float)(x+Math.cos(angle_const/i)/10), (float)(y+Math.sin(angle_const/i)/10));
			traverse_ray(ret[i]);
		}
		
		
		return ret;
	}
	
	private Ray[] traverse_ray(Ray ray){
		
		Ray[] ret = new Ray[8];
		ret[0] = ray;
		
		int index = 1;
		
		for ( int i=0 ; i<obstacles.length ; i++ ){
			
			float x = obstacles[i].x;
			float y = obstacles[i].y;
			float x_ = obstacles[i].x_;
			float y_ = obstacles[i].y_;

			if ( ray.intercept(x, y, x_, y_) ){
				
				Debug.log("Intersection! Ray with object["+i+"]", 0);
				
				Ray[] rays = traverse_ray(new Ray(x, y, obstacles[i].normal[0], obstacles[i].normal[1]));
				
				for ( int j=0 ; j<index ; j++ ){
					ret[index] = rays[j];
				}
				index++;
				
			}
			
		}
		
		
		return ret;
	}

}

package com.ChrisAndrew.Luminous;

import android.graphics.Canvas;

public class GameLoopThread extends Thread {
	
	static final long FPS = 30;
	private GameView view;
	private Game game;
	public boolean running = false;

	public GameLoopThread(GameView view) {
		this.view = view;
		this.game = new Game(view);
	}
	 
	public void setRunning(boolean run) {
		running = run;
	}
	 
	@Override
	public void run() {
		
		long ticksPS = 1000 / FPS;
		long startTime;
		long sleepTime;
		
		while (running) {
			Canvas c = null;
			startTime = System.currentTimeMillis();
			try {
				c = view.getHolder().lockCanvas();
				synchronized (view.getHolder()) {
					view.onDraw(c);
				}
			} finally {
				if (c != null) {
					view.getHolder().unlockCanvasAndPost(c);
				}
			}
			
			game.tick();
			
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			if (sleepTime > 0){
				try {
					sleep(sleepTime);
				} catch (Exception e) {}
			}
		}
	}
	
}

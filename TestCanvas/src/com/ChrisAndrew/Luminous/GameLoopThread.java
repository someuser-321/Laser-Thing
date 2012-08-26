package com.ChrisAndrew.Luminous;

import android.graphics.Canvas;

public class GameLoopThread extends Thread {
	
	static final long FPS = 25;
	private GameView view;
	private boolean running = false;

	public GameLoopThread(GameView view) {
		this.view = view;
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
			sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
			if (sleepTime > 0){
				try {
					sleep(sleepTime);
				} catch (Exception e) {}
			}
		}
	}
	
}

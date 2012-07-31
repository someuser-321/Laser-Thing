package com.example.testcanvas;

import android.content.Context;
import android.graphics.*;
import android.view.*;

public class GameView extends SurfaceView {
	
	private Bitmap bmp;
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private int x = 0;
	private int dir = 1;

	public GameView(Context context) {
	
		super(context);
		gameLoopThread = new GameLoopThread(this);
		holder = getHolder();
		holder.addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				
				boolean retry = true;
				gameLoopThread.setRunning(false);
				while (retry) {
					try {
						gameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {
					}
				}
				
			}

			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}

		});

		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

}

	@Override
	protected void onDraw(Canvas canvas) {
    	
		canvas.drawColor(Color.BLACK);
		
		if ( x < getWidth() - bmp.getWidth() && dir == 1) {
			x++;
		} else {
			dir = -1;
		}
		
		if ( x > 0 && dir == -1) {
			x--;
		} else {
			dir = 1;
		}
		
		canvas.drawBitmap(bmp, x, 10, null);

	}
}
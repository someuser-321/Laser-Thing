package com.ChrisAndrew.Luminous;

import com.ChrisAndrew.Luminous.R;

import android.content.Context;
import android.graphics.*;
import android.view.*;

public class GameView extends SurfaceView {
	
	private Bitmap bmp;
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	
	private float dx, x, x_ = 0.0f;
	private float dy, y, y_ = 0.0f;
	private boolean touchDown = false;

	
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
		
		if (touchDown)
			canvas.drawBitmap(bmp, x, y, null);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		
		switch ( ev.getAction() & MotionEvent.ACTION_MASK){
		
			case MotionEvent.ACTION_DOWN:
				x = ev.getX();
				y = ev.getY();
				touchDown = true;
				break;
				
			case MotionEvent.ACTION_MOVE:
				x_ = x;
				y_ = y;
				x = ev.getX();
				y = ev.getY();
				dx = x - x_;
				dy = y - y_;
				touchDown = true;
				invalidate();
				break;
				
			case MotionEvent.ACTION_UP:
				touchDown = false;
				break;
		
		}
		
		return true;
	}
	
}
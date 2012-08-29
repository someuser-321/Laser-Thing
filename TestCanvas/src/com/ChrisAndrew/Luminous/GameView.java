package com.ChrisAndrew.Luminous;

import com.ChrisAndrew.Luminous.R;

import android.content.Context;
import android.graphics.*;
import android.view.*;

public class GameView extends SurfaceView {
	
	private Bitmap bmp, bmp_large;
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	
	public TouchPoint touch;

	
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
		bmp_large = Bitmap.createScaledBitmap(bmp, bmp.getWidth()*2, bmp.getHeight()*2, true);

}

	@Override
	protected void onDraw(Canvas canvas) {
    	
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2, null);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		
		switch ( ev.getAction() & MotionEvent.ACTION_MASK){
		
			case MotionEvent.ACTION_DOWN:
				touch.x = ev.getX();
				touch.y = ev.getY();
				touch.down = true;
				break;
				
			case MotionEvent.ACTION_MOVE:
				touch.x_ = touch.x;
				touch.y_ = touch.y;
				touch.x = ev.getX();
				touch.y = ev.getY();
				touch.down = true;
				invalidate();
				break;
				
			case MotionEvent.ACTION_UP:
				touch.down = false;
				break;
		
		}
		
		return true;
	}
	
}
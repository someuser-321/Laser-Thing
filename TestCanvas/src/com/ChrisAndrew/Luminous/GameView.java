package com.ChrisAndrew.Luminous;

import com.ChrisAndrew.Luminous.R;

import android.content.Context;
import android.graphics.*;
import android.view.*;

public class GameView extends SurfaceView {
	
	private Bitmap bmp, bmp_large;
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	
	private float touch_x, touch_x_ = 0.0f;
	private float touch_y, touch_y_ = 0.0f;
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
		bmp_large = Bitmap.createScaledBitmap(bmp, bmp.getWidth()*2, bmp.getHeight()*2, true);

}

	@Override
	protected void onDraw(Canvas canvas) {
    	
		canvas.drawColor(Color.BLACK);
		canvas.drawBitmap(bmp_large, touch_x - bmp_large.getWidth()/2, touch_y - bmp_large.getHeight()/2, null);

	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev){
		
		switch ( ev.getAction() & MotionEvent.ACTION_MASK){
		
			case MotionEvent.ACTION_DOWN:
				touch_x = ev.getX();
				touch_y = ev.getY();
				touchDown = true;
				break;
				
			case MotionEvent.ACTION_MOVE:
				touch_x_ = touch_x;
				touch_y_ = touch_y;
				touch_x = ev.getX();
				touch_y = ev.getY();
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
package com.ChrisAndrew.Luminous;

import com.ChrisAndrew.Luminous.R;

import android.content.Context;
import android.graphics.*;
import android.view.*;

public class GameView extends SurfaceView {
	
	private Bitmap bmp, bmp_large;
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	
	private boolean flag = false;
	
	public TouchPoint touch = new TouchPoint();
	
    private Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
    private Typeface mFace;
    
    int colour = 0;

	
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
				if ( gameLoopThread.running == false && flag == false ){
					gameLoopThread.setRunning(true);
					gameLoopThread.start();
					flag = true;
				} else if ( gameLoopThread.running == false && flag == true ){
					gameLoopThread.setRunning(true);
					gameLoopThread.run();
				}
			}

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			}

		});

		bmp = BitmapFactory.decodeResource(getResources(), R.drawable.button_/*ic_launcher*/);
		bmp_large = Bitmap.createScaledBitmap(bmp, bmp.getWidth()*3/2, bmp.getHeight()*3/2, true);
		
		mFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/laserfont.ttf");  
		myPaint.setTextSize(32);
		myPaint.setARGB(255, 255, 200, 200);
		myPaint.setTypeface(mFace);

	}

	@Override
	protected void onDraw(Canvas canvas) {
    	if (canvas != null){
    		if ( colour < 255 ){
    			colour += 32;
    		} else {
    			colour = 0;
    		}
    		canvas.drawColor(Color.BLACK);
    		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2, null);
    		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2 + 1*(bmp_large.getHeight()/4 + 32), null);
    		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2 + 2*(bmp_large.getHeight()/4 + 32), null);
    		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2 + 3*(bmp_large.getHeight()/4 + 32), null);
    		
    		myPaint.setARGB(255, colour, colour, 255-colour);
        	canvas.drawText("BACON!!!", touch.x - 64, touch.y, myPaint);
    	}

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
				break;
				
			case MotionEvent.ACTION_UP:
				touch.down = false;
				break;
		
		}
		
		return true;
	}
	
	public boolean changeview(int action){
		
		
		
		return true;
		
	}

	
}
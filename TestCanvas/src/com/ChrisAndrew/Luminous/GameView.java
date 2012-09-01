package com.ChrisAndrew.Luminous;

import java.io.IOException;

import com.ChrisAndrew.Luminous.R;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.view.*;

public class GameView extends SurfaceView {
	
	private Bitmap bmp, bmp_large;
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private boolean flag = false;
	
	public Context context;
	
	public TouchPoint touch = new TouchPoint();
	
    private Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
    private Typeface mFace;
    private int colour = 0;
    private String text = "BACON!!!";
    
    public ConfigManager config = new ConfigManager(this);

	
	public GameView(Context context_) {
	
		super(context_);
		
		context = context_;	
		
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

		try {
			bmp = BitmapFactory.decodeStream(context.getAssets().open("ic_launcher"));
		} catch (IOException e) {}
		
		bmp_large = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/2, bmp.getHeight()/2, true);
		
		mFace = Typeface.createFromAsset(getContext().getAssets(), "fonts/laserfont.ttf");  
		myPaint.setTextSize(64);
		myPaint.setARGB(255, 255, 200, 200);
		myPaint.setTypeface(mFace);
		myPaint.setTextAlign(Align.CENTER);

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
    		myPaint.setARGB(255, colour, colour, 255-colour);
    		
    		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2, null);
    		canvas.drawText(text, touch.x, touch.y, myPaint);
    		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2 + 1*(bmp_large.getHeight()/4 + 32), null);
    		canvas.drawText(text, touch.x, touch.y + 1*(bmp_large.getHeight()/4 + 32), myPaint);
    		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2 + 2*(bmp_large.getHeight()/4 + 32), null);
    		canvas.drawText(text, touch.x, touch.y + 2*(bmp_large.getHeight()/4 + 32), myPaint);
    		canvas.drawBitmap(bmp_large, touch.x - bmp_large.getWidth()/2, touch.y - bmp_large.getHeight()/2 + 3*(bmp_large.getHeight()/4 + 32), null);
    		canvas.drawText(text, touch.x, touch.y + 3*(bmp_large.getHeight()/4 + 32), myPaint);
    		
        	
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
	
	public boolean changescreen(int action){
		
		try {
			Thread.sleep(200);
		} catch (Exception e) {
			
		}
		
		
		
		//change current view
		
		
		return true;
		
	}

	
}
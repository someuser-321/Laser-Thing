package com.ChrisAndrew.Luminous;


import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    public Button[] buttons;
    private String[] text;
    private Bitmap background;
    
    public ConfigManager config;

	
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

			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

			}
		);

		try {
			bmp = BitmapFactory.decodeStream(context.getAssets().open("images/lightbulb.png"));
		} catch (IOException e) {}
		
		bmp_large = Bitmap.createScaledBitmap(bmp, bmp.getWidth()/2, bmp.getHeight()/2, true);
		
		mFace = Typeface.createFromAsset(context.getAssets(), "fonts/laserfont.ttf");  
		myPaint.setTextSize(32);
		myPaint.setARGB(255, 255, 255, 255);
		myPaint.setTypeface(mFace);
		myPaint.setTextAlign(Align.CENTER);
		myPaint.setAntiAlias(true);

		
		config = new ConfigManager(this);
		changeScreen("menu");

	}

	@Override
	protected void onDraw(Canvas canvas) {
		
    	if (canvas != null){
    		
    		canvas.drawColor(Color.WHITE);
    		
    		int x = 0, y = 0;
    		while (x < 64) {
    		    while (y < 32) {
    		        canvas.drawBitmap(background, x*background.getWidth()/*+x*/, y*background.getHeight()/*+y*/, null);
    		        y += 1;
    		    }
    		    x += 1;
    		    y = 0;
    		}

    		for ( int i=0 ; i<buttons.length ; i++ ){
    			canvas.drawBitmap(buttons[i].normal, buttons[i].x_min, buttons[i].y_min, null);
    			myPaint.setTextSize(buttons[i].textsize);
    			canvas.drawText(buttons[i].text, (buttons[i].x_min + buttons[i].x_max)/2, (buttons[i].y_min + buttons[i].y_max)/2+16, myPaint);
    		}    		

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
	
	public boolean changeScreen(String action){
		
		Node screen = config.getScreen(action);
		Node buttons_ = config.getButtonNodes(screen);
		
		buttons = new Button[buttons_.getChildNodes().getLength()];
		
		for ( int i=0 ; i<buttons_.getChildNodes().getLength() ; i++){
			Node curButton = buttons_.getChildNodes().item(i);
			int x = Integer.parseInt(config.getNodeAttribute("x", curButton));
			int y = Integer.parseInt(config.getNodeAttribute("y", curButton));
			int width = Integer.parseInt(config.getNodeAttribute("width", curButton));
			int height = Integer.parseInt(config.getNodeAttribute("height", curButton));
			
			buttons[i] = new Button();
			
			buttons[i].x_min = x;
			buttons[i].x_max = x + width;
			buttons[i].y_min = y;
			buttons[i].y_max = y + height;
			buttons[i].action = config.getNodeAttribute("screen", curButton);
			buttons[i].text = config.getNodeAttribute("text", curButton);
			buttons[i].textsize = 16;
			if ( config.getNodeAttribute("textsize", curButton) != null )
				buttons[i].textsize = Float.parseFloat(config.getNodeAttribute("textsize", curButton));
			
			try {
				buttons[i].normal = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(context.getAssets().open(config.getNodeAttribute("img", curButton))), width, height, false);
				buttons[i].pressed = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(context.getAssets().open(config.getNodeAttribute("img_pressed", curButton))), width, height, false);
			} catch (IOException e) {
				return false;
			}
		}
		
		Node bg = config.getBackgroundNodes(screen).getChildNodes().item(0);
		
		try {
			background = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(context.getAssets().open(config.getNodeAttribute("img", bg))), 64, 64, false);
		} catch (IOException e) {}
		
		
		return true;
	}

	
}
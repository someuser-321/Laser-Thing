package com.ChrisAndrew.Luminous;


import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Align;
import android.util.DisplayMetrics;
import android.view.*;


public class GameView extends SurfaceView {
	
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private boolean flag = false;
	
	public Context context;
	
	public TouchPoint touch = new TouchPoint();
	
    private Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
    private Typeface mFace;
    public Button[] buttons;
    private Text[] text;
    private Bitmap background;
    
    public int screenwidth, screenheight = 0;
    
    public ConfigManager config;

	
	public GameView(Context context_) {
	
		super(context_);
		
		context = context_;
		
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		
		screenwidth = dm.widthPixels;
		System.out.println(screenwidth);
		screenheight = dm.heightPixels;
		System.out.println(screenheight);
		
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
    		
    		canvas.drawColor(Color.RED);
    		
    		int x = 0, y = 0;
    		while ( x < screenwidth ) {
    		    while ( y < screenheight ) {
    		        canvas.drawBitmap(background, x, y, null);
    		        y += background.getHeight();
    		    }
    		    x += background.getWidth();
    		    y = 0;
    		}

    		for ( int i=0 ; i<buttons.length ; i++ ){
    			canvas.drawBitmap(buttons[i].normal, buttons[i].x_min, buttons[i].y_min, null);
    			myPaint.setTextSize(buttons[i].textsize);
    			canvas.drawText(buttons[i].text, (buttons[i].x_min + buttons[i].x_max)/2, (buttons[i].y_min + buttons[i].y_max)/2+16, myPaint);
    		}
    		
    		for ( int i=0 ; i<text.length ; i++ ){
    			myPaint.setTextSize(text[i].size);
    			myPaint.setARGB(255, text[i].r, text[i].g, text[i].b);
    			myPaint.setTextAlign(text[i].align);
    			canvas.drawText(text[i].text, text[i].x, text[i].y, myPaint);
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
		Node text_ = config.getTextNodes(screen);
		
		buttons = new Button[buttons_.getChildNodes().getLength()];
		text = new Text[text_.getChildNodes().getLength()];
		
		int width_ = Integer.parseInt(config.getNodeAttribute("width", config.getRootNode("screens")));
		int height_ = Integer.parseInt(config.getNodeAttribute("height", config.getRootNode("screens")));
		
		
		for ( int i=0 ; i<buttons_.getChildNodes().getLength() ; i++){
			
			Node curButton = buttons_.getChildNodes().item(i);
			int x = Integer.parseInt(config.getNodeAttribute("x", curButton))*screenwidth/width_;
			int y = Integer.parseInt(config.getNodeAttribute("y", curButton))*screenheight/height_;
			int width = Integer.parseInt(config.getNodeAttribute("width", curButton))*screenwidth/width_;
			int height = Integer.parseInt(config.getNodeAttribute("height", curButton))*screenheight/height_;
			
			buttons[i] = new Button(x, y, x + width, y + height, config.getNodeAttribute("screen", curButton), config.getNodeAttribute("text", curButton));

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
		
		
		for ( int i=0 ; i<text_.getChildNodes().getLength() ; i++ ){
			
			Node curText = text_.getChildNodes().item(i);
			int x = Integer.parseInt(config.getNodeAttribute("x", curText))*screenwidth/width_;
			int y = Integer.parseInt(config.getNodeAttribute("y", curText))*screenheight/height_;
			int size = Integer.parseInt(config.getNodeAttribute("size", curText));
			int r = Integer.parseInt(config.getNodeAttribute("r", curText));
			int g = Integer.parseInt(config.getNodeAttribute("g", curText));
			int b = Integer.parseInt(config.getNodeAttribute("b", curText));
			String align = config.getNodeAttribute("align", curText);
			
			text[i] = new Text(x, y, curText.getNodeValue(), size, align, r, g, b);
			
		}
		
		
		Node bg = config.getBackgroundNodes(screen).getChildNodes().item(0);
		
		try {
			background = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(context.getAssets().open(config.getNodeAttribute("img", bg))), 64, 64, false);
		} catch (IOException e) {}
		
		
		return true;
	}

	
}
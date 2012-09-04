package com.ChrisAndrew.Luminous;


import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.view.*;


public class GameView extends SurfaceView {
	
	private SurfaceHolder holder;
	private GameLoopThread gameLoopThread;
	private boolean flag = false;
	
	public Context context;
	
	public TouchPoint touch = new TouchPoint();

    public Button[] buttons;
    private Text[] text;
    private Bitmap background;
    
    public int screenwidth, screenheight = 0;
    public boolean tile = false;
    
    public ConfigManager config;

	
	public GameView(Context context_) {
	
		super(context_);
		
		context = context_;
		
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		
		screenwidth = dm.widthPixels;
		screenheight = dm.heightPixels;
		
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

		
		config = new ConfigManager(context);
		changeScreen("menu");

	}

	@Override
	protected void onDraw(Canvas canvas) {
		
    	if (canvas != null){
    		
    		canvas.drawColor(Color.BLACK);
    		
    		if ( tile ){
    			int x = 0, y = 0;
    			while ( x < screenwidth ) {
    				while ( y < screenheight ) {
    					canvas.drawBitmap(background, x, y, null);
    					y += background.getHeight();
    				}
    				x += background.getWidth();
    				y = 0;
    			}
    		}

    		for ( int i=0 ; i<buttons.length ; i++ ){
    			canvas.drawBitmap(buttons[i].bmp, buttons[i].x_min, buttons[i].y_min, null);
    			canvas.drawText(buttons[i].text, (buttons[i].x_min + buttons[i].x_max)/2, (buttons[i].y_min + buttons[i].y_max)/2+16, buttons[i].paint);
    		}
    		
    		for ( int i=0 ; i<text.length ; i++ ){
    			canvas.drawText(text[i].text, text[i].x, text[i].y, text[i].paint);
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
		NodeList buttons_ = config.getButtons(screen).getChildNodes();
		NodeList text_ = config.getText(screen).getChildNodes();
		
		buttons = new Button[buttons_.getLength()];
		System.out.println("buttons_.getLength() returned '" + buttons_.getLength() + "'");
		System.out.println("buttons.length() returned '" + buttons.length + "'");
		
		text = new Text[text_.getLength()];
		System.out.println("text_.getLength() returned '" + text_.getLength() + "'");
		System.out.println("text.length() returned '" + text.length + "'");	
		
		
		for ( int i=0 ; i<buttons_.getLength() ; i++ ){
			
			Node e = buttons_.item(i);
			buttons[i] = new Button(context.getAssets());
			
			int x = Integer.parseInt(config.getAttribute(e, "x"));
			System.out.println("x assigned as '" + x + "'");
			int y = Integer.parseInt(config.getAttribute(e, "y"));
			System.out.println("y assigned as '" + y + "'");
			
			int width = Integer.parseInt(config.getAttribute(e, "width"));
			System.out.println("width assigned as '" + width + "'");
			int height = Integer.parseInt(config.getAttribute(e, "height"));
			System.out.println("height assigned as '" + height + "'");

			String buttontext = e.getFirstChild().getNodeValue();
			System.out.println("text assigned as '" + buttontext + "'");
			float textsize = Float.parseFloat(config.getAttribute(e, "textsize"));
			System.out.println("textsize assigned as '" + textsize + "'");
			
			String action_ = config.getAttribute(e, "screen");
			System.out.println("screen assigned as '" + action_ + "'");
			String bmp = config.getAttribute(e, "img");
			System.out.println("bmp assigned as '" + bmp + "'");
			
			buttons[i].x_min = x;
			buttons[i].x_max = x + width;
			buttons[i].y_min = y;
			buttons[i].y_max = y + height;
			
			buttons[i].text = buttontext;
			buttons[i].paint.setTextSize(textsize);
			
			buttons[i].action = action_;
			
			try {
				buttons[i].bmp = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(context.getAssets().open(bmp)), width, height, false);
			} catch ( IOException err ){
				System.out.println("IOException: unable to get button bitmap");
			}
			
		}
		
		for ( int i=0 ; i<text_.getLength() ; i++ ){
			
			Node e = text_.item(i);
			text[i] = new Text();
			
			int x = Integer.parseInt(config.getAttribute(e, "x"));
			System.out.println("x assigned as '" + x + "'");
			int y = Integer.parseInt(config.getAttribute(e, "y"));
			System.out.println("y assigned as '" + y + "'");
			
			int r = Integer.parseInt(config.getAttribute(e, "r"));
			System.out.println("r assigned as '" + r + "'");
			int g = Integer.parseInt(config.getAttribute(e, "g"));
			System.out.println("g assigned as '" + g + "'");
			int b = Integer.parseInt(config.getAttribute(e, "b"));
			System.out.println("b assigned as '" + b + "'");
			
			Float size = Float.parseFloat(config.getAttribute(e, "size"));
			System.out.println("size assigned as '" + size + "'");
			String align = config.getAttribute(e, "align");
			System.out.println("align assigned as '" + align + "'");
			String texttext = e.getFirstChild().getNodeValue();
			System.out.println("texttext assigned as '" + texttext + "'");
			
			text[i].x = x;
			text[i].y = y;

			text[i].size = size;
			text[i].text = texttext;
			text[i].setPaint(r, g, b, size, align, context.getAssets());

		}
		
		return true;
	}

	
}
package com.ChrisAndrew.Luminous;


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

    public float screenwidth, screenheight = 0;
    public boolean tile = false;
    
    public ConfigManager config;
    
    public Screen currentScreen;
    public Button[] buttons;
    private Image[] images;
    private Text[] text;
    public Obstacle[] obstacles;
    
    public Obstacle source;
    public Ray[] rays;

    public boolean needsUpdate = true;
	
	public GameView(Context context_) {

		super(context_);
		
		context = context_;
		
		Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		
		screenwidth = dm.widthPixels;
		Debug.log("screenwidth = " + screenwidth, 10);
		screenheight = dm.heightPixels;
		Debug.log("screenheight = " + screenheight, 10);
		
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
					} catch (InterruptedException e) {}
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

		long start = System.currentTimeMillis();
		config = new ConfigManager(context.getAssets());
		Debug.log("Total: " + String.valueOf(System.currentTimeMillis() - start) + "ms", 100);
		
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
    					canvas.drawBitmap(currentScreen.bg, x, y, null);
    					y += currentScreen.bg.getHeight();
    				}
    				x += currentScreen.bg.getWidth();
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
    		
    		for ( int i=0 ; i<images.length ; i++ ){
    			canvas.drawBitmap(images[i].bmp, images[i].x, images[i].y, null);
    		}    	
    		
    		for ( int i=0 ; i<obstacles.length ; i++ ){
    			canvas.drawBitmap(obstacles[i].bmp, obstacles[i].x, obstacles[i].y, null);
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

		touch = new TouchPoint();
		
		if ( action.equals("none") ){
			return false;
		}
		
		currentScreen = config.screenCache.get(action);
		
		buttons = currentScreen.buttons;
		text = currentScreen.text;
		images = currentScreen.images;
		obstacles = currentScreen.obstacles;
		
		
		return true;
	}

	
}
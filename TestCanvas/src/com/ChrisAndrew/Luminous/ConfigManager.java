package com.ChrisAndrew.Luminous;

//import java.util.*;
import java.io.*;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ConfigManager {

	//private String configlocation = "config/config.xml";
	
	public Bitmap bmp_button;
	public Bitmap bmp_button_;
	public Bitmap bmp_mirror;
	public Bitmap bmp_lightbulb;
	public Bitmap bmp_prism;
	public Bitmap bmp_rope;

	
	public ConfigManager(GameView view){
		
		AssetManager assets = view.context.getAssets();

		try {
			bmp_button = BitmapFactory.decodeStream(assets.open("images/button.png"));
			bmp_button_ = BitmapFactory.decodeStream(assets.open("images/button_.png"));
			bmp_mirror = BitmapFactory.decodeStream(assets.open("images/mirror.png"));
			bmp_lightbulb = BitmapFactory.decodeStream(assets.open("images/lightbulb.png"));
			bmp_prism = BitmapFactory.decodeStream(assets.open("images/prism.png"));
			bmp_rope = BitmapFactory.decodeStream(assets.open("images/rope.png"));
		} catch (IOException e) {
		}
	
	}
	
}

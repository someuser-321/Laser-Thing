package com.ChrisAndrew.Luminous;

import android.graphics.Bitmap;


public class Screen {
	
	public Button[] buttons;
	public Text[] text;
	public Image[] images;
	public Obstacle[] obstacles;
	
	public Obstacle source;
	
	public Bitmap bg;
	
	public String name;
	public String type;
	
	
	public Screen(Button[] buttons_, Text[] text_, Image[] images_, Obstacle[] obstacles_, Obstacle source_, Bitmap bg_, String name_, String type_){
		
		buttons = buttons_;
		text = text_;
		images = images_;
		obstacles = obstacles_;
		
		source = source_;
		
		bg = bg_;
		name = name_;
		type = type_;
		
	}


	public Screen() {
	}

}

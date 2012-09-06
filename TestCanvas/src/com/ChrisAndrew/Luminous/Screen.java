package com.ChrisAndrew.Luminous;

import android.graphics.Bitmap;


public class Screen {
	
	public Button[] buttons;
	public Text[] text;
	public Image[] images;
	public Bitmap bg;
	
	public String name;
	
	
	public Screen(Button[] buttons_, Text[] text_, Image[] images_, Bitmap bg_, String name_){
		
		buttons = buttons_;
		text = text_;
		images = images_;
		
		bg = bg_;
		name = name_;
		
	}


	public Screen() {
	}

}

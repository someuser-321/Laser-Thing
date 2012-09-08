package com.ChrisAndrew.Luminous;


public class Debug {
	
	static private boolean debug = true;
	static private int level = 100;
	
	static public void log(String msg, int level_){
		
		if ( debug && level_ >= level ){
			System.out.println(msg);
		}
	}

}

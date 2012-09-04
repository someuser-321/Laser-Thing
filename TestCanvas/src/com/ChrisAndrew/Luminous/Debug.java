package com.ChrisAndrew.Luminous;


public class Debug {
	
	static private boolean debug = true;
	
	static public void log(String msg){
		if (debug) System.out.println(msg);
	}

}

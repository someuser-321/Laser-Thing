package com.ChrisAndrew.Luminous;


import java.io.*;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ConfigManager {

	private String configfile = "config/config.xml";
	private InputStream file;
	private AssetManager assets_;
	
	public Hashtable<String, Screen> screenCache = new Hashtable<String, Screen>();
	public Hashtable<String, Bitmap> bitmapCache = new Hashtable<String, Bitmap>();
	
	public float width, height;
	
	
	
	public ConfigManager(AssetManager assets){
		
		try {
			
			assets_ = assets;
			file = assets_.open(configfile);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			NodeList screens = doc.getElementsByTagName("screens").item(0).getChildNodes();
			width = 1280;
			height = 752;
			
			if ( screens == null ){
				Debug.log("screens = null", 100);
			} else {
				Debug.log("screen width = '" + width + "'", 0);
				Debug.log("screen height = '" + height + "'", 0);
				Debug.log("screens.getLength() = " + screens.getLength(), 0);
			
				for ( int i=0 ; i<screens.getLength() ; i++ ){
					
					Node screen = screens.item(i);
					String type = getAttribute(screen, "type");
					
					if ( screen.getNodeType() != Node.TEXT_NODE){
						
						String name = getAttribute(screen, "name");
						Debug.log("screen name = " + name, 0);

						long start = System.currentTimeMillis();
						Hashtable<String, Image> images = getImages(screen);
						Image[] images_ = new Image[images.size()];
						Debug.log("Images: " + String.valueOf(System.currentTimeMillis() - start) + "ms", 100);
						
						int j = 0;
						for ( String key : images.keySet() ){
							images_[j] = images.get(key);
							j++;
						}
						
						
						start = System.currentTimeMillis();
						Hashtable<String, Button> buttons = getButtons(screen);
						Button[] buttons_ = new Button[buttons.size()];
						Debug.log("Buttons: " + String.valueOf(System.currentTimeMillis() - start) + "ms", 100);
						
						j = 0;
						for ( String key : buttons.keySet() ){
							buttons_[j] = buttons.get(key);
							j++;
						}
					
						
						start = System.currentTimeMillis();
						Hashtable<String, Text> text = getText(screen);
						Text[] text_ = new Text[text.size()];
						Debug.log("Text: " + String.valueOf(System.currentTimeMillis() - start) + "ms", 100);

						j = 0;
						for ( String key : text.keySet() ){
							text_[j] = text.get(key);
							j++;
						}
					
						String bgname = getAttribute(screen, "bg");
						Debug.log("bg = " + bgname, 0);
						Bitmap background = BitmapFactory.decodeStream(assets_.open(bgname));
					
						screenCache.put(name, new Screen(buttons_, text_, images_, background, name, type));
					
					}

				}
			
			}
				
			
		} catch ( IOException e ){
			Debug.log("IOException: Unable to open config file", 1000);
		} catch ( ParserConfigurationException e ){
			Debug.log("ParserConfigurationException: Unable to build document", 1000);
		} catch ( SAXException e ){
			Debug.log("SAXException: Unable to parse config file", 1000);
		}
		
	}

	public Node getScreen(String name, NodeList screens_){
		
		Node ret = null;
		for ( int i=0 ; i<screens_.getLength() ; i++ ){
			if ( screens_.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( getAttribute(screens_.item(i), "name").equals(name) ){
					ret = screens_.item(i);
					break;
				}
			}
		}
		
		if ( ret == null )
			Debug.log("getScreen() ret = null", 100);
		else
			Debug.log("getScreen() returned a node of length '" + ret.getChildNodes().getLength() + "'", 0);
		
		return ret;
	}
	
	public String getAttribute(Node node, String attr){
		
		String ret = null;
		
		NamedNodeMap attributes = node.getAttributes();
		
		if ( attributes != null ){
			for ( int i=0 ; i<attributes.getLength() ; i++ ){
				if ( attributes.item(i).getNodeName().equals(attr) ){
					ret = attributes.item(i).getNodeValue();
					break;
				}
			}
		}
		
		if ( ret == null )
			Debug.log("getAttribute(" + attr + ") ret = null", 0);
		else
			Debug.log("getAttribute(" + attr + ") returned '" + ret + "'", 0);
		
		return ret;
	}
	
	public Hashtable<String, Button> getButtons(Node screen){
		
		NodeList root = screen.getChildNodes();
		
		Hashtable<String, Button> buttonCache = new Hashtable<String, Button>();
		
		int i;
		for ( i=0 ; i<root.getLength() ; i++ ){
			if ( root.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( root.item(i).getNodeName().equals("button") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					int width = Integer.parseInt(getAttribute(root.item(i), "width"));
					int height = Integer.parseInt(getAttribute(root.item(i), "height"));
					int size = Integer.parseInt(getAttribute(root.item(i), "textsize"));
					
					String action = getAttribute(root.item(i), "screen");
					String text = root.item(i).getFirstChild().getNodeValue();
					
					if ( text == null )
						Debug.log("text is null", 100);
					
					String src = getAttribute(root.item(i), "img");
					
					if ( !bitmapCache.containsKey(src) ){
						bitmapCache.put(String.valueOf(src), getBitmap(src, width, height));
					}

					Bitmap bmp = bitmapCache.get(src);
					
					
					Button button = new Button(x, y, x + width, y + height, action, text, bmp, size, assets_);
					buttonCache.put(String.valueOf(i), button);
				}
					
			
			}
		}
		

		Debug.log("getImages() found '" + i + "' image(s)", 0);
		return buttonCache;
	}
	
	public Hashtable<String, Text> getText(Node screen){
		
		NodeList root = screen.getChildNodes();
		
		Hashtable<String, Text> textCache = new Hashtable<String, Text>();
		
		int i;
		for ( i=0 ; i<root.getLength() ; i++ ){
			if ( root.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( root.item(i).getNodeName().equals("text") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					
					int r = Integer.parseInt(getAttribute(root.item(i), "r"));
					int g = Integer.parseInt(getAttribute(root.item(i), "g"));
					int b = Integer.parseInt(getAttribute(root.item(i), "b"));
					
					String align = getAttribute(root.item(i), "align");
					int size = Integer.parseInt(getAttribute(root.item(i), "size"));
					
					String text = root.item(i).getFirstChild().getNodeValue();


					Text txt = new Text(x, y, text, size, align, r, g, b, assets_);

					textCache.put(String.valueOf(i), txt);
				}
			}
		}
		
		Debug.log("getText() found '" + i + "' text(s)", 0);
		return textCache;
	}
	
	public Hashtable<String, Image> getImages(Node screen){
		
		NodeList root = screen.getChildNodes();
		Hashtable<String, Image> ret = new Hashtable<String, Image>();
		
		Debug.log("(getImages) root node is length " + root.getLength(), 0);
		
		int i;
		for ( i=0 ; i<root.getLength() ; i++ ){
			if ( root.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( root.item(i).getNodeName().equals("image") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					int width = Integer.parseInt(getAttribute(root.item(i), "width"));
					int height = Integer.parseInt(getAttribute(root.item(i), "height"));
					
					String src = getAttribute(root.item(i), "img");
					
					if ( !bitmapCache.containsKey(src) ){
						bitmapCache.put(String.valueOf(src), getBitmap(src, width, height));
					}
					
					Image img = new Image(x, y, width, height, bitmapCache.get(src));
					
					ret.put(String.valueOf(i), img);

				}
			}
		}
		
		Debug.log("getImages() found '" + i + "' image(s)", 0);
		return ret;
	}
	
	private Bitmap getBitmap(String src, float width_, float height_){
		
		Bitmap ret = null;

		try {
			ret = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(assets_.open(src)), (int)width_, (int)height_, false);
		} catch  ( IOException e ) {}
		
		return ret;
	}

}

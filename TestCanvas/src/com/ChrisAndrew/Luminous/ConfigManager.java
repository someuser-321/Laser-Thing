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
	public Hashtable<String, Image> images = new Hashtable<String, Image>();
	
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
				Debug.log("screens = null");
			} else {
				Debug.log("screen width = '" + width + "'");
				Debug.log("screen height = '" + height + "'");
				Debug.log("screens.getLength() = " + screens.getLength());
			
				for ( int i=0 ; i<screens.getLength() ; i++ ){
					
					Node screen = screens.item(i);
					
					if ( screen.getNodeType() != Node.TEXT_NODE){
						
						String name = getAttribute(screen, "name");
						Debug.log("screen name = " + name);

						long start = System.currentTimeMillis();
						getImages(screen);
						Image[] images_ = new Image[images.size()];
						System.out.println("Images: " + String.valueOf(System.currentTimeMillis() - start));
						
						int j = 0;
						for ( String key : images.keySet() ){
							images_[j] = images.get(key);
							j++;
						}
						
						
						start = System.currentTimeMillis();
						Hashtable<String, Button> buttons = getButtons(screen);
						Button[] buttons_ = new Button[buttons.size()];
						System.out.println("Buttons: " + String.valueOf(System.currentTimeMillis() - start));
						
						j = 0;
						for ( String key : buttons.keySet() ){
							buttons_[j] = buttons.get(key);
							j++;
						}
					
						
						start = System.currentTimeMillis();
						Hashtable<String, Text> text = getText(screen);
						Text[] text_ = new Text[text.size()];
						System.out.println("Text: " + String.valueOf(System.currentTimeMillis() - start));

						j = 0;
						for ( String key : text.keySet() ){
							text_[j] = text.get(key);
							j++;
						}
					
						String bgname = getAttribute(screen, "bg");
						Debug.log("bg = " + bgname);
						Bitmap background = BitmapFactory.decodeStream(assets_.open(bgname));
					
						screenCache.put(name, new Screen(buttons_, text_, images_, background, name));
					
					} else {
						Debug.log("screen is null");
					}

				}
			
			}
				
			
		} catch ( IOException e ){
			Debug.log("IOException: Unable to open config file");
		} catch ( ParserConfigurationException e ){
			Debug.log("ParserConfigurationException: Unable to build document");
		} catch ( SAXException e ){
			Debug.log("SAXException: Unable to parse config file");
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
			Debug.log("getScreen() ret = null");
		else
			Debug.log("getScreen() returned a node of length '" + ret.getChildNodes().getLength() + "'");
		
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
			Debug.log("getAttribute(" + attr + ") ret = null");
		else
			Debug.log("getAttribute(" + attr + ") returned '" + ret + "'");
		
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
						Debug.log("text is null");
					
					String src = getAttribute(root.item(i), "img");
					
					if ( !images.containsKey(src) ){
						images.put(String.valueOf(src), getImage(src, x, y, width, height));
					}

					Bitmap bmp = images.get(src).bmp;
					
					
					Button button = new Button(x, y, x + width, y + height, action, text, bmp, size, assets_);
					buttonCache.put(String.valueOf(i), button);
				}
					
			
			}
		}
		

		Debug.log("getImages() found '" + i + "' image(s)");
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
		
		Debug.log("getText() found '" + i + "' text(s)");
		return textCache;
	}
	
	public void /*Hashtable<String, Image>*/ getImages(Node screen){
		
		NodeList root = screen.getChildNodes();
		
		//Hashtable<String, Image> imageCache = new Hashtable<String, Image>();
		
		Debug.log("(getImages) root node is length " + root.getLength());
		
		int i;
		for ( i=0 ; i<root.getLength() ; i++ ){
			if ( root.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( root.item(i).getNodeName().equals("image") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					float width = Integer.parseInt(getAttribute(root.item(i), "width"));
					float height = Integer.parseInt(getAttribute(root.item(i), "height"));
					
					String src = getAttribute(root.item(i), "img");
					
					if ( !images.containsKey(src) ){
						images.put(String.valueOf(src), getImage(src, x, y, width, height));
					}

				}
			}
		}
		

		Debug.log("getImages() found '" + i + "' image(s)");
		//return imageCache;
	}
	
	private Image getImage(String src, float x, float y, float width_, float height_){
		
		Image ret = null;

		try {
			ret = new Image(x, y, width_, height_, Bitmap.createScaledBitmap(BitmapFactory.decodeStream(assets_.open(src)), (int)width_, (int)height_, false));
		} catch  ( IOException e ) {}
		
		return ret;
	}

}

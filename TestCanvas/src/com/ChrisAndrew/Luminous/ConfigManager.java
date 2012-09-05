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
	
	private NodeList screens;
	public Hashtable<String, Screen> screenCache = new Hashtable<String, Screen>();
	
	public float width, height;
	
	
	
	public ConfigManager(AssetManager assets){
		
		try {
			
			assets_ = assets;
			file = assets_.open(configfile);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			screens = doc.getElementsByTagName("screens").item(0).getChildNodes();
			width = 1280;
			height = 752;
			
			if ( screens == null ){
				Debug.log("screens = null");
			} else {
				Debug.log("screen width = '" + width + "'");
				Debug.log("screen height = '" + height + "'");
			
				for ( int i=0 ; i<screens.getLength() ; i++ ){
					
					Node screen = screens.item(i);
					String name = screen.getNodeValue();
					
					Hashtable<String, Button> buttons = getButtons(screen);
					Button[] buttons_ = new Button[buttons.size()];
					for ( String key : buttons.keySet() ){
						buttons_[i] = buttons.get(key);
					}
					
					Hashtable<String, Image> images = getImages(screen);
					Image[] images_ = new Image[images.size()];
					for ( String key : images.keySet() ){
						images_[i] = images.get(key);
					}
					
					Hashtable<String, Text> text = getText(screen);
					Text[] text_ = new Text[text.size()];

					for ( String key : text.keySet() ){
						text_[i] = text.get(key);
					}
					
					String bgname = getAttribute(screen, "bg");
					Bitmap background = BitmapFactory.decodeStream(assets_.open(bgname));
					
					screenCache.put(name, new Screen(buttons_, text_, images_, background));

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

	public Node getScreen(String name){
		
		Node ret = null;
		for ( int i=0 ; i<screens.getLength() ; i++ ){
			if ( screens.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( getAttribute(screens.item(i), "name").equals(name) ){
					ret = screens.item(i);
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
					float width = Float.parseFloat(getAttribute(root.item(i), "width"));
					float height = Float.parseFloat(getAttribute(root.item(i), "height"));
					
					String action = getAttribute(root.item(i), "action");
					String text = getAttribute(root.item(i), "text");
					
					Bitmap bmp = null;
					
					try {
						bmp = BitmapFactory.decodeStream(assets_.open(getAttribute(root.item(i), "img")));
					} catch  ( IOException e ) {}
					
					
					Button button = new Button(x, y, x + width, y + height, action, text, bmp);
					buttonCache.put(getAttribute(root.item(i), "src"), button);
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

					textCache.put(getAttribute(root.item(i), "src"), txt);
				}
			}
		}
		
		Debug.log("getText() found '" + i + "' text(s)");
		return textCache;
	}
	
	public Hashtable<String, Image> getImages(Node screen){
		
		NodeList root = screen.getChildNodes();
		
		Hashtable<String, Image> imageCache = new Hashtable<String, Image>();
		
		int i;
		for ( i=0 ; i<root.getLength() ; i++ ){
			if ( root.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( root.item(i).getNodeName().equals("image") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					float width = Float.parseFloat(getAttribute(root.item(i), "width"));
					float height = Float.parseFloat(getAttribute(root.item(i), "height"));
					Bitmap bmp = null;
					try {
						bmp = BitmapFactory.decodeStream(assets_.open(getAttribute(root.item(i), "img")));
					} catch  ( IOException e ) {}
					
					
					Image img = new Image(x, y, width, height, bmp);
					imageCache.put(getAttribute(root.item(i), "src"), img);
				}
			}
		}
		

		Debug.log("getImages() found '" + i + "' image(s)");
		return imageCache;
	}
	
	private void prettyPrint(Node node, String padding){
		
		Debug.log("+" + padding + node.getNodeName());
		NodeList z = node.getChildNodes();
		
		for ( int i=0 ; i<z.getLength() ; i++ ){
			prettyPrint(z.item(i), padding + "----");
		}
		
	}

}

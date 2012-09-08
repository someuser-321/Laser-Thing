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
	
	private Hashtable<Integer, Image> imageCache = new Hashtable<Integer, Image>();
	private Hashtable<Integer, Button> buttonCache = new Hashtable<Integer, Button>();
	private Hashtable<Integer, Text> textCache = new Hashtable<Integer, Text>();
	private Hashtable<Integer, Obstacle> obstacleCache = new Hashtable<Integer, Obstacle>();
	
	
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
					
					if ( screen.getNodeType() != Node.TEXT_NODE){
						
						String name = getAttribute(screen, "name");
						String type = getAttribute(screen, "type");
						Debug.log("screen name = " + name, 0);
						Debug.log("screen type = " + name, 0);

						imageCache.clear();
						buttonCache.clear();
						textCache.clear();
						obstacleCache.clear();
						
						getElements(screen);

						Image[] images_ = new Image[imageCache.size()];
						Button[] buttons_ = new Button[buttonCache.size()];
						Text[] text_ = new Text[textCache.size()];
						Obstacle[] obstacles_ = new Obstacle[obstacleCache.size()];
						
						
						int j = 0;
						for (  int key : imageCache.keySet() ){
							images_[j] = imageCache.get(key);
							j++;
						}
						
						j = 0;
						for ( int key : buttonCache.keySet() ){
							buttons_[j] = buttonCache.get(key);
							j++;
						}

						j = 0;
						for ( int key : textCache.keySet() ){
							text_[j] = textCache.get(key);
							j++;
						}

						j = 0;
						for ( int key : obstacleCache.keySet() ){
							obstacles_[j] = obstacleCache.get(key);
							j++;
						}
						
						
						String bgname = getAttribute(screen, "bg");
						Debug.log("bg = " + bgname, 0);
						Bitmap background = BitmapFactory.decodeStream(assets_.open(bgname));

						Obstacle source = getSource(screen);
					
						
						screenCache.put(name, new Screen(buttons_, text_, images_, obstacles_, source, background, name, type));
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
	
	public void getElements(Node screen){
		
		NodeList root = screen.getChildNodes();
		
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
					buttonCache.put(i, button);
					
				} else if ( root.item(i).getNodeName().equals("text") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					
					int r = Integer.parseInt(getAttribute(root.item(i), "r"));
					int g = Integer.parseInt(getAttribute(root.item(i), "g"));
					int b = Integer.parseInt(getAttribute(root.item(i), "b"));
					
					String align = getAttribute(root.item(i), "align");
					int size = Integer.parseInt(getAttribute(root.item(i), "size"));
					
					String text = root.item(i).getFirstChild().getNodeValue();


					Text txt = new Text(x, y, text, size, align, r, g, b, assets_);

					textCache.put(i, txt);
					
				} else if ( root.item(i).getNodeName().equals("image") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					int width = Integer.parseInt(getAttribute(root.item(i), "width"));
					int height = Integer.parseInt(getAttribute(root.item(i), "height"));
					
					String src = getAttribute(root.item(i), "img");
					
					if ( !bitmapCache.containsKey(src) ){
						bitmapCache.put(String.valueOf(src), getBitmap(src, width, height));
					}
					
					Image img = new Image(x, y, width, height, bitmapCache.get(src));
					
					imageCache.put(i, img);

				} else if ( root.item(i).getNodeName().equals("object") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					float width = Integer.parseInt(getAttribute(root.item(i), "width"));
					float height = Integer.parseInt(getAttribute(root.item(i), "height"));
					float rotation = Float.parseFloat(getAttribute(root.item(i), "rotation"));
					
					String src = getAttribute(root.item(i), "img");
					
					if ( !bitmapCache.containsKey(src) ){
						bitmapCache.put(String.valueOf(src), getBitmap(src, width, height));
					}
					
					Bitmap bmp = bitmapCache.get(src);
					
					String type = getAttribute(root.item(i), "type");
					
					Obstacle obs = new Obstacle(type, x, y, width, height, rotation, bmp);
					
					obstacleCache.put(i, obs);

				}
			}
		}
		
		
	}
	
	private Obstacle getSource(Node screen){
		
		NodeList root = screen.getChildNodes();
		Obstacle ret = null;
		
		int i;
		for ( i=0 ; i<root.getLength() ; i++ ){
			if ( root.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( root.item(i).getNodeName().equals("source") ){
					
					float x = Float.parseFloat(getAttribute(root.item(i), "x"));
					float y = Float.parseFloat(getAttribute(root.item(i), "y"));
					
					String src = getAttribute(root.item(i), "img");
					String colour = getAttribute(root.item(i), "colour");
					
					if ( !bitmapCache.containsKey(src) ){
						bitmapCache.put(String.valueOf(src), getBitmap(src, width, height));
					}
					
					Bitmap bmp = bitmapCache.get(src);
					
					ret = new Obstacle(x, y, colour, bmp);
					break;
				}
			}
		}
		
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

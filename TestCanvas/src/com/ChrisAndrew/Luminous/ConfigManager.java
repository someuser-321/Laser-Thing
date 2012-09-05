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
	
	public float width, height;
	
	public Hashtable<String, Image> images = new Hashtable<String, Image>();
	public Hashtable<String, Button> buttons = new Hashtable<String, Button>();

	
	public ConfigManager(AssetManager assets){
		
		try {
			
			assets_ = assets;
			file = assets_.open(configfile);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);

			screens = doc.getElementsByTagName("screens").item(0).getChildNodes();
			getImages(doc.getElementsByTagName("images"));
			width = 1280;
			height = 752;
			
			if ( screens == null ){
				Debug.log("screens = null");
			} else {
				Debug.log("screen width = '" + width + "'");
				Debug.log("screen height = '" + height + "'");
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
	
	public Node getButtons(Node screen){
		
		NodeList nodes = screen.getChildNodes();
		int l = nodes.getLength();
		Node ret = screen.cloneNode(false);
		
		for ( int i=0 ; i<l ; i++ ){
			if ( nodes.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( nodes.item(i).getNodeName().equals("button") ){
					ret.appendChild(nodes.item(i));
				}
			}
		}
		
		if ( ret == null )
			Debug.log("getButtons() ret = null");
		else
			Debug.log("getButtons() returned a node of length '" + ret.getChildNodes().getLength() + "' from parent node of length '" + l + "'");
		
		return ret;
	}
	
	public Node getText(Node screen){
		
		NodeList nodes = screen.getChildNodes();
		int l = nodes.getLength();
		Node ret = screen.cloneNode(false);
		
		for ( int i=0 ; i<l ; i++ ){
			if ( nodes.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( nodes.item(i).getNodeName().equals("text") ){
					ret.appendChild(nodes.item(i));
				}
			}
		}
		
		if ( ret == null )
			Debug.log("getText() ret = null");
		else
			Debug.log("getText() returned a node of length '" + ret.getChildNodes().getLength() + "' from parent node of length '" + l + "'");
		
		return ret;
	}
	
	public void getImages(NodeList root){
		
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
						bmp = BitmapFactory.decodeStream(assets_.open(getAttribute(root.item(i), "y")));
					} catch  ( IOException e ) {}
					
					Image img = new Image(x, y, width, height, bmp);
					images.put(getAttribute(root.item(i), "src"), img);
				}
			}
		}
		

		Debug.log("getImages() found '" + i + "' images");
		
	}
	
	public Image getImage(String image){
		
		if ( images.containsKey(image) ){
			return images.get(image);
		} else {
			Debug.log("Error retrieving image from cache");
			return null;
		}
		
	}
	
	private void prettyPrint(Node node, String padding){
		
		Debug.log("+" + padding + node.getNodeName());
		NodeList z = node.getChildNodes();
		
		for ( int i=0 ; i<z.getLength() ; i++ ){
			prettyPrint(z.item(i), padding + "----");
		}
		
	}

}

package com.ChrisAndrew.Luminous;


import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ConfigManager {

	private String configfile = "config/config.xml";
	
	private Document doc;

	
	public ConfigManager(GameView view){
		
		AssetManager assets = view.context.getAssets();

		try {
			
			InputStream input = assets.open(configfile);
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = db.parse(input);
			
		} catch (ParserConfigurationException e) { System.out.println("ParserConfigurationException");
		} catch (SAXException e) {System.out.println("SAXException");
		} catch (IOException e) {System.out.println("IOException");
		}
	
	}
	
	public Node getScreen(String name){

		NodeList screens = doc.getElementsByTagName("screens").item(0).getChildNodes();
		Node ret = null;
		
		for ( int i=0 ; i<screens.getLength() ; i++ ){
			if ( screens.item(i) != null ){
				if ( getNodeAttribute("name", screens.item(i)).equals(name) ){
					//System.out.println(getNodeAttribute("name", screens.item(i)));
					ret = screens.item(i);
				}
			}
		}
		
		//if ( ret == null )
		//	System.out.println("screen is null");
		//else
		//	System.out.println("screen is not null");
		return ret;
	}
	
	public String getNodeAttribute(String name, Node node){
		
		String ret = "poop";
		NamedNodeMap attributes = node.getAttributes();

		if ( attributes != null ){
			for ( int i=0; i<attributes.getLength(); i++ ){
				Node attribute = attributes.item(i);
				//System.out.println(name + ": " + attribute.getNodeName() + " = " + attribute.getNodeValue());
				if ( attribute.getNodeName().equals(name) ){
					ret = attribute.getNodeValue();
				}
			}
		}
		
		//if ( ret != null )
		//	System.out.println(ret);
		return ret;
	}
	
	public Node getButtonNodes(Node screen_){
		
		NodeList screen = screen_.getChildNodes();
		Element root = doc.createElement("buttons");
		
		for ( int i=0 ; i<screen.getLength() ; i++ ){
			if ( screen.item(i) != null ){
				if ( screen.item(i).getNodeName().equals("button") ){
					System.out.println("button");
					root.appendChild(screen.item(i));
				}
			}
		}

		
		return root;
	}
	
	public Node getTextNodes(Node screen_){
		
		NodeList screen = screen_.getChildNodes();
		Element root = doc.createElement("buttons");
		
		for ( int i=0 ; i<screen.getLength() ; i++ ){
			Element e = (Element)screen.item(i);
			if ( e.getTagName() == "text" ){
				root.appendChild(e);
			}
		}
		
		return root;
	}

}

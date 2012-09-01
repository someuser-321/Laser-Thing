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
	
	public Bitmap bmp_button;
	public Bitmap bmp_button_;
	public Bitmap bmp_mirror;
	public Bitmap bmp_lightbulb;
	public Bitmap bmp_prism;
	public Bitmap bmp_rope;
	
	private Document doc;

	
	public ConfigManager(GameView view){
		
		AssetManager assets = view.context.getAssets();

		try {
			
			InputStream input = assets.open(configfile);
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			
			try {
				db = dbf.newDocumentBuilder();
				doc = db.parse(input);
			} catch (ParserConfigurationException e) {
			} catch (SAXException e) {}
			
			bmp_button = BitmapFactory.decodeStream(assets.open("images/button.png"));
			bmp_button_ = BitmapFactory.decodeStream(assets.open("images/button_.png"));
			bmp_mirror = BitmapFactory.decodeStream(assets.open("images/mirror.png"));
			bmp_lightbulb = BitmapFactory.decodeStream(assets.open("images/lightbulb.png"));
			bmp_prism = BitmapFactory.decodeStream(assets.open("images/prism.png"));
			bmp_rope = BitmapFactory.decodeStream(assets.open("images/rope.png"));
			
		} catch (IOException e) {
		}
	
	}
	
	public NodeList getScreens(){
		
		NodeList nodes = doc.getElementsByTagName("screens");
		
		return nodes;
	}
	
	public Node getTextNodes(NodeList screen){
		
		Node root = screen.item(0);
		
		for ( int i=0 ; i<screen.getLength() ; i++ ){
			Element e = (Element)screen.item(i);
			if ( e.getTagName() == "text" ){
				root.appendChild(e);
			}
		}
		
		return root;
	}
	
	public Node getButtonNodes(NodeList screen){
		
		Node root = screen.item(0);
		
		for ( int i=0 ; i<screen.getLength() ; i++ ){
			Element e = (Element)screen.item(i);
			if ( e.getTagName() == "button" ){
				root.appendChild(e);
			}
		}
		
		return root;
	}
	
	public String getNodeAttribute(String name, Node node){
		
		String ret = null;
		NamedNodeMap attributes = node.getAttributes();
		
		for (int i = 0; i < attributes.getLength(); i++ ) {
			Node attribute = attributes.item(i);
			if ( attribute.getNodeName() == name ) {
					ret = attribute.getNodeValue();
			}
		}
		
		return ret;
	}
	
}

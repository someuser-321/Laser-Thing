package com.ChrisAndrew.Luminous;


import java.io.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;


public class ConfigManager {

	private String configfile = "config/config.xml";
	
	private NodeList screens;
	
	
	public ConfigManager(Context context){
		
		try {
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(context.getAssets().open(configfile));		
			screens = doc.getElementsByTagName("screens");
			
		} catch ( IOException e ){
			System.out.println("IOException: Unable to open config file");
		} catch ( ParserConfigurationException e ){
			System.out.println("ParserConfigurationException: Unable to build document");
		} catch ( SAXException e ){
			System.out.println("SAXException: Unable to parse config file");
		}

		
	}

	public Node getScreen(String name){
		
		Node ret = null;

		for ( int i=0 ; i<screens.getLength() ; i++ ){
			if ( screens.item(i).getNodeName().equals(name) ){
				ret = screens.item(i);
			}
		}
		
		return ret;
	}
	
	public Node getButtons(Node screen){
		
		NodeList out = screen.getChildNodes();
		Node ret = screen.cloneNode(false);
		
		for ( int i=0 ; i<out.getLength() ; i++ ){
			if ( out.item(i).getNodeName().equals("button") ){
				ret.appendChild(out.item(i));
			}
		}
		
		return ret;
	}
	
	public Node getText(Node screen){
		
		NodeList out = screen.getChildNodes();
		Node ret = screen.cloneNode(false);
		
		for ( int i=0 ; i<out.getLength() ; i++ ){
			if ( out.item(i).getNodeName().equals("text") ){
				ret.appendChild(out.item(i));
			}
		}
		
		return ret;
	}
	
	public String getAttirbute(Node node, String attr){
		
		String ret = "0";
		
		NamedNodeMap attributes = node.getAttributes();
		
		for ( int i=0 ; i<attributes.getLength() ; i++ ){
			if ( attributes.item(i).getNodeName().equals(attr) ){
				ret = attributes.item(i).getNodeValue();
			}
		}
		
		return ret;
	}

}

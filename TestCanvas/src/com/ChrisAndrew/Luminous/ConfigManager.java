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

import android.content.res.AssetManager;


public class ConfigManager {

	private String configfile = "config/config.xml";
	private InputStream file;
	
	private NodeList screens;
	
	public float width, height;
	
	
	public ConfigManager(AssetManager assets){
		
		try {
			
			file = assets.open(configfile);
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
		
		for ( int i=0 ; i<nodes.getLength() ; i++ ){
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
		
		for ( int i=0 ; i<nodes.getLength() ; i++ ){
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
	
	private void prettyPrint(Node node, String padding){
		
		Debug.log("+" + padding + node.getNodeName());
		NodeList z = node.getChildNodes();
		
		for ( int i=0 ; i<z.getLength() ; i++ ){
			prettyPrint(z.item(i), padding + "----");
		}
		
	}

}

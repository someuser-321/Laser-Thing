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

			screens = doc.getElementsByTagName("screens").item(0).getChildNodes();
			if ( screens == null )
				System.out.println("screens = null");
			
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
			if ( screens.item(i).getNodeType() != Node.TEXT_NODE ){
				if ( getAttribute(screens.item(i), "name").equals(name) ){
					ret = screens.item(i);
					break;
				}
			}
		}
		
		if ( ret == null )
			System.out.println("getScreen() ret = null");
		else
			System.out.println("getScreen() returned a node of length '" + ret.getChildNodes().getLength() + "'");
		
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
			System.out.println("getAttribute(" + attr + ") ret = null");
		else
			System.out.println("getAttribute(" + attr + ") returned '" + ret + "'");
		
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
			System.out.println("getButtons() ret = null");
		else
			System.out.println("getButtons() returned a node of length '" + ret.getChildNodes().getLength() + "' from parent node of length '" + l + "'");
		
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
			System.out.println("getText() ret = null");
		else
			System.out.println("getText() returned a node of length '" + ret.getChildNodes().getLength() + "' from parent node of length '" + l + "'");
		
		return ret;
	}
	
	private void prettyPrint(Node node, String padding){
		
		System.out.println("+" + padding + node.getNodeName());
		NodeList z = node.getChildNodes();
		
		for ( int i=0 ; i<z.getLength() ; i++ ){
			prettyPrint(z.item(i), padding + "----");
		}
		
	}

}

package com.cpsc310.team_name.parkfinder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.cpsc310.team_name.parkfinder.client.Park;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

public class ParksListingParser {
	
	// URL of data: ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml
	
	ArrayList parks = new ArrayList<Park>();
	
	public ParksListingParser() {
		
	}
	
	public void parse() {
		// TODO: implement this method using helper functions below
	}
	
	
	/** 
	 * Called to return the Parks Listing data from the CoV server from URL:
	 * ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml
	 * 
	 * @return a String representing the XML file from the City of Vancouver Data Catalogue
	 */
	
	private String downloadXMLString() {
		String file = new String();
		StringBuffer tempFile = new StringBuffer();
		
		tempFile.setLength(0);  // initialise the StringBuffer

		try {
			URL url = new URL("ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			String individualLine;

			while ((individualLine = reader.readLine()) != null) {  // while non-empty lines exist:
			 tempFile.append(individualLine);  // read each line and add it to tempFile
			}
			
			reader.close();

		} catch (MalformedURLException e) {
			// Do nothing
		} catch (IOException e) {
			// Do nothing
		}
		
		file = tempFile.toString();
		return file;
	}
	
	/**
	 * Called to parse an XML String and create individual Park instances with
	 * the data
	 * 
	 * @param file
	 *            The XML file to be parsed
	 */
	private void parseXML(String file) {
		try {
			
			// Convert XML String to DOM
			Document fileDom = XMLParser.parse(file);
			
			// Collect required data and store as strings
			
			// parkId
			Node idNode = fileDom.getElementsByTagName("Park").item(0);
			String parkId = ((Element)idNode).getAttribute("ID");
			
			// TODO: complete this method
		
		
		
		
		
		} catch (DOMException e) {
			Window.alert("Could not parse XML document");
		}
		
		
	}
	

}

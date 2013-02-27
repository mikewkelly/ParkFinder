package com.cpsc310.team_name.parkfinder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.cpsc310.team_name.parkfinder.client.Facility;
import com.cpsc310.team_name.parkfinder.client.LatLong;
import com.cpsc310.team_name.parkfinder.client.Park;
import com.cpsc310.team_name.parkfinder.client.ParkFacilities;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class ParksListingParser {

	// URL of data: ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml

	private ArrayList<Park> initialParks = new ArrayList<Park>();
	private ArrayList<Park> updatedParks = new ArrayList<Park>();

	public ParksListingParser() {
	}

	public ArrayList<Park> parse() {
		String xmlString = downloadXMLString();
		parseXML(xmlString);
		// pass value of initialParks to other parser and assign return value to updatedParks
		return updatedParks;
	}

	/**
	 * Called to return the Parks Listing data from the CoV server from URL:
	 * ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml
	 * 
	 * @return a String representing the XML file from the City of Vancouver
	 *         Data Catalogue
	 */

	private String downloadXMLString() {
		String file = new String();
		StringBuffer tempFile = new StringBuffer();

		tempFile.setLength(0); // initialise the StringBuffer

		try {
			URL url = new URL(
					"ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String individualLine;

			while ((individualLine = reader.readLine()) != null) { // while
																	// non-empty
																	// lines
																	// exist:
				tempFile.append(individualLine); // read each line and add it to
													// tempFile
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
	 * @param file The XML file to be parsed
	 *            
	 */
	private void parseXML(String file) {

		try {
			// Convert XML String to DOM
			Document fileDom = XMLParser.parse(file);

			Element element = fileDom.getDocumentElement();
			NodeList parkNodeList = element.getElementsByTagName("Park");
			final int parkCount = parkNodeList.getLength();		

			// Initialize the StringBuffers used to hold data needed for construction of Park instances
			StringBuffer tempParkID = new StringBuffer();
			StringBuffer tempParkName = new StringBuffer();
			StringBuffer tempStreetName = new StringBuffer();
			StringBuffer tempStreetNumber = new StringBuffer();
			StringBuffer tempGoogleMapDest = new StringBuffer();
			StringBuffer tempNeighbourhoodName = new StringBuffer();
			
			 // iterate over every "Park" node	
			for (int i = 0; i < parkCount; i++) {	
				// Clear the contents of each StringBuffer
				tempParkID.setLength(0);
				tempParkName.setLength(0);
				tempStreetName.setLength(0);
				tempStreetNumber.setLength(0);
				tempGoogleMapDest.setLength(0);
				tempNeighbourhoodName.setLength(0);
				
				// create an ArrayList to hold the facilities for the park
				ArrayList<Facility> parkFacilities = new ArrayList<Facility>();
				parkFacilities.clear();
				
				//Get the ID of the park node
				Node parkNode = parkNodeList.item(i);
				tempParkID.append(((Element) parkNode).getAttribute("ID"));

				Element parkContents = (Element) parkNodeList.item(i); // parkContents is an individual park node's contents

				// get the name of the park
				tempParkName.append(parkContents.getElementsByTagName("Name").item(0).getFirstChild().getNodeValue());
				
				// get the street number
				tempStreetNumber.append(parkContents.getElementsByTagName("StreetNumber").item(0).getFirstChild().getNodeValue());
				
				// get the name of the street
				tempStreetName.append(parkContents.getElementsByTagName("StreetName").item(0).getFirstChild().getNodeValue());
				
				// get the GoogleMapDest
				tempGoogleMapDest.append(parkContents.getElementsByTagName("GoogleMapDest").item(0).getFirstChild().getNodeValue());
				
				// get the neighbourhood name
				tempNeighbourhoodName.append(parkContents.getElementsByTagName("NeighbourhoodName").item(0).getFirstChild().getNodeValue());

				// get the facilities data - NOTE: Parks may have 0..n facility type and 1..n facility count of each facility type

				// Get a Node list of all the facilities for this individual park
				NodeList facilityNodeList = parkContents.getElementsByTagName("Facility");
				int facilityTypeCount = facilityNodeList.getLength();
				
				// initialize the accumulators
				StringBuffer tempFacilityType = new StringBuffer();
				StringBuffer tempFacilityCount = new StringBuffer();
				
				// iterate over the facilityNodeList, collecting data on each type of facility for this individual park
				for (int j = 0; j < facilityTypeCount; j++) {
					
					// reset the accumulators
					tempFacilityType.setLength(0);
					tempFacilityCount.setLength(0);
					
					Element facilityContents = (Element) facilityNodeList.item(j); // facilityContents is an individual facility node's contents
					// get the facility type
					tempFacilityType.append(facilityContents.getElementsByTagName("FacilityType").item(0).getFirstChild().getNodeValue());
					// get the facility count
					tempFacilityCount.append(facilityContents.getElementsByTagName("FacilityCount").item(0).getFirstChild().getNodeValue());
					
					// create a new Facility instance and add it to parkFacilities
					int tempFacilityCountAsInt = Integer.parseInt(tempFacilityCount.toString());
					Facility f = new Facility(tempFacilityType.toString(), tempFacilityCountAsInt);
					parkFacilities.add(f);
				
				}
				
				// Construct a new Park instance using collected data and add it to initialParks
				
				Park p = new Park(tempParkID.toString());
				p.setName(tempParkName.toString());
				p.setStreetName(tempStreetName.toString());
				p.setStreetNumber(Integer.parseInt(tempStreetNumber.toString()));
				LatLong theLatLong = convertGMDtoLatLong(tempGoogleMapDest.toString());
				p.setGoogleMapDest(theLatLong);
				p.setNeighbourhoodName(tempNeighbourhoodName.toString());
				// Create an instance of ParkFacilities using parkFacilities and tempParkID
				ParkFacilities pf = new ParkFacilities(tempParkID.toString(), parkFacilities);
				p.setParkFacilities(pf);
				
				//add p to initialParks
				initialParks.add(p);
			}

		} catch (DOMException e) {
			Window.alert("Could not parse XML document");
		}
		
	}
	
	/**
	 * Called to convert a single-string googleMapDest to a LatLong object
	 * 
	 * @param 
	 *            
	 */
	private LatLong convertGMDtoLatLong(String googleMapDest) {
		String[] theLatAndTheLong = googleMapDest.split(",");
		String theLat = theLatAndTheLong[0];
		String theLong = theLatAndTheLong[1];
		LatLong theLatLong = new LatLong(Float.parseFloat(theLat), Float.parseFloat(theLong)); 
		return theLatLong;
		}
		
	}


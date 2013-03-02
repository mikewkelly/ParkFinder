package com.cpsc310.team_name.parkfinder.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.cpsc310.team_name.parkfinder.client.Area;
import com.cpsc310.team_name.parkfinder.client.Park;
import com.cpsc310.team_name.parkfinder.client.ParkAreas;
import com.google.gwt.user.client.Window;
import com.google.gwt.xml.client.DOMException;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public class WeekendPlayfieldStatusParser {

	// URL: ftp://webftp.vancouver.ca/opendata/xml/weekendplayfieldstatus.xml

	private ArrayList<Park> initialParks;
	private ArrayList<Area> allAreas;
	private ArrayList<Park> updatedParks;
	private ArrayList<ParkAreas> parkAreasList;
	
	public WeekendPlayfieldStatusParser(ArrayList<Park> theInitialParks) {
		initialParks = theInitialParks;
	}

	public ArrayList<Park> parse() {
		// get the XML file from CoV server
		String xmlString = downloadXMLString();
		// get an ArrayList of all the Areas from the XML
		allAreas = parseXML(xmlString);
		// get an ArrayList of ParkAreas, organized by parkId, from allAreas
		parkAreasList = organizeAreas(allAreas);
		// update initialParks to include data from ParkAreas
		updatedParks = updateParks(initialParks, parkAreasList);
		return updatedParks;
	}

	/**
	 * Called to return the Weekend Playfield Status data from the CoV server
	 * from URL:
	 * ftp://webftp.vancouver.ca/opendata/xml/weekendplayfieldstatus.xml
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
					"http://www.ugrad.cs.ubc.ca/~p8h8/weekendplayfieldstatus.xml");
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
	 * Called to parse an XML String and create ParkFacilities and ParkAreas instances with
	 * the data
	 * 
	 * @param file
	 *            The XML file to be parsed
	 */
	private ArrayList<Area> parseXML(String file) {
		
		ArrayList<Area> theAreas = new ArrayList<Area>();
		
		try {
		// Convert XML String to DOM
		Document fileDom = XMLParser.parse(file);

		Element element = fileDom.getDocumentElement();
		NodeList parkNodeList = element.getElementsByTagName("Park");
		final int parkCount = parkNodeList.getLength();
	
		// Initialize the Accumulators used to hold data needed for construction of Park instances
		StringBuffer tempParkID = new StringBuffer();
		StringBuffer tempSiteArea = new StringBuffer();
		StringBuffer tempClosureNotes = new StringBuffer();
		StringBuffer tempWeekendStatus = new StringBuffer();
		StringBuffer tempLastUpdated = new StringBuffer();
		
		// Iterate over every "Park" node
		for (int i = 0; i < parkCount; i++ ) {
			// clear the accumulators
			tempParkID.setLength(0);
			tempSiteArea.setLength(0);
			tempClosureNotes.setLength(0);
			tempWeekendStatus.setLength(0);
			tempLastUpdated.setLength(0);
			
			//Get the ID of the park
			Node parkNode = parkNodeList.item(i);
			tempParkID.append(((Element) parkNode).getAttribute("ID"));
			
			Element parkContents = (Element) parkNodeList.item(i); // parkContents is an individual park node's contents
			
			// get the site area
			tempSiteArea.append(parkContents.getElementsByTagName("SiteArea").item(0).getFirstChild().getNodeValue());

			// get the closure notes
			tempClosureNotes.append(parkContents.getElementsByTagName("ClosureNotes").item(0).getFirstChild().getNodeValue());
			
			// get the weekend status
			tempWeekendStatus.append(parkContents.getElementsByTagName("WeekendStatus").item(0).getFirstChild().getNodeValue());
			
			// get last updated
			tempLastUpdated.append(parkContents.getElementsByTagName("LastUpdated").item(0).getFirstChild().getNodeValue());
			
			// Construct a new Area instance using accumulator values
			
			Area a = new Area(tempParkID.toString(), tempSiteArea.toString());
			a.setClosureNotes(tempClosureNotes.toString());
			a.setWeekendStatus(tempWeekendStatus.toString());
			a.setLastUpdated(tempLastUpdated.toString());

			// Add the area to theAreas
			
			theAreas.add(a);
		}
		
		} catch (DOMException e) {
			Window.alert("Could not parse XML document");
		}
		
		return theAreas;

	}
	/**
	 * Called to transform an ArrayList of Areas into an ArrayList of sorted ParkAreas instances
	 * 
	 * @param theAreas an ArrayList of Areas
	 * @return ArrayList of ParkAreas, organized by parkId
	 */
	private ArrayList<ParkAreas> organizeAreas(ArrayList<Area> theAreas) {
		ArrayList<ParkAreas> parkAreasList = new ArrayList<ParkAreas>();
		ArrayList<Area> areas = theAreas;
		StringBuffer tempParkId = new StringBuffer();
		ArrayList<String> ids = new ArrayList<String>();
		
		// Populate a list of unique Park Ids
		for (int i = 0; i < areas.size(); i++ ) {
			tempParkId.setLength(0);
			tempParkId.append(areas.get(i).getParkId());
			if (!(ids.contains(tempParkId.toString()))) {
				ids.add(tempParkId.toString());
			}
		}
		// Create a new ParkAreas instance and add it to tempParkAreasList
		for (int i = 0; i < ids.size(); i++) {
			ParkAreas pa = new ParkAreas(ids.get(i));
			parkAreasList.add(pa);
		}
		// Now add each Area to its corresponding ParkArea object in the list
		// iterate over the list of areas
		for (Area a: areas) {
			// iterate over the list of ParkAreas
				//check if duplicate area is being added
			for (int i = 0; i < parkAreasList.size(); i++) {
				//if the ParkAreas and the Area have the same parkId, and the ParkAreas doesn't already contain the Area
				if ( (parkAreasList.get(i).getParkId().equals(a.getParkId())) && (!(parkAreasList.get(i).containsArea(a)) ) ) {
					// add Area to ParkArea
					parkAreasList.get(i).addArea(a);
				}
			}
		
		}

		return parkAreasList;
	}
	/**
	 * Called to update initial list of Parks with ParkAreas data
	 * 
	 * @param theParks the initial ArrayList of Park objects
	 * @param theParkAreas the ArrayList of ParkAreas objects
	 * @return an updated ArrayList<Park> of Park objects which now includes ParkAreas data
	 */
	private ArrayList<Park> updateParks(ArrayList<Park> theParks, ArrayList<ParkAreas> theParkAreas) {
		ArrayList<Park> parks = theParks;
		ArrayList<ParkAreas> parkAreas = theParkAreas;
		// iterate over the list of parks
		for (Park p: parks) {
			for (int i = 0; i < parkAreas.size(); i++ ){
				// if the parkId for the park matches the parkId for the ParkAreas object
				if (p.getParkId().equals(parkAreas.get(i).getParkId())) {
					p.setParkAreas(parkAreas.get(i));
				}
			}
		}
		return parks;
	}
	

}

package com.cpsc310.team_name.parkfinder.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import com.cpsc310.team_name.parkfinder.client.Area;
import com.cpsc310.team_name.parkfinder.client.Park;

public class WeekendPlayfieldStatusParser {

	// URL: ftp://webftp.vancouver.ca/opendata/xml/weekendplayfieldstatus.xml
	// URL of workaround: http://www.ugrad.cs.ubc.ca/~p8h8/weekendplayfieldstatus.xml
	
	private ArrayList<Area> allAreas;
	
	public WeekendPlayfieldStatusParser(){}
	
	public WeekendPlayfieldStatusParser(ArrayList<Area> theInitialAreas) {
		allAreas = theInitialAreas;
	}

	public ArrayList<Area> parse() {
		// get an ArrayList of all the Areas from the XML
		allAreas = parseXML();
		return allAreas;
	}

	/**
	 * Called to return the Weekend Playfield Status data, parse it, and produce
	 * an ArrayList of all areas described by the xml
	 * 
	 * @return an ArrayList<Area> containing all the data from the xml pertaining to areas of parks
	 */

	private ArrayList<Area> parseXML() {
		
		ArrayList<Area> theAreas = new ArrayList<Area>();
		
		try {
		
		URL url = new URL(
					"http://www.ugrad.cs.ubc.ca/~p8h8/weekendplayfieldstatus.xml");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			
		InputSource inputSource = new InputSource(reader);
			
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document fileDom = dBuilder.parse(inputSource);
		reader.close();
			

		Element element = fileDom.getDocumentElement();
		NodeList parkNodeList = element.getElementsByTagName("Park");
		final int parkCount = parkNodeList.getLength();
	
		// Initialize the Accumulators used to hold data needed for construction of Area instances
		StringBuffer tempAreaID = new StringBuffer();
		StringBuffer tempParkID = new StringBuffer();
		StringBuffer tempParkName = new StringBuffer();
		StringBuffer tempSiteArea = new StringBuffer();
		StringBuffer tempClosureNotes = new StringBuffer();
		StringBuffer tempWeekendStatus = new StringBuffer();
		StringBuffer tempLastUpdated = new StringBuffer();
		
		// Iterate over every "Park" node
		for (int i = 0; i < parkCount; i++ ) {
			// clear the accumulators
			//tempAreaID.setLength(0);
			tempParkID.setLength(0);
			tempParkName.setLength(0);
			tempSiteArea.setLength(0);
			tempClosureNotes.setLength(0);
			tempWeekendStatus.setLength(0);
			tempLastUpdated.setLength(0);
			
//			//Get the ID of the park
			Node parkNode = parkNodeList.item(i);
			
			Element parkContents = (Element) parkNodeList.item(i); // parkContents is an individual park node's contents
//			//Get the ID of the park

			String ID = parkContents.getAttribute("ID");
			// get the park name
			tempParkName.append(parkContents.getElementsByTagName("ParkName").item(0).getFirstChild().getNodeValue());
		
			// get the site area
		
			tempSiteArea.append(parkContents.getElementsByTagName("SiteArea").item(0).getFirstChild().getNodeValue());

			// get the closure notes
			try {
			tempClosureNotes.append(parkContents.getElementsByTagName("ClosureNotes").item(0).getFirstChild().getNodeValue());
			} catch (Exception e) {
				tempClosureNotes.append("N/A");
			}
			// get the weekend status
			tempWeekendStatus.append(parkContents.getElementsByTagName("WeekendStatus").item(0).getFirstChild().getNodeValue());
			
			// get last updated
			tempLastUpdated.append(parkContents.getElementsByTagName("LastUpdated").item(0).getFirstChild().getNodeValue());
			
			// assign a unique area id - format: positionInListOfAllAreas_parkID
			// example: An area belonging to park 65 which occurs 3rd in the list of all areas
			// will have an areaId of 2_65
			
			String areaID = String.valueOf(i) + "_"+ ID;
			System.out.println(areaID);
			// Construct a new Area instance using accumulator values
			
			Area a = new Area(Long.parseLong(ID), areaID,tempSiteArea.toString());
			a.setParkName(tempParkName.toString());
			a.setClosureNotes(tempClosureNotes.toString());
			a.setWeekendStatus(tempWeekendStatus.toString());
			a.setLastUpdated(tempLastUpdated.toString());

			// Add the area to theAreas
			
			theAreas.add(a);
		}
		
		} catch (Exception e) {
			System.out.println("Could not parse XML document");
		}
		
		return theAreas;

	}
	

}

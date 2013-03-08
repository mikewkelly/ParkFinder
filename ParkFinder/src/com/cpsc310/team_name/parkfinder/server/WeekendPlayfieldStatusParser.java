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
	
	private ArrayList<Park> initialParks;
	private ArrayList<Area> allAreas;
	private ArrayList<Park> updatedParks;
	private ArrayList<ArrayList<Area>> parkAreasByParkID;
	
	public WeekendPlayfieldStatusParser(ArrayList<Park> theInitialParks) {
		initialParks = theInitialParks;
	}

	public ArrayList<Park> parse() {
		// get the XML file from CoV server

		// get an ArrayList of all the Areas from the XML
		allAreas = parseXML();
		// get an ArrayList of ArrayList<Area>, organized by parkId, from allAreas
		parkAreasByParkID = organizeAreas(allAreas);
		// update initialParks to include data from ParkAreas
		updatedParks = updateParks(initialParks, parkAreasByParkID);
		return updatedParks;
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
			tempAreaID.setLength(0);
			tempParkID.setLength(0);
			tempParkName.setLength(0);
			tempSiteArea.setLength(0);
			tempClosureNotes.setLength(0);
			tempWeekendStatus.setLength(0);
			tempLastUpdated.setLength(0);
			
//			//Get the ID of the park
			Node parkNode = parkNodeList.item(i);
			tempParkID.append(((Element) parkNode).getAttribute("ID"));
			
			Element parkContents = (Element) parkNodeList.item(i); // parkContents is an individual park node's contents
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
			tempAreaID.append(Integer.toString(i) + "_" + tempParkID.toString());

			// Construct a new Area instance using accumulator values
			
			Area a = new Area(tempParkID.toString(), tempSiteArea.toString(), tempAreaID.toString());
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
	/**
	 * Called to transform an ArrayList<Area> of of all Area objects
	 * into an ArrayList of ArrayList<Area>, sorted by ParkID
	 * 
	 * @param theAreas an ArrayList of Areas
	 * @return ArrayList of ArrayList<Area>, organized by parkId
	 */
	private ArrayList<ArrayList<Area>> organizeAreas(ArrayList<Area> theAreas) {
		ArrayList<ArrayList<Area>> parkAreasByParkID = new ArrayList<ArrayList<Area>>();
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
		
		// For each unique park id, create an ArrayList<Area>
		for (String id: ids) {
			ArrayList<Area> thisParksAreas = new ArrayList<Area>();
			// iterate over the list of all areas, getting and removing areas with
			// park IDs corresponding to the current park ID in ids
			for (Area a: areas) {
				if (a.getParkId().equals(id)) {
					thisParksAreas.add(a);
				}
			}
			//add the ArrayList<Area> for this park to parkAreasByParkID
			parkAreasByParkID.add(thisParksAreas);
		}
	
		return parkAreasByParkID;
	}
	/**
	 * Called to update initial list of Parks with ParkAreas data
	 * 
	 * @param theParks the initial ArrayList of Park objects
	 * @param theParkAreas the ArrayList of ArrayList<Area>, organized by ParkID
	 * @return an updated ArrayList<Park> of Park objects which now includes ParkAreas data
	 */
	
	private ArrayList<Park> updateParks(ArrayList<Park> theParks, ArrayList<ArrayList<Area>> theAreas) {
		ArrayList<Park> parks = theParks;
		ArrayList<ArrayList<Area>> parkAreasList = theAreas;
		// initialize the accumulators
		StringBuffer tempParkID = new StringBuffer();
		StringBuffer tempParkName = new StringBuffer();
		tempParkID.setLength(0);
		tempParkName.setLength(0);	
		
		// iterate over the list of park Areas
		for (ArrayList<Area> aParksAreas: parkAreasList) {
			// get the ParkID for all the Areas in this ArrayList
			tempParkID.append(aParksAreas.get(0).getParkId());
			// get the ParkName for all the Areas in this ArrayList
			tempParkName.append(aParksAreas.get(0).getParkName());
			// iterate over the list of Parks
			for (Park p: parks) {
				// check if the park name AND park id match for the park and the area (this will prevent id/name mismatch problem)
				if ((p.getParkId().equals(tempParkID.toString())) && (p.getName().toUpperCase().equals(tempParkName.toString().toUpperCase())) ){
					p.setParkAreas(aParksAreas);
					break; // Exit the loop because Park IDs are unique, therefore the rest of the list can be ignored
				}
			}
			// reset the accumulators
			tempParkID.setLength(0);
			tempParkName.setLength(0);
				
		}
		return theParks;
	}

	

}

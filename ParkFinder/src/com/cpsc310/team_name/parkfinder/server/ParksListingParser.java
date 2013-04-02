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
import com.cpsc310.team_name.parkfinder.client.Facility;
import com.cpsc310.team_name.parkfinder.client.Park;

public class ParksListingParser {

	// URL of data: ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml
	// URL of data (workaround): http://www.ugrad.cs.ubc.ca/~p8h8/parks_facilities.xml
	
	private ArrayList<Park> initialParks = new ArrayList<Park>();
	private ArrayList<Facility> initialFacility = new ArrayList<Facility>();
	private ArrayList<String> facilities = new ArrayList<String>();
	
	public ParksListingParser() {

	}
	// call parse XML file and get the parks
	public ArrayList<Park> getPark() {
		parseXML();
		return initialParks;
	}
	//call parse XML file and get facilities
	public ArrayList<Facility> getFacility(){
		parseXML();
		return initialFacility;
	}
	
	/**
	 * Called to parse a remote XML file and create individual Park instances with
	 * the data
	 * 
	 * @param file The XML file to be parsed
	 *            
	 */
	private void parseXML() {

		ArrayList<Park> tempInitialParks = new ArrayList<Park>();

		try {

			URL url = new URL(
					"http://www.ugrad.cs.ubc.ca/~p8h8/parks_facilities.xml");
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

			// Initialize the Accumulators used to hold data needed for construction of Park instances
			StringBuffer tempParkID = new StringBuffer();
			StringBuffer tempParkName = new StringBuffer();
			StringBuffer tempStreetName = new StringBuffer();
			StringBuffer tempStreetNumber = new StringBuffer();
			StringBuffer tempGoogleMapDest = new StringBuffer();
			StringBuffer tempNeighbourhoodName = new StringBuffer();
			StringBuffer tempFacilities = new StringBuffer();
			StringBuffer tempFacilityCount = new StringBuffer();


			 // iterate over every "Park" node	
			for (int i = 0; i < parkCount; i++) {	
				// Clear the contents of each Accumulator
				tempParkID.setLength(0);
				tempParkName.setLength(0);
				tempStreetName.setLength(0);
				tempStreetNumber.setLength(0);
				tempGoogleMapDest.setLength(0);
				tempNeighbourhoodName.setLength(0);
				tempFacilities.setLength(0);
				tempFacilityCount.setLength(0);


				//Get the ID of the park node
				Node parkNode = parkNodeList.item(i);
				String parkID = tempParkID.append(((Element) parkNode).getAttribute("ID")).toString();
				Element parkContents = (Element) parkNodeList.item(i); // parkContents is an individual park node's contents

				// get the name of the park
				tempParkName.append(parkContents.getElementsByTagName("Name").item(0).getFirstChild().getNodeValue());

				// get the street number
				
				try {
					tempStreetNumber.append(parkContents.getElementsByTagName("StreetNumber").item(0).getFirstChild().getNodeValue());			
				} catch (Exception e) {
					tempStreetNumber.append("N/A ");
				}
				
				// get the name of the street
				tempStreetName.append(parkContents.getElementsByTagName("StreetName").item(0).getFirstChild().getNodeValue());

				// get the GoogleMapDest
				tempGoogleMapDest.append(parkContents.getElementsByTagName("GoogleMapDest").item(0).getFirstChild().getNodeValue());

				// get the neighbourhood name
				tempNeighbourhoodName.append(parkContents.getElementsByTagName("NeighbourhoodName").item(0).getFirstChild().getNodeValue());
				
				// Get a Node list of all the facilities for this individual park
				NodeList facilityNodeList = parkContents.getElementsByTagName("Facility");
				int facilityTypeCount = facilityNodeList.getLength();
				// initialize the accumulators
				StringBuffer tempFacilityType = new StringBuffer();
				StringBuffer tempFacilityCount1 = new StringBuffer();
				StringBuffer tempFacilityID = new StringBuffer();

				try {
					// iterate over the facilityNodeList, collecting data on each type of facility for this individual park

					for (int j = 0; j < facilityTypeCount; j++) {

						// reset the accumulators
						tempFacilityType.setLength(0);
						tempFacilityCount1.setLength(0);
						tempFacilityID.setLength(0);

						Element facilityContents = (Element) facilityNodeList.item(j); // facilityContents is an individual facility node's contents
						// get the facility type
						String type = tempFacilityType.append(facilityContents.getElementsByTagName("FacilityType").item(0).getFirstChild().getNodeValue()).toString();
						String count =tempFacilityCount1.append(facilityContents.getElementsByTagName("FacilityCount").item(0).getFirstChild().getNodeValue()).toString();
						// assign a new facility id in the format: parkId_type
						// example the baseball diamond  facility for a park with an id of 20 will have a facility id 20_baseballdiamond
						String FacilityID = parkID + "_" + type;
						
						// create a new Facility instance and add it to parkFacilities
						//TODO make type with count
						Facility f = new Facility(Long.parseLong(tempParkID.toString()), type, FacilityID, count);
						initialFacility.add(f);
					}

				} catch (Exception e) {
				}
			
				// Construct a new Park instance using collected data and add it to initialParks

				Park p = new Park(Long.parseLong(tempParkID.toString()));
				p.setName(tempParkName.toString());
				p.setStreetName(tempStreetName.toString());
				p.setStreetNumber(tempStreetNumber.toString());
				p.setGoogleMapDest(tempGoogleMapDest.toString());
				p.setNeighbourhoodName(tempNeighbourhoodName.toString());

				//add p to initialParks
				initialParks.add(p);
				
			}
			

		} catch (Exception e) {
			System.out.println("Could not parse XML document: "+e.getMessage());
		} 

	}

	}
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

//import com.cpsc310.team_name.parkfinder.client.LatLong;


public class ParksListingParser {

	// URL of data: ftp://webftp.vancouver.ca/opendata/xml/parks_facilities.xml
	// URL of data (workaround): http://www.ugrad.cs.ubc.ca/~p8h8/parks_facilities.xml
	
	private ArrayList<Park> initialParks = new ArrayList<Park>();

	public ParksListingParser() {

	}

	public ArrayList<Park> parse() {
		initialParks = parseXML();
		return initialParks;
	}

	/**
	 * Called to parse a remote XML file and create individual Park instances with
	 * the data
	 * 
	 * @param file The XML file to be parsed
	 *            
	 */
	private ArrayList<Park> parseXML() {

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
				
				try {
					tempStreetNumber.append(parkContents.getElementsByTagName("StreetNumber").item(0).getFirstChild().getNodeValue());			
				} catch (Exception e) {
					System.out.println("Street Number Does not Exist For This Park:" + tempParkID.toString());
					tempStreetNumber.append("N/A");
				}
				
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
				StringBuffer tempFacilityCount1 = new StringBuffer();

				try {
					// iterate over the facilityNodeList, collecting data on each type of facility for this individual park
					
					for (int j = 0; j < facilityTypeCount; j++) {
	
						// reset the accumulators
						tempFacilityType.setLength(0);
						tempFacilityCount1.setLength(0);
	
						Element facilityContents = (Element) facilityNodeList.item(j); // facilityContents is an individual facility node's contents
						// get the facility type
						tempFacilityType.append(facilityContents.getElementsByTagName("FacilityType").item(0).getFirstChild().getNodeValue());
						// get the facility count
						tempFacilityCount1.append(facilityContents.getElementsByTagName("FacilityCount").item(0).getFirstChild().getNodeValue());
								
						// create a new Facility instance and add it to parkFacilities
						int tempFacilityCountAsInt = Integer.parseInt(tempFacilityCount1.toString());
						Facility f = new Facility(tempParkID.toString(), tempFacilityType.toString(), tempFacilityCountAsInt);
						parkFacilities.add(f);
					}
					
				} catch (Exception e) {
					parkFacilities.clear(); // for Parks with no facilities
				}
						
				// Construct a new Park instance using collected data and add it to initialParks

				Park p = new Park(tempParkID.toString());
				p.setName(tempParkName.toString());
				p.setStreetName(tempStreetName.toString());
				p.setStreetNumber(tempStreetNumber.toString());
				p.setGoogleMapDest(tempGoogleMapDest.toString());
				p.setNeighbourhoodName(tempNeighbourhoodName.toString());
				p.setParkFacilities(parkFacilities);

				//add p to initialParks
				tempInitialParks.add(p);
				
				//FOR TESTING:
//				System.out.println("Park ID: " + tempParkID.toString());
//				for (Facility f: parkFacilities) {
//					System.out.println(f.getFacilityType());
//					System.out.println(f.getFacilityCount());
//				}

			}

		} catch (Exception e) {
			System.out.println("Could not parse XML document: "+e.getMessage());
		} 

		return tempInitialParks;	
	}

	/**
	 * Called to convert a single-string googleMapDest to a LatLong object
	 * 
	 * @param 
	 *            
	 */
	/*private LatLong convertGMDtoLatLong(String googleMapDest) {
		String[] theLatAndTheLong = googleMapDest.split(",");
		String theLat = theLatAndTheLong[0];
		String theLong = theLatAndTheLong[1];
		LatLong theLatLong = new LatLong(Float.parseFloat(theLat), Float.parseFloat(theLong)); 
		return theLatLong;
		}*/

	}
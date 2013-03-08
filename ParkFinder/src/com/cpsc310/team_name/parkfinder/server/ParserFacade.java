package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;
import com.cpsc310.team_name.parkfinder.client.Park;
import com.cpsc310.team_name.parkfinder.client.Area;
import com.cpsc310.team_name.parkfinder.client.Facility;


public class ParserFacade {
	
	private ParksListingParser plParser;
	private WeekendPlayfieldStatusParser wpfsParser;
	private ArrayList<Park> initialParks;
	private ArrayList<Park> updatedParks;
	
	public ParserFacade() {
		
	}
	/**Called to generate an ArrayList of complete Park objects based upon
	 * data collected from CoV databases
	 * 
	 * @return ArrayList of Park objects
	 */
	public ArrayList<Park> parse() {
		// Create a new PL parser to get an ArrayList of Park objects, 
		// based upon CoV Park Listing data
		plParser = new ParksListingParser();
		initialParks = plParser.parse();
		
		// Create a new WPFS Parser with initial park data to get an updated 
		// ArrayList of Park objects, which includes data concerning ParkAreas
		wpfsParser = new WeekendPlayfieldStatusParser(initialParks);
		updatedParks = wpfsParser.parse();
		
		// For testing:
	//	testByDisplay(updatedParks);
		
		return updatedParks;
	}
	
	public void testByDisplay(ArrayList<Park> theParks) {
		System.out.println("There are " + theParks.size() + " individual Parks");
		for (Park p: theParks) {
			System.out.println("****************NEW PARK*************");
			System.out.println("Park ID:" + p.getParkId());
			System.out.println("Name: " + p.getName());
			System.out.println("Street Number: " + p.getStreetNumber());
			System.out.println("Street Name: " + p.getStreetName());
			System.out.println("GoogleMapDest: " + p.getGoogleMapDest());
			System.out.println("Neighbourhood Name: " + p.getNeighbourhoodName());
			System.out.println();
			System.out.println(p.getName() + "'s facilities: ");
			System.out.println();
			if (p.getParkFacilities().size() == 0) {
				System.out.println("This Park has no specific facilities");
			} else {
				for (Facility f: p.getParkFacilities()) {
					System.out.println();
					System.out.println("###### NEW FACILITY ######");
					System.out.println("Facility Type: " + f.getFacilityType());
					System.out.println("Facility Count: " + f.getFacilityCount());
				}
			}
			
			System.out.println();
			System.out.println(p.getName() + "'s areas: ");
			System.out.println();
			if ((p.getParkAreas() == null)) {
				System.out.println("This Park has no specific areas");
			} else {
				for (Area a: p.getParkAreas()) {
					System.out.println();
					System.out.println("$$$$$ NEW AREA $$$$$");
					System.out.println("Site Area: " + a.getSiteArea());
					System.out.println("Closure Notes: " + a.getClosureNotes());
					System.out.println("Weekend Status: " + a.getWeekendStatus());
					System.out.println("Last Updated: " + a.getLastUpdated());
				}
			}
			
			System.out.println();
			
		}
	}
}


package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;

import com.cpsc310.team_name.parkfinder.client.Park;
import com.cpsc310.team_name.parkfinder.server.ParksListingParser;
import com.cpsc310.team_name.parkfinder.server.WeekendPlayfieldStatusParser;

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
		
		return updatedParks;
	}

}


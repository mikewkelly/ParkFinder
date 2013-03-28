package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;
import com.cpsc310.team_name.parkfinder.client.Park;
import com.cpsc310.team_name.parkfinder.client.Area;
import com.cpsc310.team_name.parkfinder.client.Facility;


public class ParserFacade {
	
	private ParksListingParser plParser;
	private WeekendPlayfieldStatusParser wpfsParser;
	private ArrayList<Facility> initialFacility;
	private ArrayList<Park> initialParks;
	private ArrayList<Area> initialAreas;
	//private ArrayList<Park> updatedParks;
	
	public ParserFacade() {
		
	}
	
	/**Called to generate an ArrayList of complete Park objects based upon
	 * data collected from CoV databases
	 * 
	 * @return ArrayList of Park objects
	 */
	public ArrayList<Park> getPark() {
		
		plParser = new ParksListingParser();
		initialParks = plParser.getPark();
		//total number of parks
		System.out.println("Total input parks:"+initialParks.size());
		return initialParks;
	}
	public ArrayList<Facility> getFacility()
	{	
		plParser = new ParksListingParser();
		initialFacility = plParser.getFacility();
		//for test, display total number of facility
		System.out.println("Total input facilities:"+initialFacility.size());
		return initialFacility;
	}
	
	public ArrayList<Area> getArea() {
		wpfsParser = new WeekendPlayfieldStatusParser();
		initialAreas = wpfsParser.parse();
		//total number of areas
		System.out.println("Total input areas:"+initialAreas.size());
		return initialAreas;
	}
	

}


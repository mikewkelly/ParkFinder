package com.cpsc310.team_name.parkfinder.server.test;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import com.cpsc310.team_name.parkfinder.client.Area;
import com.cpsc310.team_name.parkfinder.server.WeekendPlayfieldStatusParser;

public class TestWeekendPlayfieldStatusParser extends junit.framework.TestCase {

	protected WeekendPlayfieldStatusParser parser;
	protected ArrayList<Area> allAreas;
	protected Area area0;
	protected Area area1;
	protected Area area2;
	protected Area area3;
	protected Area area4;

	@Before
	public void runBeforeTests() {
		parser = new WeekendPlayfieldStatusParser();
		allAreas = parser.parse();

		// Create 3 Area instances using data collected manually from the City of
		// Vancouver Weekend Playfield Status dataset, in order to compare with
		// Area instances generated by the parser.

		// Adanac Park's NW summer field
		area0 = new Area((long)65, "0_65", "NW");
		area0.setParkName("Adanac Park");
		area0.setClosureNotes("Summer field");
		area0.setWeekendStatus("Closed");

		// Brockton's SW area
		area1 = new Area((long)206, "18_206", "SW");
		area1.setParkName("Brockton");
		area1.setClosureNotes("N/A");
		area1.setWeekendStatus("User discretion");

		// Jones Park's Center summer field
		area2 = new Area((long)86, "78_86", "C");
		area2.setParkName("Jones Park");
		area2.setClosureNotes("Summer use only");
		area2.setWeekendStatus("Closed");

		// A non-existant Park's area
		area3 = new Area((long)999, "818_999", "NE");
		area3.setParkName("Dude Chilling Park");
		area3.setClosureNotes("Winter use only");
		area3.setWeekendStatus("Open");

		// An Area with null values
		area4 = new Area(null, null, null);
		area4.setParkName(null);
		area4.setClosureNotes(null);
		area4.setWeekendStatus(null);
	}

	@Test
	public void testParse() {
		runBeforeTests();
		assertTrue(compareArea(area0));
		assertTrue(compareArea(area1));
		assertTrue(compareArea(area2));
		assertFalse(compareArea(area3));
		assertFalse(compareArea(area4));

	}

	private boolean compareArea(Area theAreaToCompareToParsedArea) {
		for (Area a : allAreas) {
			if (a.getParkId().equals(theAreaToCompareToParsedArea.getParkId())
					&& (a.getAreaId().equals(theAreaToCompareToParsedArea
							.getAreaId()))
					&& (a.getSiteArea().equals(theAreaToCompareToParsedArea
							.getSiteArea()))
					&& (a.getParkName().equals(theAreaToCompareToParsedArea
							.getParkName()))
					&& (a.getClosureNotes().equals(theAreaToCompareToParsedArea
							.getClosureNotes()))
					&& (a.getWeekendStatus()
							.equals(theAreaToCompareToParsedArea
									.getWeekendStatus()))) {
				return true;
			}
		}

		return false;
	}

}

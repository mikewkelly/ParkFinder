package com.cpsc310.team_name.parkfinder.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class WeekendPlayfieldStatusParser {

	// URL: ftp://webftp.vancouver.ca/opendata/xml/weekendplayfieldstatus.xml

	public WeekendPlayfieldStatusParser() {

	}

	public void parse() {
		// TODO: Implement this method using helper methods below

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
					"ftp://webftp.vancouver.ca/opendata/xml/weekendplayfieldstatus.xml");
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
	private void parseXML(String file) {
		// TODO: Complete this method
	
	}

}

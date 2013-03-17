package com.cpsc310.team_name.parkfinder.client;

import java.util.ArrayList;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParkFinder implements EntryPoint {

	// the main panel
	DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);

	// the header panel
	VerticalPanel headerPanel = new VerticalPanel();

	// the tab layout panel for the map and the table
	TabLayoutPanel tlp = new TabLayoutPanel(1.5, Unit.EM);

	// for the search panel
	private VerticalPanel searchPanel = new VerticalPanel();
	private HorizontalPanel searchPanelContents = new HorizontalPanel();
	private TextBox searchCriteriaTextBox = new TextBox();
	private Button displayAllButton = new Button("Display All");
	private Button displayAllInMapButton = new Button("Display Parks in Map");
	private Button searchButton = new Button("Search");
	private Label lastUpdateLabel = new Label();
	private Label errorMessage = new Label();
	private Label successMsg = new Label();

	// for the table
	private FlexTable parkTable = new FlexTable();
	private VerticalPanel tablePanel = new VerticalPanel();

	// for the map
	FlexTable mapParkList = new FlexTable();
	MapWidget theMap;

	// the string list to store the primary keys
	private ArrayList<String> parklist = new ArrayList<String>();
	private ArrayList<String> facilitylist = new ArrayList<String>();
	private ArrayList<String> arealist = new ArrayList<String>();

	private Button importDataButton = new Button("Import");
	// the Async server objects
	private final ParkServiceAsync parkService = GWT.create(ParkService.class);
	private final FacilityServiceAsync facilityService = GWT
			.create(FacilityService.class);
	private final AreaServiceAsync areaService = GWT.create(AreaService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		loadParkTable();

		mainPanel.addNorth(headerPanel, 100);
		mainPanel.addNorth(searchPanel, 80);
		mainPanel.add(tlp);
		RootLayoutPanel.get().add(mainPanel);

		// Async load of Map API
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				buildMap();
			}
		});

	}

	public void loadParkTable() {
		// table layout
		// parkTable.setText(0,0,"Park ID");
		parkTable.setText(0, 0, "Park Name");
		parkTable.setText(0, 1, "Neighbourhood");
		parkTable.setText(0, 2, "Address");
		// parkTable.setText(0,3,"GoogleMapDest");
		parkTable.setText(0, 3, "Facilities");
		parkTable.setText(0, 4, "Weekend Status");
		// parkTable.setText(0,6,"Area");

		parkTable.getRowFormatter().addStyleName(0, "parkListHeader");
		parkTable.addStyleName("parkList");
		parkTable.setCellPadding(10);

		// used for future implementation on searching
		searchPanelContents.add(searchCriteriaTextBox);
		searchPanelContents.add(searchButton);
		searchPanelContents.add(displayAllButton);
		searchPanelContents.add(importDataButton);
		searchPanelContents.add(displayAllInMapButton);
		searchPanelContents.addStyleName("inputTextBox");
		searchPanel.add(searchPanelContents);

		searchButton.addStyleDependentName("search");
		errorMessage.setStyleName("errorMessage");
		successMsg.setStyleName("successMessage");
		errorMessage.setVisible(false);
		successMsg.setVisible(false);

		searchPanel.add(errorMessage);
		searchPanel.add(successMsg);

		tablePanel.add(parkTable);
		tablePanel.add(lastUpdateLabel);

		searchCriteriaTextBox.setFocus(true);

		displayAllButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				displayAll();
			}
		});

		importDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				importData();
			}
		});

		displayAllInMapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				displayAllInMap();
			}
		});

	}

	private void importData() {
		// import parks to GWT server
		parkService.importParks(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				errorMessage.setText("Error: failed to import data");
				errorMessage.setVisible(true);
			}

			public void onSuccess(Void ignore) {
				successMsg.setText("Data imported successfully");
				successMsg.setVisible(true);
			}
		});
		// import facilities to GWT server
		facilityService.importFacility(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				errorMessage.setText("Error: failed to import data");
				errorMessage.setVisible(true);
			}

			public void onSuccess(Void ignore) {
				successMsg.setText("Data imported successfully");
				successMsg.setVisible(true);
			}
		});
		// import areas to GWT server
		areaService.importArea(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				errorMessage.setText("Error: failed to import data");
				errorMessage.setVisible(true);
			}

			public void onSuccess(Void ignore) {
				successMsg.setText("Data imported successfully");
				successMsg.setVisible(true);
			}
		});
	}

	private void displayAll() {
		successMsg.setText("Getting data from server...");
		successMsg.setVisible(true);

		// TODO
		// Here is what goes wrong. For some reason the parks don't have count
		// at all and the others have
		// most but not all of the data

		// here is call get all parks
		parkService.getParks(new AsyncCallback<Park[]>() {
			public void onFailure(Throwable error) {
				errorMessage
						.setText("Error: failed to receive data from server");
				errorMessage.setVisible(true);
			}

			public void onSuccess(Park[] parks) {
				successMsg.setText("Getting data from server...");
				successMsg.setVisible(true);
				int count = 0;
				for (Park p : parks) {
					if (!parklist.contains(p.getParkId())) {
						parklist.add(String.valueOf(p.getParkId()));
						// showParkInTable(p);
						count++;
						System.out.println(p.getParkId());
					}

				}
				System.out.println("parks: " + count);
				successMsg.setText("Parks Displayed: " + parklist.size());
			}
		});
		// get all facilities
		facilityService.getFacility(new AsyncCallback<Facility[]>() {
			public void onFailure(Throwable error) {
				errorMessage
						.setText("Error: failed to receive data from server");
				errorMessage.setVisible(true);
			}

			public void onSuccess(Facility[] facilities) {
				successMsg.setText("Getting data from server...");
				int count = 0;
				successMsg.setVisible(true);
				for (Facility f : facilities) {
					if (!facilitylist.contains(f.getFacilityID())) {
						facilitylist.add(String.valueOf(f.getFacilityID()));
						count++;
						System.out.println(f.getFacilityID());
					}
				}
				System.out.println("facilities: " + count);
				successMsg.setText("Facility Displayed: " + facilitylist.size());
			}
		});
		// get all areas
		areaService.getArea(new AsyncCallback<Area[]>() {
			public void onFailure(Throwable error) {
				errorMessage
						.setText("Error: failed to receive data from server");
				errorMessage.setVisible(true);
			}

			public void onSuccess(Area[] areas) {
				successMsg.setText("Getting data from server...");
				int count = 0;
				successMsg.setVisible(true);
				for (Area a : areas) {
					if (!arealist.contains(a.getAreaId())) {
						arealist.add(String.valueOf(a.getAreaId()));
						count++;
						System.out.println(a.getAreaId());
					}
				}
				System.out.println("areas: " + count);
				successMsg.setText("Area Displayed: " + arealist.size());
			}
		});
	}

	private void showParkInTable(Park park) {

		int row = parkTable.getRowCount();
		// String parkfacilities = "";
		// for(Facility s:park.getParkFacilities().getFacilities()) {
		// parkfacilities = parkfacilities + s.getFacilityType() + " ";
		// }
		// parkTable.setText(row, 0, park.getParkId());
		parkTable.setText(row, 0, park.getName());
		parkTable.setText(row, 1, park.getNeighbourhoodName());
		parkTable.setText(
				row,
				2,
				String.valueOf(park.getStreetNumber().concat(" ")
						.concat(park.getStreetName())));
		// parkTable.setText(row, 3, park.getParkFacilities());
		// parkTable.setText(row, 3, String.valueOf(park.getStreetName()));
		// parkTable.setText(row, 4,
		// String.valueOf(park.getGoogleMapDest().getLat())
		// +String.valueOf(park.getGoogleMapDest().getLong()));
		// parkTable.setText(row, 5, parkfacilities);
	}

	// The following methods have to do with the Map

	private void buildMap() {

		LatLng teaSwampPark = LatLng.newInstance(49.257091, -123.098595);
		final MapWidget map = new MapWidget(teaSwampPark, 12);

		map.setSize("100%", "100%");
		map.setGoogleBarEnabled(false);

		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		theMap = map;

		final DockLayoutPanel parkMapDock = new DockLayoutPanel(Unit.PCT);
		parkMapDock.addEast(theMap, 80);
		parkMapDock.addWest(mapParkList, 20);

		// add the DockLayoutPanel to the TabLayoutPanel
		tlp.add(parkMapDock, "Map View");

		// add the table to the tlp
		tlp.add(tablePanel, "Detailed View");

	}

	private void updateMap(Park[] parks) {

		for (Park p : parks) {
			LatLong theLatLong = convertGMDtoLatLong(p.getGoogleMapDest());
			LatLng locationPoint = LatLng.newInstance(theLatLong.getLat(),
					theLatLong.getLong());
			
			// define options for the marker
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			markerOptions.setClickable(true);
			Marker theMarker = new Marker(locationPoint, markerOptions);
			theMap.addOverlay(theMarker);
		}

		// Set up the flextable showing the park names
		mapParkList.setText(0, 0, "Park Name:");

		// add each park to the mapParkList
		for (Park p : parks) {
			showParkInMapList(p);
		}

	}

	private void displayAllInMap() {
		successMsg.setText("Getting data from server...");
		successMsg.setVisible(true);

		parkService.getParks(new AsyncCallback<Park[]>() {
			public void onFailure(Throwable error) {
				errorMessage
						.setText("Error: failed to receive data from server");
				errorMessage.setVisible(true);
			}

			public void onSuccess(Park[] parks) {
				successMsg.setText("Getting data from server...");
				successMsg.setVisible(true);
				updateMap(parks);
			}
		});

	}

	private void showParkInMapList(Park park) {

		int row = mapParkList.getRowCount();

		mapParkList.setText(row, 0, park.getName());

	}

	private LatLong convertGMDtoLatLong(String googleMapDest) {
		String[] theLatAndTheLong = googleMapDest.split(",");
		String theLat = theLatAndTheLong[0];
		String theLong = theLatAndTheLong[1];
		LatLong theLatLong = new LatLong(Double.parseDouble(theLat),
				Double.parseDouble(theLong));
		return theLatLong;
	}

}
	
	


package com.cpsc310.team_name.parkfinder.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.control.MenuMapTypeControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.core.java.util.Arrays;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParkFinder implements EntryPoint {
	
	int INITIAL_ZOOM_LEVEL = 12;

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
	private Button clearMapButton = new Button("Clear Map");
	private Button searchButton = new Button("Search");
	private Label lastUpdateLabel = new Label();
	private Label errorMessage = new Label();
	private Label successMsg = new Label();

	// for the table
	private FlexTable parkTable = new FlexTable();
	private VerticalPanel tablePanel = new VerticalPanel();

	// for the map
	CellList<String> mapParkList;
	ListDataProvider<String> dataProvider;
	ScrollPanel mapParkListScrollPanel;
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
		searchPanelContents.add(clearMapButton);
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
		
		clearMapButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				clearMapAndList();
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

	@SuppressWarnings("unchecked")
	private void buildMap() {

		LatLng teaSwampPark = LatLng.newInstance(49.257091, -123.098595);
		final MapWidget map = new MapWidget(teaSwampPark, INITIAL_ZOOM_LEVEL);

		map.setSize("100%", "100%");
		map.setGoogleBarEnabled(false);
		map.setScrollWheelZoomEnabled(true);
		map.addControl(new MapTypeControl());
		map.setCurrentMapType(MapType.getNormalMap());

		// Add some controls for the zoom level
		map.addControl(new LargeMapControl());

		theMap = map;

		final DockLayoutPanel parkMapDock = new DockLayoutPanel(Unit.PCT);
		parkMapDock.addEast(theMap, 80);
		
		mapParkList = new CellList<String>(new TextCell());
		dataProvider = new ListDataProvider<String>();
	    dataProvider.addDataDisplay(mapParkList);
	    mapParkListScrollPanel = new ScrollPanel(mapParkList);

		parkMapDock.addWest(mapParkListScrollPanel, 20);
		tlp.add(parkMapDock, "Map View");

		tlp.add(tablePanel, "Detailed View");
		

	}

	private void updateMap(Park[] parks) {
		
		// How to handle the user's selection of a specific park in the list next to the map
		final Park[] parksFinalCopy = parks;
		final SingleSelectionModel<String> selectionModel = new SingleSelectionModel<String>();
		    mapParkList.setSelectionModel(selectionModel);
		    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event) {
		        String selected = selectionModel.getSelectedObject();
		        if (selected != null) {
		        	for (Park p: parksFinalCopy) {
		        		if (p.getName().toUpperCase().equals(selected.toUpperCase())) {
		        			
		        			String streetNumber = p.getStreetNumber();
		        			String streetName = p.getStreetName();
		        			String neighbourhoodName = p.getNeighbourhoodName();
		        			String space = " ";
		        			String address;
		        			if (streetNumber.contains("N/A")) {
		        				address = streetName;
		        			} else {
		        				streetNumber = streetNumber.concat(space);
		        				address = streetNumber.concat(streetName);
		        			}
		        			
		        			LatLong theLatLong = convertGMDtoLatLong(p.getGoogleMapDest());
		        			LatLng locationPoint = LatLng.newInstance(theLatLong.getLat(), theLatLong.getLong());
		        			theMap.setCenter(locationPoint);
		        			
		        			VerticalPanel infoVerticalPanel = new VerticalPanel();
		        			FlexTable parkInfoFlexTable = new FlexTable();
		        			parkInfoFlexTable.setText(0, 0, selected);
		        			parkInfoFlexTable.setText(1, 0, address);
		        			parkInfoFlexTable.setText(2, 0, neighbourhoodName);
		        			
		        			infoVerticalPanel.add(parkInfoFlexTable);
		        			theMap.getInfoWindow().open(theMap.getCenter(), new InfoWindowContent(infoVerticalPanel));
		        		}
		        	}
		        }
		      }
		    });
		    
		clearMapAndList();
		List<String> currentMapParkList = dataProvider.getList();
		mapParkList.setVisibleRange(0, parks.length);
		for (Park p : parks) {
			LatLong theLatLong = convertGMDtoLatLong(p.getGoogleMapDest());
			final LatLng locationPoint = LatLng.newInstance(theLatLong.getLat(),
					theLatLong.getLong());
			final String parkName = p.getName();
			final String neighbourhoodName = p.getNeighbourhoodName();
			final String streetNumber = p.getStreetNumber();
			final String streetName = p.getStreetName();
			
			currentMapParkList.add(parkName);
			
			// define options for the marker
			MarkerOptions markerOptions = MarkerOptions.newInstance();
			markerOptions.setClickable(true);
			Marker theMarker = new Marker(locationPoint, markerOptions);
			
			// handle click events for the marker
			theMarker.addMarkerClickHandler(new MarkerClickHandler() {
                @Override
                public void onClick(MarkerClickEvent event) {
                	theMap.getInfoWindow().close();
                	String space = " ";
        			String address;
        			if (streetNumber.contains("N/A")) {
        				address = streetName;
        			} else {
        				String streetNumber2 = streetNumber.concat(space);
        				address = streetNumber2.concat(streetName);
        			}
                	
        			VerticalPanel infoVerticalPanel = new VerticalPanel();
        			FlexTable parkInfoFlexTable = new FlexTable();
        			parkInfoFlexTable.setText(0, 0, parkName);
        			parkInfoFlexTable.setText(1, 0, address);
        			parkInfoFlexTable.setText(2, 0, neighbourhoodName);
        			
        			infoVerticalPanel.add(parkInfoFlexTable);
        			theMap.getInfoWindow().open(locationPoint, new InfoWindowContent(infoVerticalPanel));
        			
        			//selected the park name in the list associated with the clicked marker
        			@SuppressWarnings("unchecked")
					SingleSelectionModel<String> selectionModel = (SingleSelectionModel<String>) mapParkList.getSelectionModel();
        			selectionModel.setSelected(parkName, true);
        
                }

        });
			
			
			theMap.addOverlay(theMarker);
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
	
	private void clearMapAndList() {
		theMap.clearOverlays();
		mapParkList.setVisibleRangeAndClearData(new Range(0, 0), true);
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
	
	


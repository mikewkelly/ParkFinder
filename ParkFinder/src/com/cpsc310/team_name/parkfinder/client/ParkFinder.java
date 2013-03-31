package com.cpsc310.team_name.parkfinder.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cpsc310.team_name.parkfinder.shared.FieldVerifier;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapType;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.control.MapTypeControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.SelectionChangeEvent;
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
	private HorizontalPanel searchPanelPark = new HorizontalPanel();
	private VerticalPanel listPanel = new VerticalPanel();
	private TextBox searchFacilityTextBox = new TextBox();
	private TextBox searchParkTextBox = new TextBox();
	
	private Button searchFacilityButton = new Button("Search Facility");
	private Button searchParkButton = new Button("Search Park");
    final ListBox nbhdDropBox = new ListBox();

	private Label lastUpdateLabel = new Label();
	private Label errorMessage = new Label();
	private Label successMsg = new Label();
	private Label facilityLabel = new Label("Enter the type of facility:");
	private Label parkLabel = new Label("Or, enter the park name:");
	private Label nbhdLabel = new Label("Select a neighbourhood");
	// for the table
	private FlexTable parkTable = new FlexTable();
	private VerticalPanel tablePanel = new VerticalPanel();

	// for the map
	CellList<String> mapParkList;
	ListDataProvider<String> dataProvider;
	ScrollPanel mapParkListScrollPanel;
	MapWidget theMap;


	public String facilityToSearch=null;
	public String nbhd="All";
	public String searchParkName =null;

	// the string list to store the primary keys
	public ArrayList<String> parklist = new ArrayList<String>();
	private ArrayList<String> facilitylist=new ArrayList<String>();
	private ArrayList<String> nbhdlist = new ArrayList<String>();
	// the Async server objects
	private final ParkServiceAsync parkService = GWT.create(ParkService.class);
	private final FacilityServiceAsync facilityService = GWT.create(FacilityService.class);
	private final AreaServiceAsync areaService = GWT.create(AreaService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		loadParkTable();

		mainPanel.addNorth(headerPanel, 100);
		mainPanel.addNorth(searchPanel, 165);
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
		
		parkTable.setText(0, 0, "Park Name");
		parkTable.setText(0, 1, "Neighbourhood");
		parkTable.setText(0, 2, "Address");
		parkTable.setText(0, 3, "Facilities");
		parkTable.setText(0, 4, "Weekend Status");

		parkTable.getRowFormatter().addStyleName(0, "parkListHeader");
		parkTable.addStyleName("parkList");
		parkTable.setCellPadding(10);

		
		searchPanelContents.add(searchFacilityTextBox);
		searchPanelContents.add(searchFacilityButton);
		searchPanelContents.addStyleName("inputTextBox");
		
		searchPanelPark.add(searchParkTextBox);
		searchPanelPark.add(searchParkButton);
		searchPanelPark.addStyleName("inputTextBox");

		searchFacilityButton.addStyleDependentName("search");
		searchParkButton.addStyleDependentName("search");
		errorMessage.setStyleName("errorMessage");
		successMsg.setStyleName("successMessage");
		errorMessage.setVisible(false);
		successMsg.setVisible(false);

		
		
		searchPanel.add(facilityLabel);
		searchPanel.add(searchPanelContents);
		searchPanel.add(parkLabel);
		searchPanel.add(searchPanelPark);
		searchPanel.add(errorMessage);
		searchPanel.add(successMsg);

		tablePanel.add(parkTable);
		tablePanel.add(lastUpdateLabel);

		searchFacilityTextBox.setFocus(true);

		importData();
		
		nbhdDropBox.addStyleDependentName("drop");
		
		nbhdLabel.addStyleName("nbhdlabel");
		listPanel.add(nbhdLabel);
		listPanel.add(nbhdDropBox);
		nbhdDropBox.setVisibleItemCount(1);
		searchPanelContents.add(listPanel);
		// Add a drop box with the list types
	    
	    
		searchFacilityButton.addClickHandler(new ClickHandler(){

			public void onClick(ClickEvent event) {
					SearchFacility();
			}
			
		});
		nbhdDropBox.addChangeHandler(new ChangeHandler(){
			public void onChange(ChangeEvent event)
			{
				int selectIndex = nbhdDropBox.getSelectedIndex();
				nbhd = nbhdDropBox.getValue(selectIndex);
			}

		});
			
		
		searchParkButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				getParkbyName();
			}
		});

	}

	private void importData() {
		//import parks to GWT server
		parkService.importParks(new AsyncCallback<String[]>() {
			public void onFailure(Throwable error) {
				errorMessage.setText("Error: failed to import data");
				errorMessage.setVisible(true);
			}
			public void onSuccess(String[] neighborhood) {
				nbhdDropBox.addItem("All");
				for(String n:neighborhood)
				{	nbhdDropBox.addItem(n);
				}
			}
		});
		//import facilities to GWT server
		facilityService.importFacility(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				System.out.println("faclity fail");

				errorMessage.setText("Error: failed to import data");
				errorMessage.setVisible(true);
			}
			public void onSuccess(Void ignore) {
				
				
			}
		});
		//import areas to GWT server
		areaService.importArea(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				System.out.println("area fail");
				errorMessage.setText("Error: failed to import data");
				errorMessage.setVisible(true);
			}
			public void onSuccess(Void ignore) {
				
			}
		});
	}
	
private void SearchFacility() {
	
		clearMapAndList();

		refreshTable();
		parklist.clear();
		facilitylist.clear();
		facilityToSearch = searchFacilityTextBox.getText().trim();
		searchFacilityTextBox.setFocus(true);
			
		successMsg.setText("Getting " +facilityToSearch+ " from server...");
    	successMsg.setVisible(true);
    	
    	System.out.println("facility "+facilityToSearch);
	
    	
			facilityService.getFacility(facilityToSearch,new AsyncCallback<Facility[]>() {
    	
			public void onFailure(Throwable error) {
				errorMessage.setText("Error: failed to receive data from server");
				errorMessage.setVisible(true);
			}
			
		    public void onSuccess(Facility[] facilities) {
		    	successMsg.setText("Getting data from server...");
		    	successMsg.setVisible(true);
		    	for(Facility f:facilities) {
		    		if(!facilitylist.contains(f.getFacilityID())) {
		    			facilitylist.add(f.getFacilityID());
		    			if(!parklist.contains(f.getParkId()))
		    			{
		    				parklist.add(f.getParkId());
		    			}
		    		}
	    		}

		    	parkService.getParks(parklist, new AsyncCallback<Park[]>() {
		    		int row=1;
					public void onFailure(Throwable error) {
						errorMessage.setText("Error: failed to receive data from server");
						errorMessage.setVisible(true);
					}
				    public void onSuccess(Park[] parks) {
				    	successMsg.setText("Getting data from server...");
				    	successMsg.setVisible(true);
				    	
				    	if(!nbhd.equals("All"))
				    	{
				    		for(Park p:parks)
				    		{
				    			if(p.getNeighbourhoodName().equals(nbhd))
				    			{	
				    				showParkInTable(p,row);
				    				row++;				    		
				    			}
				    		}
				    		
				    	}
				    	else
				    	{
				    		for(Park p:parks) {
					    		showParkInTable(p,row);
					    		row++;				    		
					  
				    		}
				    	}
				    	
				    	
				displayAllInMap();
				if(!facilityToSearch.isEmpty())
		    	{
		    		if(!nbhd.equals("All"))
		    		successMsg.setText("Found parks with " + facilityToSearch+ " : " + --row+ " in "+ nbhd+" Neighbourhood.");
		    		else
		    		successMsg.setText("Found parks with " + facilityToSearch+ " : " + --row+" in Vancouver.");
		    	}
		    	else{
		    		if(nbhd.equals("All"))
		    			successMsg.setText("Display all "+--row+ "parks in Vancouver");
		    		else
		    			successMsg.setText("Display all " + --row+" parks in " + nbhd +" neighbourhood.");
		    	}
		    }
		});

			}
			
		});
}
	private void refreshTable(){
		int row = parkTable.getRowCount();
		for(int i=row-1;i>0;i--){
			parkTable.removeRow(i);
		}
	}
	
	
private void getParkbyName()
{
	clearMapAndList();
	refreshTable();
	parklist.clear();
	facilitylist.clear();
	searchParkName = searchParkTextBox.getText().trim();
	searchParkTextBox.setFocus(true);
		
	successMsg.setText("Getting " +facilityToSearch+ " from server...");
	successMsg.setVisible(true);
	parkService.getParkByName(searchParkName,nbhd, new AsyncCallback<Park[]>() {
		int row=1;
		public void onFailure(Throwable error) {
			errorMessage.setText("Error: failed to receive data from server");
			errorMessage.setVisible(true);
		}
	    public void onSuccess(Park[] parks) {
	    	successMsg.setText("Getting data from server...");
	    	successMsg.setVisible(true);
	    	
	    	
	    		for(Park p:parks)
	    		{
	    				
	    				showParkInTable(p,row);
	    				if(!parklist.contains(p.getParkId()))
	    				parklist.add(p.getParkId());
	    				row++;				    		
	    			
	    		}
	    		
	    	
	    	
	    	
	    	
	    displayAllInMap();
		
	if(!searchParkName.isEmpty())
	{
		if(!nbhd.equals("All"))
		successMsg.setText("Found parks with ' " + searchParkName+ " ' : " + --row+ " in "+ nbhd+" Neighbourhood.");
		else
		successMsg.setText("Found parks with ' " + searchParkName+ " ' : " + --row+" in Vancouver.");
	}
	else
	{
		if(nbhd.equals("All"))
			successMsg.setText("Display all "+ --row+ "parks in Vancouver");
		else
			successMsg.setText("Display all " + --row+" parks in " + nbhd +" neighbourhood.");
	}
 }
});

}





private void showParkInTable(Park park,int row) {
		
		parkTable.setText(row, 0, park.getName());
		parkTable.setText(row, 1, park.getNeighbourhoodName());
		parkTable.setText(row, 2, String.valueOf(park.getStreetNumber().concat(" ").concat(park.getStreetName())));	
		final String parkId = park.getParkId();
		final String parkName =  park.getName();
		Button showAreaButton = new Button("Status");
		showAreaButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){

				areaService.getArea(parkId, new AsyncCallback<Area[]>() {
					public void onFailure(Throwable error) {
						Window.alert("SORRY, SERVER NOT AVAILABLE");
					}
				    public void onSuccess(Area[] areas) {
				    	successMsg.setText("Getting data from server...");
				    	successMsg.setVisible(true);
				    		showWeekendStatus(areas,parkName);
				    		successMsg.setText("There are " + areas.length+" status notes.");
				    	
				    	
					}
				});
			}
		});
		Button showFacilityButton = new Button("Facility");
		showFacilityButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){

				facilityService.getFacilitybyPark(parkId, new AsyncCallback<Facility[]>() {
					public void onFailure(Throwable error) {
						Window.alert("SORRY, SERVER NOT AVAILABLE");
					}
				    public void onSuccess(Facility[] facilities) {
				    	successMsg.setText("Getting data from server...");
				    	successMsg.setVisible(true);
				    		showFacility(facilities,parkName);
				    		successMsg.setText("There are " + facilities.length+" status notes.");
				    	
				    	
					}
				});
			}
		});
		parkTable.setWidget(row, 3, showFacilityButton);
		parkTable.setWidget(row, 4, showAreaButton);


		}

private void showWeekendStatus(Area[] areas,String parkName)
{
	FlexTable statusTable = new FlexTable();
	statusTable.setText(0, 0, parkName);
	statusTable.setText(1,0,"SiteArea");
	statusTable.setText(1,1,"ClosureNote");
	statusTable.setText(1,2,"WeekendStatus");
	statusTable.setText(1,3,"Last Update on");

	statusTable.getRowFormatter().addStyleName(0, "areaNameListHeader");
	statusTable.getRowFormatter().addStyleName(1,"areaListHeader");
	statusTable.addStyleName("parkList");
	statusTable.setCellPadding(5);
	
	
	PopupPanel pop = new PopupPanel();
	if(areas.length==0)
	{
		Label l = new Label();
		l.setText("No current status avaliable for  "+ parkName);
		pop.setWidget(l);
	}
	else
	{
		int row=2;
		for(Area a:areas)
		{
			statusTable.setText(row, 0, a.getSiteArea());
			statusTable.setText(row, 1, a.getClosureNotes());
			statusTable.setText(row, 2, a.getWeekendStatus());
			statusTable.setText(row, 3, a.getLastUpdated());
			row++;
		}	
		pop.setWidget(statusTable);
	
	}
	pop.setAnimationEnabled(true);
	pop.setAutoHideEnabled(true);
	pop.setGlassEnabled(true);

	pop.center();
}
private void showFacility(Facility[] facilities,String parkName)
{
	FlexTable facilityTable = new FlexTable();
	facilityTable.setText(0, 0, parkName);
	facilityTable.setText(1,0,"Facility");
	facilityTable.setText(1,1,"Count");
	

	facilityTable.getRowFormatter().addStyleName(0, "areaNameListHeader");
	facilityTable.getRowFormatter().addStyleName(1,"areaListHeader");
	facilityTable.addStyleName("parkList");
	facilityTable.setCellPadding(5);
	
	
	PopupPanel pop = new PopupPanel();
	if(facilities.length==0)
	{
		Label l = new Label();
		l.setText("No facilities avaliable in "+ parkName);
		pop.setWidget(l);
	}
	else
	{
		int row=2;
		for(Facility f:facilities)
		{
			facilityTable.setText(row, 0, f.getFacility());
			facilityTable.setText(row, 1, f.getFacilityCount());
			
			row++;
		}	
		pop.setWidget(facilityTable);
	
	}
	pop.setAnimationEnabled(true);
	pop.setAutoHideEnabled(true);
	pop.setGlassEnabled(true);
	pop.center();

}


	// The following methods have to do with the Map

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

		parkService.getParks(parklist,new AsyncCallback<Park[]>() {
			public void onFailure(Throwable error) {
				errorMessage
						.setText("Error: failed to receive data from server");
				errorMessage.setVisible(true);
			}

			public void onSuccess(Park[] parks) {
				
				ArrayList<Park> newparklist = new ArrayList<Park>();
				if(!nbhd.equals("All"))
				{
					for(Park p:parks)
				
					{
					
					if(p.getNeighbourhoodName().equals(nbhd))
					{
						newparklist.add(p);
						
					}
				}
					updateMap(newparklist.toArray(new Park[newparklist.size()]));
			}
				else
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
	
	


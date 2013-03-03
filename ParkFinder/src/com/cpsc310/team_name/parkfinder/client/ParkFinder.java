package com.cpsc310.team_name.parkfinder.client;

import java.util.ArrayList;
import com.cpsc310.team_name.parkfinder.shared.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ParkFinder implements EntryPoint {
	
	private FlexTable parkTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();
	private VerticalPanel mainPanel = new VerticalPanel();
	private TextBox searchCriteriaTextBox = new TextBox();
	private Button displayAllButton = new Button("DisplayAll");
	private Button searchButton = new Button("Search");
	private Label lastUpdateLabel = new Label();
	private Label errorMessage = new Label();
	private Label successMsg = new Label();
	private ArrayList<Park> parklist = new ArrayList<Park>();
	
	private Button importDataButton = new Button("Import");
	
	private final ParkServiceAsync parkService = GWT.create(ParkService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		loadParkTable();
		
	}
	
	public void loadParkTable() {
		// table layout
		parkTable.setText(0,0,"Park ID");
		parkTable.setText(0,1,"Park Name");
		parkTable.setText(0,2,"Neighbourhood");
		parkTable.setText(0,3,"Street Name");
		parkTable.setText(0,4,"Street Number");
		parkTable.setText(0,5,"GoogleMapDest");
		parkTable.setText(0,6,"Facilities");
		parkTable.setText(0,7,"Area");
		
		parkTable.getRowFormatter().addStyleName(0, "parkListHeader");
		parkTable.addStyleName("parkList");
		parkTable.setCellPadding(10);
		
		// used for future implementation on searching
		addPanel.add(searchCriteriaTextBox);
		addPanel.add(searchButton);
		addPanel.add(displayAllButton);
		addPanel.add(importDataButton);
		addPanel.addStyleName("inputTextBox");
		searchButton.addStyleDependentName("search");
		errorMessage.setStyleName("errorMessage");
		successMsg.setStyleName("successMessage");
		errorMessage.setVisible(false);
		successMsg.setVisible(false);
		
		mainPanel.add(errorMessage);
		mainPanel.add(successMsg);
		mainPanel.add(parkTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdateLabel);
		
		RootPanel.get("parkList").add(mainPanel);
		
		searchCriteriaTextBox.setFocus(true);
		
		displayAllButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				displayAll();
			}
		});
		
		importDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				importData();
			}
		});
		
	}
	
	private void importData() {
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
	}
	
	private void displayAll() {
		parkService.getParks(new AsyncCallback<Park[]>() {
			public void onFailure(Throwable error) {
				errorMessage.setText("Error: failed to receive data from server");
				errorMessage.setVisible(true);
			}
		    public void onSuccess(Park[] parks) {
		    	successMsg.setText("Getting data from server...");
		    	successMsg.setVisible(true);
		    	for(Park p:parks) {
		    		parklist.add(p);
		    		showParkInTable(p);
		    	}
		    	successMsg.setText("Parks Displayed");
			}
		});
	}

	private void showParkInTable(Park park) {
		int row = parkTable.getRowCount();
		
		//String parkfacilities = "";
		//for(Facility s:park.getParkFacilities().getFacilities()) {
			//parkfacilities = parkfacilities + s.getFacilityType() + " ";
		//}
		parkTable.setText(row, 0, park.getParkId());
		parkTable.setText(row, 1, park.getName());
		parkTable.setText(row, 2, park.getNeighbourhoodName());
		parkTable.setText(row,3,park.getStreetName());
		parkTable.setText(row, 4, park.getStreetNumber());
		//parkTable.setText(row, 5, String.valueOf(park.getGoogleMapDest().getLat())
		//		+String.valueOf(park.getGoogleMapDest().getLong()));
		//parkTable.setText(row, 6, parkfacilities);
		

	}
	
	

}

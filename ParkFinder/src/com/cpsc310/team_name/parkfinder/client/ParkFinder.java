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
	private ArrayList<String> parklist = new ArrayList<String>();
	
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
		parkTable.setText(0,3,"Address");
		parkTable.setText(0,4,"GoogleMapDest");
		parkTable.setText(0,5,"Facilities");
		//parkTable.setText(0,6,"Area");
		
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
		successMsg.setText("Getting data from server...");
    	successMsg.setVisible(true);
		parkService.getParks(new AsyncCallback<Park[]>() {
			public void onFailure(Throwable error) {
				errorMessage.setText("Error: failed to receive data from server");
				errorMessage.setVisible(true);
			}
		    public void onSuccess(Park[] parks) {
		    	for(Park p:parks) {
		    		if(!parklist.contains(p.getParkId())) {
		    			parklist.add(p.getParkId());
		    			showParkInTable(p);
		    		}
		    	}
		    	successMsg.setText("Parks Displayed");
			}
		});
	}

	private void showParkInTable(Park park) {
		int row = parkTable.getRowCount();
		
		
		parkTable.setText(row, 0, park.getParkId());
		parkTable.setText(row, 1, park.getName());
		parkTable.setText(row, 2, park.getNeighbourhoodName());
		parkTable.setText(row, 3, park.getStreetNumber() + "," + park.getStreetName()); 
		parkTable.setText(row, 4, "("+park.getGoogleMapDest()+")");
		parkTable.setText(row, 5, park.getParkFacilities());
		

	}
	
	

}

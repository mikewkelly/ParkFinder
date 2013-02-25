package com.cpsc310.team_name.parkfinder.client;

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
	private Button displayAll = new Button("DisplayAll");
	private Button search = new Button("Search");
	private Label lastUpdateLabel = new Label();
	
	private final ParkServiceAsync parkService = GWT.create(ParkService.class);
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final Button sendButton = new Button("Send");
		final TextBox nameField = new TextBox();
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
	}
	
	public void loadParkTable() {
		// table layout
		parkTable.setText(0,0,"Park ID");
		parkTable.setText(0,1,"Park Name");
		parkTable.setText(0,2,"Neighbourhood");
		parkTable.setText(0,3,"Street Number");
		parkTable.setText(0,4,"GoogleMapDest");
		parkTable.setText(0,5,"Facilities");
		parkTable.setText(0,6,"Area");
		
		// used for future implementation on searching
		addPanel.add(searchCriteriaTextBox);
		addPanel.add(search);
		addPanel.add(displayAll);
		
		mainPanel.add(parkTable);
		mainPanel.add(addPanel);
		mainPanel.add(lastUpdateLabel);
		
		RootPanel.get("parkList").add(mainPanel);
		
		searchCriteriaTextBox.setFocus(true);
		
		displayAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event){
				displayAll();
			}
		});
		
	}
	
	private void displayAll() {
		parkService.getParks(new AsyncCallback<Park[]>() {
			public void onFailure(Throwable error) {
				// TODO handling the error
			}
		    public void onSuccess(Park[] parks) {
		    	for(Park p:parks) {
		    		showParkInTable(p);
		    	}
			}
		});
	}

	private void showParkInTable(Park park) {
		int row = parkTable.getRowCount();
		
		String id = String.valueOf(park.getParkId());
		String parkfacilities = "";
		for(Facility s:park.getParkFacilities().getFacilities()) {
			parkfacilities = parkfacilities + s.getFacilityType() + " ";
		}
		parkTable.setText(row, 0, id);
		parkTable.setText(row, 1, park.getName());
		parkTable.setText(row, 2, park.getNeighbourhoodName());
		parkTable.setText(row, 3, String.valueOf(park.getStreetNumber()));
		parkTable.setText(row, 4, String.valueOf(park.getGoogleMapDest().getLat())
				+String.valueOf(park.getGoogleMapDest().getLong()));
		parkTable.setText(row, 5, parkfacilities);

	}
	
	

}

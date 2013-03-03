package com.cpsc310.team_name.parkfinder.client;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ParkServiceAsync {
	public void addPark(String parkId, AsyncCallback<Void> async);
	public void removePark(String parkId, AsyncCallback<Void> async);
	public void getParks(AsyncCallback<Park[]> async);
	public void importParks(AsyncCallback<Void> async);
	
}

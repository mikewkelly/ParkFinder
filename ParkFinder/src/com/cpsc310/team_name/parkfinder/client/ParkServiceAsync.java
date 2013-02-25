package com.cpsc310.team_name.parkfinder.client;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ParkServiceAsync {
	public void addPark(int parkId, AsyncCallback<Void> async);
	public void removePark(int parkId, AsyncCallback<Void> async);
	public void getParks(AsyncCallback<Park[]> async);
}

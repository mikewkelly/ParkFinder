package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("Park")
public interface ParkService extends RemoteService {
	public void addPark(String parkId);
	public Park[] getParks();
	public void removePark(String parkId);
	public void importParks();
}

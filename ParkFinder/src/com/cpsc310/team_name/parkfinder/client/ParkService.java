package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parkservice")
public interface ParkService extends RemoteService {
	public void addPark(Long parkId);
	public Park[] getParks();
	public void removePark(Long parkId);
	public void importParks();
}

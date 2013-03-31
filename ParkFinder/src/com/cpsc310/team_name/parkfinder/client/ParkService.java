package com.cpsc310.team_name.parkfinder.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parkservice")
public interface ParkService extends RemoteService {
	public void addPark(String parkId);
	public Park[] getParks(ArrayList<String> idPara);
	public Park[] getParkByName(String parkName, String nbhd);
	public void removePark(String parkId);
	public String[] importParks();
}

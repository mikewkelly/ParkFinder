package com.cpsc310.team_name.parkfinder.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("parkservice")
public interface ParkService extends RemoteService {
	public void addPark(Long parkId);
	public Park[] getParks(ArrayList<Long> idPara);
	public Park[] getParkByName(String parkName, String nbhd);
	public void removePark(Long parkId);
	public String[] importParks();
}

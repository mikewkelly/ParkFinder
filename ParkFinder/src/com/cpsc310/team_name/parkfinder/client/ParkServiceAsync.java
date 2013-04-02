package com.cpsc310.team_name.parkfinder.client;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ParkServiceAsync {
	public void addPark(Long parkId, AsyncCallback<Void> async);
	public void removePark(Long parkId, AsyncCallback<Void> async);
	public void getParks(ArrayList<Long> idPara,AsyncCallback<Park[]> async);
	public void getParkByName(String parkName, String nbhd, AsyncCallback<Park[]> async);
	public void importParks(AsyncCallback<String[]> async);
}

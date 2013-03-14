package com.cpsc310.team_name.parkfinder.client;
import com.google.gwt.user.client.rpc.AsyncCallback;


public interface FacilityServiceAsync {

	public void addFacility(Long parkId, AsyncCallback<Void> async);
	public void removeFacility(Long parkId, AsyncCallback<Void> async);
	public void getFacility(AsyncCallback<Facility[]> async);
	public void importFacility(AsyncCallback<Void> async);
}

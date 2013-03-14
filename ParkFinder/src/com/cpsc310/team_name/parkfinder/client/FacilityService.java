package com.cpsc310.team_name.parkfinder.client;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("facilityservice")
public interface FacilityService extends RemoteService{
	

	public void addFacility(Long parkId);
	public Facility[] getFacility();
	public void removeFacility(Long parkId);
	public void importFacility();
}


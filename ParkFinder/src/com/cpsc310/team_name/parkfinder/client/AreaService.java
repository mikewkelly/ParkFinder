package com.cpsc310.team_name.parkfinder.client;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("areaservice")
public interface AreaService extends RemoteService{
	

	public void addArea(Long parkId);
	public Area[] getArea();
	public void removeArea(Long parkId);
	public void importArea();
}

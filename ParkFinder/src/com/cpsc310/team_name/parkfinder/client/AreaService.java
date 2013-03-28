package com.cpsc310.team_name.parkfinder.client;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("areaservice")
public interface AreaService extends RemoteService{

	public void addArea(String parkId);
	public Area[] getArea(String parkID);
	public void removeArea(String parkId);
	public void importArea();
}

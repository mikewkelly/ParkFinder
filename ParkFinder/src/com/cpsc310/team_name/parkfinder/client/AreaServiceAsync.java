package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AreaServiceAsync {

	void addArea(String parkId, AsyncCallback<Void> callback);

	void getArea(String parkId,AsyncCallback<Area[]> callback);

	void importArea(AsyncCallback<Void> callback);

	void removeArea(String parkId, AsyncCallback<Void> callback);

}

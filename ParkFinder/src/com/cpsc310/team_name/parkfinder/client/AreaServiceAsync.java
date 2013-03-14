package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface AreaServiceAsync {

	void addArea(Long parkId, AsyncCallback<Void> callback);

	void getArea(AsyncCallback<Area[]> callback);

	void importArea(AsyncCallback<Void> callback);

	void removeArea(Long parkId, AsyncCallback<Void> callback);

}

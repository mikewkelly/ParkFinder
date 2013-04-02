package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserSearchServiceAsync {
	public void add(String search, AsyncCallback<Void> async);
	public void getUserSearch(AsyncCallback<String> async);
}

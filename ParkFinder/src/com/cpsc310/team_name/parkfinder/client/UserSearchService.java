package com.cpsc310.team_name.parkfinder.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("usersearchservice")
public interface UserSearchService extends RemoteService {
	public void add(String search);
	public String getUserSearch();
}

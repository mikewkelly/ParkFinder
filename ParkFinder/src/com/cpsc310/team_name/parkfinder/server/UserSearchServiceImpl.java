package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.cpsc310.team_name.parkfinder.client.UserSearchService;

public class UserSearchServiceImpl extends RemoteServiceServlet implements UserSearchService {

	private static final Logger LOG = Logger.getLogger(UserSearchServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
	      JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public void add(String search) {
		PersistenceManager pm = getPersistenceManager();
		
		try {
			Query q = pm.newQuery(UserSearch.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<UserSearch> usersearch = (List<UserSearch>) q.execute(getUser());
			if(usersearch.size()!=0) {
				if(!usersearch.get(0).getSearch().toUpperCase().contains(search.toUpperCase())) {
					usersearch.get(0).addSearch(search);
				}
			}else{
				UserSearch us = new UserSearch(getUser(),search);
				pm.makePersistent(us);
			}
		} finally {
			pm.close();
		}
	}
	
	public String getUserSearch() {
		String result = "";
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(UserSearch.class,"user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
			List<UserSearch> usersearch = (List<UserSearch>) q.execute(getUser());
			if(usersearch.size()!=0)
				result = usersearch.get(0).getSearch();
		} finally {
			pm.close();
		}
		return result;
	}
	
	private User getUser() {
		UserService userService = UserServiceFactory.getUserService();
	    return userService.getCurrentUser();
	}
	
	private PersistenceManager getPersistenceManager() {
	    return PMF.getPersistenceManager();
	}
}

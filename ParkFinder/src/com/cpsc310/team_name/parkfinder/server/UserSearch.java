package com.cpsc310.team_name.parkfinder.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.users.User;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class UserSearch {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String search;
	
	@Persistent
	private User user;
	
	public UserSearch() {
		
	}
	
	public UserSearch(User user, String search) {
		this.user = user;
		this.search = search;
	}
	
	public User getUser() {
		return user;
	}
	
	public String getSearch() {
		return search;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setSearch(String search) {
		this.search = search;
	}
	
	public void addSearch(String added) {
		String tmp[] = this.search.split(" ");
		if(tmp.length<3) {
			this.search = this.search + " " + added;
		}else{
			this.search = tmp[1] + " " + tmp[2] + " " + added;
		}
	}

}

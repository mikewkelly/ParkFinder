package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;

public class SearchCriteria {
	private ArrayList<String> stringList;
	
	public SearchCriteria() {
	}
	public void setSearchFacility(ArrayList<String> list)
	{
		stringList = list;
	}
	
	public ArrayList<String> getList()
	{
		return stringList;
	}
		
	
	public String criteriaToSearch(String input){
		String theType = null;
		for(String str: stringList)
		{
			if(str.toLowerCase().contains(input.toLowerCase()))
			{
				theType = str;
				System.out.println(theType);
			}
		}
		return theType;
		
		
	}

	
}



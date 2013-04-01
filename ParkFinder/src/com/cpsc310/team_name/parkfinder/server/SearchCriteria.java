package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;

public class SearchCriteria {
	private ArrayList<String> stringList = new ArrayList<String>();
	
	
	public SearchCriteria() {
	}
	public void setSearch(ArrayList<String> list)
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
	
	public String getSimilarFacility(String input)
	{
		String goodGuess = null;
		char[] inputArray = input.toLowerCase().toCharArray();
		int count = 0;
		for(String str:stringList)
		{
			char[] matchArray = str.toLowerCase().toCharArray(); 
			int i =0;
			int j = 0;
			int temp = 0;
			int tempcount=0;
			while(i<input.length()-1&&input.length()<=str.length())
			{
				
				if(inputArray[i]==matchArray[j])
				{
					i++;
					j++;
					tempcount++;
					temp=j;
					if(temp==matchArray.length)
						i=input.length();
				}
				else
				{
					if(j>=matchArray.length-1)
					{	j=temp;
						i++;
					}
					else
						j++;
				}
				
				
			}
			if(tempcount>count)
			{	
				count=tempcount;
				goodGuess = str;
			}
		}
		
		return goodGuess;
		
	}
	
	
	public ArrayList<String> getSimilarNames(String input)
	{
		ArrayList<String> goodGuess = new ArrayList<String>();
		char[] inputArray = input.toLowerCase().toCharArray();
		for(String str:stringList)
		{
			char[] matchArray = str.toLowerCase().toCharArray(); 
			int i =0;
			int j = 0;
			int temp = 0;
			int count=0;
			System.out.println(str);
			System.out.println(input);
			System.out.println(str.length()+"  "+input.length());
			while(i<input.length()-1&&input.length()<=str.length())
			{
				System.out.println("i = "+i);
				System.out.println("j = "+j);
				System.out.println("count = "+count);
				if(inputArray[i]==matchArray[j])
				{
					i++;
					j++;
					count++;
					temp=j;
					if(temp==matchArray.length)
						i=input.length();
				}
				else
				{
					if(j>=matchArray.length-1)
					{	j=temp;
						i++;
					}
					else
						j++;
				}
				
				
			}
			if(input.length()<=5)
			{
					if(count>=input.length()-1)
					goodGuess.add(str.toLowerCase());
			}
			else
			{
				if(count>=input.length()*0.7)
				goodGuess.add(str.toLowerCase());
			}
		
		}
		
		return goodGuess;
	}

	
	}



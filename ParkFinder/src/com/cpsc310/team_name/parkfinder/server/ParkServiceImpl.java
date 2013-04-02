package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.cpsc310.team_name.parkfinder.client.Facility;
import com.cpsc310.team_name.parkfinder.client.Park;
import com.cpsc310.team_name.parkfinder.client.ParkService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class ParkServiceImpl extends RemoteServiceServlet implements ParkService {

	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private ArrayList<String> names=new ArrayList<String>();
	private ArrayList<String> nbhd = new ArrayList<String>();
	private SearchCriteria search = new SearchCriteria();
	
	@Override
	public void addPark(Long parkId) {
		PersistenceManager pm = getPersistenceManager();
		try{
			pm.makePersistent(new Park(parkId));
		}
		finally{
				//TODO
			}
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	@Override
	public Park[] getParks(ArrayList<Long> idPara) {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Park> park = new ArrayList<Park>();
		try{
			for(Long str:idPara)
			{	
			
		        Park p = pm.getObjectById(Park.class, str);
		        park.add(p);
			
			}
		}
		finally{
			pm.close();
		}
		
		return park.toArray(new Park[park.size()]);
		
	}
	

	@Override
	public void removePark(Long parkId) {
		PersistenceManager pm = getPersistenceManager();
		try{
			long deleteCount = 0;
			Query q = pm.newQuery(Park.class);
			List<Park> parks = (List<Park>) q.execute();
			for(Park park:parks){
				if(parkId.equals(park.getParkId())){
					deleteCount++;
				pm.deletePersistent(park);
				}
			}
			LOG.log(Level.WARNING, "DELETED "+deleteCount+" PARKS");
		}finally{
			pm.close();
		}

	}
	
	@Override
	public String[] importParks() {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Park> parks = new ArrayList<Park>();
		ParserFacade pf = new ParserFacade();
		parks = pf.getPark();
		for(Park p:parks)
		{
		 if(!names.contains(p.getName()))
			 names.add(p.getName());
		 if(!nbhd.contains(p.getNeighbourhoodName()))
			 nbhd.add(p.getNeighbourhoodName());
		}
		try {			
			pm.makePersistentAll(parks);
		} finally {
			pm.close();		}
		search.setSearch(names);
		return nbhd.toArray(new String[nbhd.size()]);
	}

	@Override
	public Park[] getParkByName(String parkName, String nbhd) 
	{
		ArrayList<Park> nameMatchingParks =  new ArrayList<Park>();
		
		if(parkName.isEmpty()||parkName.length()<=2)
		{
			PersistenceManager pm = getPersistenceManager();
			System.out.println("find all");

			
		try
		{
			Query q = pm.newQuery(Park.class);
			q.setOrdering("parkId");
			List<Park> parks = (List<Park>) q.execute();
			if(nbhd.equals("All"))
			{	
				for (Park p:parks)
				{
					nameMatchingParks.add(p);
				
				}
			}
			else
			{
				for(Park p:parks)
				{
					if(p.getNeighbourhoodName().equals(nbhd))
						nameMatchingParks.add(p);
						
				}
			}
		}finally
		{
			pm.close();
		}
		}
		
		else
		{	
			
			ArrayList<String> guess = search.getSimilarNames(parkName);
			PersistenceManager pm = getPersistenceManager();
			


		try
		{
			Query q = pm.newQuery(Park.class);
			q.setOrdering("parkId");
			List<Park> parks = (List<Park>) q.execute();
			if(nbhd.equals("All"))
			{	
				for (Park p:parks)
				{
					if(guess.contains(p.getName().toLowerCase()))
					nameMatchingParks.add(p);
				
				}
			}
			else
			{
				for(Park p:parks)
				{
					if(p.getNeighbourhoodName().equals(nbhd)
							&&guess.contains(p.getName().toLowerCase()))
						nameMatchingParks.add(p);
						
				}
			}
		}finally
		{
			pm.close();
		}
		}
		return nameMatchingParks.toArray(new Park[nameMatchingParks.size()]);
				
	}
	
	
	

}

package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.cpsc310.team_name.parkfinder.client.Park;
import com.cpsc310.team_name.parkfinder.client.ParkService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;


public class ParkServiceImpl extends RemoteServiceServlet implements ParkService {

	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public void addPark(String parkId) {
		// TODO 
		PersistenceManager pm = getPersistenceManager();
		try{
			pm.makePersistent(new Park(parkId));
		}
		finally{
			pm.close();
		}
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

	@Override
	public Park[] getParks() {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Park> park = new ArrayList<Park>();
		
		// test
		//-----
		Park aPark = new Park("test_Id");
		aPark.setName("test_name");
		pm.makePersistent(aPark);
		//-----
		try{
			Query q = pm.newQuery(Park.class);
			q.setOrdering("parkId ");
			List<Park> parks = (List<Park>) q.execute();
			for (Park p:parks){
				park.add(p);
			}
		}finally{
			pm.close();
		}
		return park.toArray(new Park[park.size()]);
		
	}

	@Override
	public void removePark(String parkId) {
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
	public void importParks() {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Park> parks = new ArrayList<Park>();
		ParserFacade pf = new ParserFacade();
		parks = pf.parse();	
		try {			
			pm.makePersistentAll(parks);
		} finally {
			pm.close();
		}
	}

}

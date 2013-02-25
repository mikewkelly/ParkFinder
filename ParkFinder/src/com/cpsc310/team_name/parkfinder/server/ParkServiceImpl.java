package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import org.apache.commons.lang.ArrayUtils;

import com.cpsc310.team_name.parkfinder.client.Park;
import com.cpsc310.team_name.parkfinder.client.ParkService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.cpsc310.team_name.parkfinder.client.ParkService;

public class ParkServiceImpl extends RemoteServiceServlet implements ParkService {

	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@Override
	public void addPark(int parkId) {
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
		// TODO 
		return PMF.getPersistenceManager();
	}

	@Override
	public Park[] getParks() {
		// TODO 
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Park> park = new ArrayList<Park>();
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
	public void removePark(int parkId) {
		PersistenceManager pm = getPersistenceManager();
		try{
			long deleteCount = 0;
			Query q = pm.newQuery(Park.class);
			List<Park> parks = (List<Park>) q.execute();
			for(Park park:parks){
				if(parkId==park.getParkId()){
					deleteCount++;
					pm.deletePersistent(park);
				}
			}
			LOG.log(Level.WARNING, "DELETED "+deleteCount+" PARKS");
		}finally{
			pm.close();
		}

	}

}

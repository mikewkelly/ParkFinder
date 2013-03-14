package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.cpsc310.team_name.parkfinder.client.Facility;
import com.cpsc310.team_name.parkfinder.client.FacilityService;
import com.cpsc310.team_name.parkfinder.client.Park;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class FacilityServiceImpl extends RemoteServiceServlet implements
		FacilityService {
	
	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");

	@Override
	public void addFacility(Long parkId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Facility[] getFacility() {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Facility> facility =  new ArrayList<Facility>();
		try
		{
			Query q = pm.newQuery(Facility.class);
			q.setOrdering("facility");
			List<Facility> facilities = (List<Facility>) q.execute();
			for (Facility f:facilities)
			{
				facility.add(f);
			}
			
		}finally
		{
			pm.close();
		}
		
		return facility.toArray(new Facility[facility.size()]);
	}

	
	@Override
	public void removeFacility(Long parkId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void importFacility() {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Facility> facility = new ArrayList<Facility>();
		ParserFacade pf = new ParserFacade();
		facility = pf.getFacility();
		try {			
			pm.makePersistentAll(facility);
		} finally {
			pm.close();
		}

	}
	
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}

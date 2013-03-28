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
	
	private SearchCriteria searchFacility = new SearchCriteria();
	
	@Override
	public void addFacility(Long parkId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Facility[] getFacility(String fac) {
		
		ArrayList<Facility> facility =  new ArrayList<Facility>();
		System.out.println(searchFacility.getList().size());
		String facilityType = searchFacility.criteriaToSearch(fac);

		if(facilityType.isEmpty())
		{
			PersistenceManager pm = getPersistenceManager();
			System.out.println("find all");

			
		try
		{
			Query q = pm.newQuery(Facility.class);
			q.setOrdering("parkId");
			List<Facility> facilities = (List<Facility>) q.execute();
				for (Facility f:facilities)
			{
				facility.add(f);
				
			}
			
		}finally
		{
			pm.close();
		}
		}
		
		else
		{	
			PersistenceManager pm = getPersistenceManager();
			System.out.println("find certain facility");
			System.out.println(facilityType);


		try
		{
			Query q = pm.newQuery(Facility.class);
			q.setFilter("facility==facilityParam");
			q.setOrdering("parkId");
			q.declareParameters("String facilityParam");
			List<Facility> facilities = (List<Facility>) q.execute(facilityType);
			if(!facilities.isEmpty())
			{
				for (Facility f:facilities)
			{
				facility.add(f);
			}
			}
		}finally
		{
			pm.close();
		}
		}
		return facility.toArray(new Facility[facility.size()]);
	}
	
	@Override
	public Facility[] getFacilitybyPark(String parkId) {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Facility> facility =  new ArrayList<Facility>();
		
			Query q = pm.newQuery(Facility.class);
			q.setFilter("parkId==parkIdParam");
			q.setOrdering("parkId");
			q.declareParameters("String parkIdParam");
			
		try
		{
			List<Facility> facilities = (List<Facility>) q.execute(parkId);
			if(!facilities.isEmpty())
			{
				for (Facility f:facilities)
			{
				facility.add(f);
				f.getFacility();
			}
			}
		}finally
		{
			q.closeAll();
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
		ArrayList<String> type = new ArrayList<String>();

		ArrayList<Facility> facility = new ArrayList<Facility>();
		ParserFacade pf = new ParserFacade();
		facility = pf.getFacility();
		for(Facility f:facility)
		{
			if(!type.contains(f.getFacility()))
				type.add(f.getFacility());
		}
		try {	
			pm.makePersistentAll(facility);
		} 
		finally {

			pm.close();		
			}
		searchFacility.setSearchFacility(type);
		

	}
	
	
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}

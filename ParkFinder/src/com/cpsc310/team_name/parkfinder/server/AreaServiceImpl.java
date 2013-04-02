package com.cpsc310.team_name.parkfinder.server;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.cpsc310.team_name.parkfinder.client.Area;
import com.cpsc310.team_name.parkfinder.client.AreaService;
import com.cpsc310.team_name.parkfinder.client.Facility;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class AreaServiceImpl extends RemoteServiceServlet implements
		AreaService {

	private static final Logger LOG = Logger.getLogger(ParkServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	public void addArea(Long parkId) {
		//TODO
	}
	

	
	public Area[] getArea(Long parkID) {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Area> theArea =  new ArrayList<Area>();
		
		Query q = pm.newQuery(Area.class);
		q.setFilter("parkId==parkIdParam");
		q.setOrdering("parkId");
		q.declareParameters("String parkIdParam");
		try
		{
			
			List<Area> area = (List<Area>) q.execute(parkID);
			for (Area a:area)
			{
				theArea.add(a);
			}
			
		}finally
		{
			pm.close();
		}
		
		return theArea.toArray(new Area[theArea.size()]);
	}

	
	public void removeArea(Long parkId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void importArea() {
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Area> theArea = new ArrayList<Area>();
		ParserFacade pf = new ParserFacade();
		theArea = pf.getArea();	
		try {			
			pm.makePersistentAll(theArea);
		} finally {
			//TODO
		}

	}
	private PersistenceManager getPersistenceManager() {
		return PMF.getPersistenceManager();
	}

}

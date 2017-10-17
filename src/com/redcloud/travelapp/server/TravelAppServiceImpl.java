package com.redcloud.travelapp.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.redcloud.travelapp.client.TravelAppService;
import com.redcloud.travelapp.server.guice.GuiceDBModule;
import com.redcloud.travelapp.server.service.PlacesService;
import com.redcloud.travelapp.server.service.PlacesServiceImpl;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TravelAppServiceImpl extends RemoteServiceServlet implements TravelAppService {

	@Inject
	private PlacesService placesService;
	private PlacesServiceImpl placesServiceImpl;
	
	public TravelAppServiceImpl() {
		super();
		Injector injector = Guice.createInjector(new GuiceDBModule());
		placesServiceImpl = injector.getInstance(PlacesServiceImpl.class);  
	}

	public String login(String user, String password) throws IllegalArgumentException {
		return "";
	}

	/**
	 * Fetches the Data from DB for given City
	 */
	@Override
	public List<PlaceDBResult> fetchDBData(String searchCity) throws IllegalArgumentException {
		return placesServiceImpl.fetchDBData(searchCity);
	}

	/**
	 * Saves the first fetched Places Data per Search City
	 */
	@Override
	public String saveDBData(List<PlaceDBResult> dbResults) throws IllegalArgumentException {
		return placesServiceImpl.saveDBData(dbResults);
	}

	/**
	 * Edits from the User on the place commits to DB
	 */
	@Override
	public String updateValues(String placeID, String newRate, String mustSee)
			throws IllegalArgumentException {
		return placesServiceImpl.updateValues(placeID, newRate, mustSee);
	}

	/**
	 * Incase User wants to remove place from his List
	 */
	@Override
	public String removePlace(String placeID) throws IllegalArgumentException {
		return placesServiceImpl.removePlace(placeID) ;
	}
}

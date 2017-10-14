package com.redcloud.travelapp.server;

import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.redcloud.travelapp.client.TravelAppService;
import com.redcloud.travelapp.server.service.PlacesService;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class TravelAppServiceImpl extends RemoteServiceServlet implements TravelAppService {

	@Inject
	private PlacesService placesService;
	
	public String login(String user, String password) throws IllegalArgumentException {
		return "";
	}

	/**
	 * Fetches the Data from DB for given City
	 */
	@Override
	public List<PlaceDBResult> fetchDBData(String searchCity) throws IllegalArgumentException {
		return placesService.fetchDBData(searchCity);
	}

	/**
	 * Saves the first fetched Places Data per Search City
	 */
	@Override
	public String saveDBData(String searchCity, List<PlaceDBResult> dbResults) throws IllegalArgumentException {
		return placesService.saveDBData(searchCity, dbResults);
	}

	/**
	 * Edits from the User on the place commits to DB
	 */
	@Override
	public String updateValues(String searchCity, String placeID, String newRate, String mustSee)
			throws IllegalArgumentException {
		return placesService.updateValues(searchCity, placeID, newRate, mustSee);
	}

	/**
	 * Incase User wants to remove place from his List
	 */
	@Override
	public String removePlace(String searchCity, String placeID) throws IllegalArgumentException {
		return placesService.removePlace(searchCity, placeID) ;
	}
}

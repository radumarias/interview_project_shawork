package com.redcloud.travelapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TravelAppServiceAsync {
	void login(String username, String password, AsyncCallback<String> callback) throws IllegalArgumentException;

	/**
	 * Fetches Places data from DB for given Search city
	 */
	void fetchDBData(String searchCity, AsyncCallback<List<PlaceDBResult>> asyncCallback) throws IllegalArgumentException;

	/**
	 * Saves the Places DB data to Database 
	 */
	void saveDBData(List<PlaceDBResult> dbResults, AsyncCallback<String> asyncCallback) throws IllegalArgumentException;

	/**
	 * Updates user action on Places
	 */
	void updateValues(String placeID, String newRate, String mustSee, AsyncCallback<String> callback) throws IllegalArgumentException;

	/**
	 * Removes Place from DB for further listing
	 */
	void removePlace(String placeID, AsyncCallback<String> asyncCallback);

	//void getPlaceResponseJSON(double latitude, double longitude, AsyncCallback<String> asyncCallback);

	void getPlacesFromApi(String searchCity, double latitude, double longitude, AsyncCallback<List<PlaceDBResult>> callback);
}

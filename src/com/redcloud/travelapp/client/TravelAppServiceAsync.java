package com.redcloud.travelapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface TravelAppServiceAsync {
	void login(String username, String password, AsyncCallback<String> callback) throws IllegalArgumentException;

	void fetchDBData(String searchCity, AsyncCallback<List<PlaceDBResult>> asyncCallback) throws IllegalArgumentException;

	void saveDBData(String searchCity, List<PlaceDBResult> dbResults, AsyncCallback<String> asyncCallback) throws IllegalArgumentException;

	void updateValues(String searchCity, String placeID, String newRate, String mustSee, AsyncCallback<String> callback) throws IllegalArgumentException;

	void removePlace(String searchCity, String placeID, AsyncCallback<String> asyncCallback);
}

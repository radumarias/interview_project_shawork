package com.redcloud.travelapp.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("login")
public interface TravelAppService extends RemoteService {
	String login(String name, String password) throws IllegalArgumentException;

	/**
	 * Fetches Places data from DB for given Search city
	 */
	List<PlaceDBResult> fetchDBData(String searchCity) throws IllegalArgumentException;

	/**
	 * Saves the Places DB data to Database 
	 */
	String saveDBData(List<PlaceDBResult> dbResults) throws IllegalArgumentException;

	/**
	 * Updates user action on Places
	 */
	String updateValues(String placeID, String newRate, String mustSee) throws IllegalArgumentException;

	/**
	 * Removes Place from DB for further listing
	 */
	String removePlace(String placeID) throws IllegalArgumentException;
}

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

	List<PlaceDBResult> fetchDBData(String searchCity) throws IllegalArgumentException;

	String saveDBData(String searchCity, List<PlaceDBResult> dbResults) throws IllegalArgumentException;

	String updateValues(String searchCity, String placeID, String newRate, String mustSee) throws IllegalArgumentException;

	String removePlace(String searchCity, String placeID) throws IllegalArgumentException;
}

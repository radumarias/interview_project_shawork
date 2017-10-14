package com.redcloud.travelapp.server.service;

import java.util.List;

import com.google.inject.ImplementedBy;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * Places Service Interface Definitions
 * @author Sharadha
 *
 */
@ImplementedBy(PlacesServiceImpl.class)
public interface PlacesService {

	public List<PlaceDBResult> fetchDBData(String searchCity) throws IllegalArgumentException;
	public String saveDBData(String searchCity, List<PlaceDBResult> dbResults) throws IllegalArgumentException;
	public String updateValues(String searchCity, String placeID, String newRate, String mustSee)
			throws IllegalArgumentException;
	public String removePlace(String searchCity, String placeID) throws IllegalArgumentException;
}
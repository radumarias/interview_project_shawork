package com.redcloud.travelapp.server.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.redcloud.travelapp.server.db.PlacesDB;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * Places Services Implementation Module
 * @author Sharadha
 *
 */
public class PlacesServiceImpl implements PlacesService {

	@Inject
	private EntityManager entityManager;
	
	/**
	 * Fetches the Data of Places based on Search City
	 */
	@Override
	@Transactional
	public List<PlaceDBResult> fetchDBData(String searchCity) throws IllegalArgumentException {
		String query = "FROM places WHERE searchcity=:searchcity";
		List<PlacesDB> dbResults = (List<PlacesDB>)entityManager.createQuery(query)
				.setParameter("searchcity", searchCity).getResultList();
		List<PlaceDBResult> results = convertDBObjs(dbResults);
		return results;
	}

	/**
	 * Saves the data of Places Info per Search City
	 */
	@Override
	@Transactional
	public String saveDBData(String searchCity, List<PlaceDBResult> dbResults) throws IllegalArgumentException {
		List<PlacesDB> results = convertObjDBs(dbResults, searchCity);
		for(PlacesDB result : results) {
			entityManager.persist(result);
		}
		return "";
	}

	/**
	 * JPA - Update Values for given SearchCity - PlaceID
	 */
	@Override
	@Transactional
	public String updateValues(String searchCity, String placeID, String newRate, String mustSee)
			throws IllegalArgumentException {
		String query = "FROM places WHERE searchcity=:searchcity and idstr=:idstr";
		PlacesDB place = (PlacesDB) entityManager.createQuery(query).setParameter("searchcity", searchCity)
				.setParameter("idstr", placeID).getSingleResult();
		if(place != null) {
			place.setRating(newRate);
			place.setMustSee(mustSee);
			entityManager.persist(place);
			return "Success";
		} else {
			return null;
		}
	}

	/**
	 * JPA - Remove Place DB 
	 */
	@Override
	@Transactional
	public String removePlace(String searchCity, String placeID) throws IllegalArgumentException {
		String query = "FROM places WHERE searchcity=:searchcity and idstr=:idstr";
		PlacesDB place = (PlacesDB) entityManager.createQuery(query).setParameter("searchcity", searchCity)
				.setParameter("idstr", placeID).getSingleResult();
		if(place != null) {
			entityManager.remove(place);
			return "Success";
		} else {
			return null;
		}
	}
	
	/**
	 * Translation methods
	 */
	private List<PlaceDBResult> convertDBObjs(List<PlacesDB> inputs) {
		List<PlaceDBResult> outputs = new ArrayList<PlaceDBResult>();
		for(PlacesDB input : inputs) {
			PlaceDBResult output = new PlaceDBResult();
			output.setIcon(input.getIcon());
			output.setIdstr(input.getIdstr());
			output.setMustSee(input.getMustSee());
			output.setName(input.getName());
			output.setRating(input.getRating());
			output.setReviewStr(input.getReviewStr());
			output.setSearchcity(input.getSearchcity());
			output.setWebsite(input.getWebsite());
			outputs.add(output);
		}
		return outputs;
	}
	
	/**
	 * Conversion methods
	 */
	private List<PlacesDB> convertObjDBs(List<PlaceDBResult> inputs, String searchCity) {
		List<PlacesDB> outputs = new ArrayList<PlacesDB>();
		for(PlaceDBResult input : inputs) {
			PlacesDB output = new PlacesDB();
			output.setIcon(input.getIcon());
			output.setIdstr(input.getIdstr());
			output.setMustSee(input.getMustSee());
			output.setName(input.getName());
			output.setRating(input.getRating());
			output.setReviewStr(input.getReviewStr());
			output.setSearchcity(searchCity);
			output.setWebsite(input.getWebsite());
			outputs.add(output);
		}
		return outputs;
	}
}
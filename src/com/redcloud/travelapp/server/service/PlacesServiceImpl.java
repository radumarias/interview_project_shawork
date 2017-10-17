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
	
	@Inject
    public PlacesServiceImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
    }
	
	/**
	 * Fetches the DB data of Places based on Search city
	 */
	@Override
	@Transactional
	public List<PlaceDBResult> fetchDBData(String searchCity) throws IllegalArgumentException {
		try {
			searchCity = searchCity.replaceAll(",", " ");
		} catch(Exception e) {
			
		}
		try {
			String query = "FROM PlacesDB WHERE searchcity=:searchcity";
			List<PlacesDB> dbResults = (List<PlacesDB>)entityManager.createQuery(query)
					.setParameter("searchcity", searchCity).getResultList();
			List<PlaceDBResult> results = convertDBObjs(dbResults);
			return results;
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Saves to DB the data of the Places
	 */
	@Override
	@Transactional
	public String saveDBData(List<PlaceDBResult> dbResults) throws IllegalArgumentException {
		List<PlacesDB> results = convertObjDBs(dbResults);
		entityManager.getTransaction().begin();
		for(PlacesDB result : results) {
			try {
				entityManager.persist(result);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		entityManager.flush();
		entityManager.getTransaction().commit();
		return "";
	}

	/**
	 * Updates the value for the Place
	 */
	@Override
	@Transactional
	public String updateValues(//String searchCity, 
			String placeID, String newRate, String mustSee)
			throws IllegalArgumentException {
		/*
		try {
			searchCity = searchCity.replaceAll(",", " ");
		} catch(Exception e) {}*/
		
		String query = //"FROM PlacesDB WHERE searchcity=:searchcity and idstr=:idstr";
				"FROM PlacesDB WHERE idstr=:idstr";
		PlacesDB place = (PlacesDB) entityManager.createQuery(query)//.setParameter("searchcity", searchCity)
				.setParameter("idstr", placeID).getSingleResult();
		if(place != null) {
			place.setRating(newRate);
			place.setMustSee(mustSee);
			entityManager.getTransaction().begin();
			try {
				entityManager.persist(place);
			} catch(Exception e) {
				return null;
			}
			entityManager.flush();
			entityManager.getTransaction().commit();
			return "Success";
		} else {
			return null;
		}
	}

	
	/**
	 * Users wants to remove Place from List
	 */
	@Override
	@Transactional
	public String removePlace(//String searchCity, 
			String placeID) throws IllegalArgumentException {
		/*
		try {
			searchCity = searchCity.replaceAll(",", " ");
		} catch(Exception e) {}*/
		
		String query = //"FROM PlacesDB WHERE searchcity=:searchcity and idstr=:idstr";
				"FROM PlacesDB WHERE idstr=:idstr";
		PlacesDB place = (PlacesDB) entityManager.createQuery(query)//.setParameter("searchcity", searchCity)
				.setParameter("idstr", placeID).getSingleResult();
		if(place != null) {
			entityManager.getTransaction().begin();
			try {
				entityManager.remove(place);
			} catch(Exception e) {
				return null;
			}
			entityManager.flush();
			entityManager.getTransaction().commit();
			return "Success";
		} else {
			return null;
		}
	}
	
	/**
	 * Converts db pojos to objs
	 * @param inputs
	 * @return List of PlaceDBResults
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
	 * Converts objs to db pojos
	 * @param inputs
	 * @return List of PlacesDB
	 */
	private List<PlacesDB> convertObjDBs(List<PlaceDBResult> inputs) {
		List<PlacesDB> outputs = new ArrayList<PlacesDB>();
		for(PlaceDBResult input : inputs) {
			PlacesDB output = new PlacesDB();
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
}
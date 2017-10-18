package com.redcloud.travelapp.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
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
	private GeoApiContext context;
	
	public TravelAppServiceImpl() {
		super();
		Injector injector = Guice.createInjector(new GuiceDBModule());
		placesServiceImpl = injector.getInstance(PlacesServiceImpl.class);
		context = new GeoApiContext.Builder()
			    .apiKey("AIzaSyD8FBX-lagxh6nHtoNk3sPdfUMRVL7Tp9E")
			    .build();
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

	/**
	 * Retrieves Places Info from 
	 * Google Places API
	 * return List<PlaceDBResult>
	 */
	@Override
	public List<PlaceDBResult> getPlacesFromApi(String searchCity, double latitude, double longitude) {
		com.google.maps.model.LatLng latlng = new com.google.maps.model.LatLng(latitude, longitude);
		List<PlaceDBResult> places = new ArrayList<PlaceDBResult>();
		try {
			PlacesSearchResponse req =
			          PlacesApi.nearbySearchQuery(context,latlng)
			              .radius(5000).type(PlaceType.ESTABLISHMENT, PlaceType.AMUSEMENT_PARK, 
			            		  PlaceType.AQUARIUM, PlaceType.CASINO,
			            		  PlaceType.CITY_HALL, PlaceType.ESTABLISHMENT, 
			            		  PlaceType.MUSEUM,PlaceType.ZOO).await();
			PlacesSearchResult results[] = req.results;
			if(results != null)
			for(PlacesSearchResult result : results) {
				try {
					PlaceDBResult dbResult = new PlaceDBResult();
					dbResult.setIcon(result.icon.toString());
					dbResult.setIdstr(result.placeId);
					dbResult.setMustSee("N");
					dbResult.setName(result.name);
					dbResult.setRating(result.rating + "");
					dbResult.setReviewStr("");
					dbResult.setWebsite("");
					dbResult.setAddress(result.vicinity);//result.formattedAddress);
					if(result.photos != null && result.photos.length > 0)
						dbResult.setPhotosURL(result.photos[0].photoReference);
					else 
						dbResult.setPhotosURL("--no photo--");
					try {
						dbResult.setMyPhotosURL2(result.photos[1].photoReference);
					} catch(Exception e) {
						e.printStackTrace();
					}
					dbResult.setSearchcity(searchCity);
					
					StringBuffer typeBuffer = new StringBuffer();
					if(result.types != null) {
						for(String type : result.types) {
							typeBuffer.append(type.toUpperCase()).append(" and ");
						}
					}
					dbResult.setType(typeBuffer.toString());
					places.add(dbResult);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
		} catch (ApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return places;
	}
}
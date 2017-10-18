package com.redcloud.travelapp.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.placeslib.Autocomplete;
import com.google.gwt.maps.client.placeslib.AutocompleteOptions;
import com.google.gwt.maps.client.placeslib.AutocompleteType;
import com.google.gwt.maps.client.placeslib.PlaceDetailsHandler;
import com.google.gwt.maps.client.placeslib.PlaceDetailsRequest;
import com.google.gwt.maps.client.placeslib.PlaceResult;
import com.google.gwt.maps.client.placeslib.PlaceSearchHandler;
import com.google.gwt.maps.client.placeslib.PlaceSearchPagination;
import com.google.gwt.maps.client.placeslib.PlaceSearchRequest;
import com.google.gwt.maps.client.placeslib.PlacesService;
import com.google.gwt.maps.client.placeslib.PlacesServiceStatus;
import com.google.gwt.maps.client.services.Geocoder;
import com.google.gwt.maps.client.services.GeocoderAddressComponent;
import com.google.gwt.maps.client.services.GeocoderGeometry;
import com.google.gwt.maps.client.services.GeocoderRequest;
import com.google.gwt.maps.client.services.GeocoderRequestHandler;
import com.google.gwt.maps.client.services.GeocoderResult;
import com.google.gwt.maps.client.services.GeocoderStatus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.redcloud.travelapp.client.widgets.PlaceTile;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * TravelApp EntryPoint Class
 * 
 * @author SHARADHA
 *
 */
public class TravelApp implements EntryPoint {

	private TextBox mySearchBox;
	private VerticalPanel myDisplayPanel;
    private ListBox myFilterDropBox;
    private List<PlaceTile> myValidTiles;
    
	/**
	 * Remote Service Proxy
	 */
	private final TravelAppServiceAsync myTravelAppService = GWT.create(TravelAppService.class);

	//private TravelMap myMap;
	/**
	 * The main method of execution On Load of Module
	 */
	public void onModuleLoad() {
		
		VerticalPanel topPanel = createPanel();
		myDisplayPanel = new VerticalPanel();
		myValidTiles = new ArrayList<PlaceTile>();
	
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setSpacing(10);
		mainPanel.add(myDisplayPanel);
		topPanel.add(mainPanel);
		topPanel.setSpacing(10);
		RootPanel.get().add(topPanel);
	}

	
	/**
	 * Creates Main Panel
	 * @return
	 */
	private VerticalPanel createPanel() {

		VerticalPanel vPanel = new VerticalPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setSpacing(5);
		hPanel.add(new Label("Enter City : "));

		mySearchBox = new TextBox();
		boolean sensor = false;
		/**
		 * AutoComplete Search Text functionality
		 */
		ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
		loadLibraries.add(LoadLibrary.PLACES);
		LoadApi.go(new Runnable() {
			public void run() {
				AutocompleteType[] types = new AutocompleteType[1];
				types[0] = AutocompleteType.ESTABLISHMENT;
				types[1] = AutocompleteType.GEOCODE;

				AutocompleteOptions options = AutocompleteOptions.newInstance();
				options.setTypes(types);

				Element e = mySearchBox.getElement();
				Autocomplete o = Autocomplete.newInstance(e, options);

			}
		}, loadLibraries, sensor);

		hPanel.add(mySearchBox);
		Button searchButton = new Button("Near By Places");
		hPanel.add(searchButton);
		
		// Filters Section
		myFilterDropBox = new ListBox(false);
	    List<String> listTypes = getPlaceTypeCategories();
	    for (String listType : listTypes) {
	      myFilterDropBox.addItem(listType);
	    }
	    hPanel.add(new Label("Place Type : "));
	    hPanel.add(myFilterDropBox);
	    Button filterButton = new Button("Apply Filter");
		hPanel.add(filterButton);
		vPanel.add(hPanel);

		// Listeners Module
		mySearchBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					displayPlacesInfo(mySearchBox.getText());
				}
			}
		});
		searchButton.addClickListener(new ClickListener() {
			@Override
			public void onClick(Widget sender) {
				displayPlacesInfo(mySearchBox.getText());
			}
		});
		filterButton.addClickListener(new ClickListener() {
			@Override
			public void onClick(Widget sender) {
				applyMainFilter();
			}
		});
		return vPanel;
	}

	/**
	 * Displays Places info
	 * @param searchCityStr
	 */
	private void displayPlacesInfo(final String searchCityStr) {
		try {
			myDisplayPanel.clear();
			myValidTiles.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// RETRIEVES DATA IF PRESENT IN DB
		myTravelAppService.fetchDBData(searchCityStr, new AsyncCallback<List<PlaceDBResult>>() {
			public void onFailure(Throwable caught) {
				GWT.log("Data not present in DB, so fetching from API..");
				fetchFromApi(searchCityStr);
				applyMainFilter();
			}

			public void onSuccess(List<PlaceDBResult> results) {
				GWT.log("Success fetching Data from DB..");
				if (results != null && results.size() > 0) {
					for (int i = 0; i < results.size(); i++) {
						addToDisplay(results.get(i), searchCityStr);
					}
				} else {
					GWT.log("No results found in DB, fetching from API..");
					fetchFromApi(searchCityStr);
				}
				applyMainFilter();
			}
		});
	}

	/**
	 * Fetches From API
	 * @param searchCity
	 */
	private void fetchFromApi(final String searchCity) {
		Geocoder o = Geocoder.newInstance();
		GeocoderRequest request = GeocoderRequest.newInstance();
		request.setAddress(searchCity);
		o.geocode(request, new GeocoderRequestHandler() {
			@Override
			public void onCallback(JsArray<GeocoderResult> results, GeocoderStatus status) {
				if (status == GeocoderStatus.OK) {
					if (results.length() > 0) {
					}
					for (int i = 0; i < results.length(); i++) {
						GeocoderResult result = results.get(i);
						JsArray<GeocoderAddressComponent> components = result.getAddress_Components();
						GeocoderGeometry geo = result.getGeometry();
						
						// Fetches Places Info from API
						myTravelAppService.getPlacesFromApi(searchCity, geo.getLocation().getLatitude(), geo.getLocation().getLongitude(), 
								new AsyncCallback<List<PlaceDBResult>>() {
							public void onFailure(Throwable caught) {
								GWT.log("Requesting Places from API of Places Search..");
							}
							@Override
							public void onSuccess(List<PlaceDBResult> results) {
								if(results != null && results.size() > 0) {
									try {
										for(PlaceDBResult result : results) {
											addToDisplay(result, searchCity);
										}
									} catch(Exception e) {
										e.printStackTrace();
									}
								}
								
								// Persist to DB - creation
								GWT.log("Persisting to DB");
								myTravelAppService.saveDBData(results, new AsyncCallback<String>() {
									public void onFailure(Throwable caught) {
										GWT.log("Error saving to DB");
									}
									public void onSuccess(String str) {
										GWT.log("success saving to DB");
									}
								});

							}});
						//searchRequest(geo.getLocation(), searchCity);
					}
				}
			}
		});
	}

	/**
	 * GUI Display Code
	 * @param result
	 * @param searchCityStr
	 */
	private void addToDisplay(PlaceDBResult result, final String searchCityStr) {
		
		VerticalPanel vpanel = new VerticalPanel();
		PlaceTile tile = new PlaceTile(result);
		vpanel.add(tile);
		myValidTiles.add(tile);
		myDisplayPanel.add(vpanel);

		final PlaceDBResult presult = result;
		tile.addClickListener(new ClickListener() {
			@Override
			public void onClick(Widget sender) {
				onPlaceClick(presult, searchCityStr);
			}
		});
		
	}
	/**
	 * On Clicking Place Tile
	 * @param searchCityStr
	 * @param placeid
	 * @param pName
	 * @param rating
	 * @param reviewCnt
	 */
	private void onPlaceClick(final PlaceDBResult presult, final String searchCityStr) {

		final String placeid = presult.getIdstr();
		String pName = presult.getName();
		String rating = presult.getRating();
		//String reviewCntStr = presult.getReviewStr();
		
		final DialogBox dialog = new DialogBox();
		VerticalPanel vdPanel = new VerticalPanel();
		vdPanel.add(new Label(pName));
		
		String url = getAPIURL(presult.getMyPhotosURL2() == null ? 
				presult.getPhotosURL() : presult.getMyPhotosURL2());
		String description = presult.getAddress();
		String html = 
			"<div><center>"
				+ "<img src = '" + url + "'  height=\"150\" width=\"300\"></img>"
				//+ "</br><p>" + description + "</p></br>"
			+ "</center></div>";
		
		vdPanel.add(new HTML(html));
		vdPanel.add(new HTML(description));
		HorizontalPanel hRatePanel = new HorizontalPanel();
		hRatePanel.add(new Label("Ratings : "));
		final TextBox rateBox = new TextBox();
		rateBox.setText(rating + "");
		hRatePanel.add(rateBox);
		vdPanel.add(hRatePanel);

		HorizontalPanel mustSeePanel = new HorizontalPanel();
		final CheckBox cb = new CheckBox("Must See");
		cb.setValue(presult.getMustSee() != null && presult.getMustSee().equalsIgnoreCase("Y"));
		mustSeePanel.add(cb);
		vdPanel.add(mustSeePanel);

		dialog.setModal(true);
		dialog.setText("PLACE DETAILS");
		dialog.setAnimationEnabled(true);
		dialog.setGlassEnabled(true);
		
		// Important Buttons & Functionalities
		Button closeButton = new Button("Close");
		closeButton.addClickListener(new ClickListener() {
			@Override
			public void onClick(Widget sender) {
				dialog.hide();
			}
		});
		
		Button saveButton = new Button("Save");
		saveButton.addClickListener(new ClickListener() {
			@Override
			public void onClick(Widget sender) {
				String newRate = rateBox.getValue();
				boolean mustSee = cb.isChecked();
				String pid = placeid;
				String mustSeeStr = mustSee ? "Y" : "N";
				myTravelAppService.updateValues(pid, newRate, mustSeeStr, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert("Error while Updating values..");
					}
					public void onSuccess(String str) {
						dialog.hide();
						Window.alert("Success Saving Values for the Place.");
						displayPlacesInfo(searchCityStr);
					}
				});
				
			}
		});

		Button removeButton = new Button("Remove");
		removeButton.addClickListener(new ClickListener() {
			@Override
			public void onClick(Widget sender) {
				String pid = placeid;
				//removePlace(searchCityStr, pid);
				myTravelAppService.removePlace(pid, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						Window.alert("Error while Removing Place..");
					}
					public void onSuccess(String str) {
						dialog.hide();
						Window.alert("Success Removing Place from List.");
						displayPlacesInfo(searchCityStr);
					}
				});
			}
		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(8);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.add(saveButton);
		buttonPanel.add(removeButton);
		buttonPanel.add(closeButton);
		vdPanel.setSpacing(6);
		vdPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vdPanel.add(buttonPanel);
		dialog.add(vdPanel);
		dialog.show();
		dialog.center();
	}
	
	/**
	 * Available Place Types
	 * @return
	 */
	private List<String> getPlaceTypeCategories() {
		ArrayList<String> placeTypes = new ArrayList<String>();
		placeTypes.add("ALL".toString().toUpperCase());
		placeTypes.add("RV_PARK"); 
		placeTypes.add("CAMPGROUND");
		placeTypes.add("PARK");
		placeTypes.add("LODGING"); 
		placeTypes.add("POINT_OF_INTEREST"); 
		placeTypes.add("SPA"); 
		placeTypes.add("PREMISE"); 
		placeTypes.add("SCHOOL"); 
		placeTypes.add("HEALTH"); 
		placeTypes.add("STORE"); 
		placeTypes.add("BAR"); 
		placeTypes.add("REAL_ESTATE_AGENCY"); 
		placeTypes.add("UNIVERSITY"); 
		placeTypes.add("RESTAURANT"); 
		placeTypes.add("FOOD"); 
		//placeTypes.add("AMUSEMENT_PARK".toString().toUpperCase()); 
		//placeTypes.add("AQUARIUM".toString().toUpperCase()); 
		//placeTypes.add("CASINO".toString().toUpperCase());
		//placeTypes.add("CITY_HALL".toString().toUpperCase()); 
		placeTypes.add("ESTABLISHMENT".toString().toUpperCase()); 
		//placeTypes.add("MUSEUM".toString().toUpperCase());
		//placeTypes.add("ZOO".toString().toUpperCase());
		return placeTypes;
	}

	/**
	 * Returns API URL of Photo
	 * @param photoReference
	 * @return String
	 */
	private String getAPIURL(String photoReference) {
		String apiURL = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="
				+ (photoReference != null ? photoReference.trim() : "")
				+ "&key=AIzaSyD8FBX-lagxh6nHtoNk3sPdfUMRVL7Tp9E";
		return apiURL;
	}	
	
	/**
	 * Applies Place Filter per tile
	 * @param filterText
	 */
	private void applyMainFilter() {
		try {
			String filterText = myFilterDropBox.getSelectedItemText();
			for(PlaceTile tile : myValidTiles) {
				try {
					tile.setVisible(tile.getPlaceType().contains(filterText) || 
							filterText.equalsIgnoreCase("ALL"));
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/////////////////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Convert to DB instance
	 * @param result
	 * @return
	 */
	private PlaceDBResult convertToDBR(PlaceResult result, String searchString) {
		PlaceDBResult pdrb = new PlaceDBResult();
		try {
			pdrb.setIdstr(result.getId());
			pdrb.setIcon(result.getIcon() == null ? "" : result.getIcon().trim());
			pdrb.setMustSee("N");
			pdrb.setName(result.getName() == null ? "" : result.getName().trim());
			pdrb.setRating(result.getRating() + "");
			pdrb.setReviewStr(result.getReviews() == null ? "(Reviews : " + "0" + ") "
					: "(Reviews : " + result.getReviews().length() + ") ");
			pdrb.setWebsite(result.getWebsite() == null ? "" : result.getWebsite().trim());
			pdrb.setSearchcity(searchString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdrb;
	}
	
	/**
	 * Search Request
	 * @param clickLocation
	 * @param searchCityStr
	 */
	private void searchRequest(final LatLng clickLocation, final String searchCityStr) {
		String[] types = new String[1];
		types[0] = "point_of_interest";
		types[1] = "establishment";

		PlaceSearchRequest request = PlaceSearchRequest.newInstance();
		request.setLocation(clickLocation);
		request.setRadius(5000d);
		request.setTypes(types);

		PlacesService placeService = PlacesService.newInstance(mySearchBox.getElement());// mapWidget);
		placeService.nearbySearch(request, new PlaceSearchHandler() {

			@Override
			public void onCallback(JsArray<PlaceResult> results, PlaceSearchPagination pagination,
					PlacesServiceStatus status) {

				if (status == PlacesServiceStatus.OK) {
					if (results.length() > 0) {
						List<PlaceDBResult> dbResults = new ArrayList<PlaceDBResult>();
						for (int i = 0; i < results.length(); i++) {
							PlaceResult result = results.get(i);
							String uniqueId = System.currentTimeMillis() + "_" + i;
							if (result.getId() == null) {
								result.setId(uniqueId);
							}
							String reference = result.getReference();
							getPlaceDetails(reference, searchCityStr, clickLocation);

							String json = new JSONObject(result).toString();
							GWT.log("details=" + json);
							try {
								dbResults.add(convertToDBR(result,searchCityStr));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						// Persist to DB - creation
						GWT.log("Persisting to DB");
						myTravelAppService.saveDBData(dbResults, new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								GWT.log("Error saving to DB");
							}

							public void onSuccess(String str) {
								GWT.log("success saving to DB");
							}
						});

					}
				} else {
				}
			}
		});
	}
	
	/**
	 * Gets the Map Place's Details
	 * @param reference
	 */
	private void getMapPlaceDetails(String reference) {
		if (reference == null || reference.isEmpty()) {
			return;
		}
	}
	
	/**
	 * Fetches Places Details
	 * @param reference
	 * @param searchCityStr
	 */
	private void getPlaceDetails(String reference, final String searchCityStr, final LatLng location) {
		if (reference == null || reference.isEmpty()) {
			return;
		}
		
		PlacesService placeService = PlacesService.newInstance(mySearchBox.getElement());// mapWidget);
		PlaceDetailsRequest request = PlaceDetailsRequest.newInstance();
		request.setReference(reference);

		placeService.getDetails(request, new PlaceDetailsHandler() {
			@Override
			public void onCallback(PlaceResult result, PlacesServiceStatus status) {
				if (status == PlacesServiceStatus.OK) {
					String resultStr = result.getName() + "<br/>" + "<a href=\"" + result.getWebsite()
							+ "\" /> Website </a><br/>"
					// result.getIcon() +
							+ "(Reviews : " + result.getReviews().length() + ") " + result.getRating();

					VerticalPanel vpanel = new VerticalPanel();
					String imageIcon = result.getIcon();

					Button button = new Button();
					String url = imageIcon;
					String html = "<div><center><img src = '" + url + "'></img></center><label>" + resultStr
							+ "</label></br></div>";

					button.setHTML(html);
					vpanel.add(button);
					button.setSize("400px", "200px");

					final PlaceResult presult = result;
					button.addClickListener(new ClickListener() {

						@Override
						public void onClick(Widget sender) {
							/*onPlaceClick(searchCityStr, presult.getId(), 
									presult.getName(), presult.getRating()+"", 
									(presult.getReviews() == null 
										? 0 : presult.getReviews().length()) + "");*/
						}
					});
					myDisplayPanel.add(vpanel);

				} else {
					String json = new JSONObject(result).toString();
					GWT.log("details=" + json);
				}
			}
		});
	}
}
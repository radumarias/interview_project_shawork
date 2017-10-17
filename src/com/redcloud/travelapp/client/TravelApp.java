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
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.redcloud.travelapp.shared.PlaceDBResult;

/**
 * TravelApp EntryPoint Class
 * 
 * @author SHARADHA
 *
 */
public class TravelApp implements EntryPoint {

	TextBox mySearchBox;
	private Autocomplete mySearchCity;
	private VerticalPanel myDisplayPanel;

	/**
	 * Remote Service Proxy
	 */
	private final TravelAppServiceAsync myTravelAppService = GWT.create(TravelAppService.class);

	/**
	 * The main method of execution On Load of Module
	 */
	public void onModuleLoad() {
		VerticalPanel topPanel = createPanel();
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.setSpacing(10);
		myDisplayPanel = new VerticalPanel();
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
		vPanel.add(hPanel);

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

		myDisplayPanel = new VerticalPanel();
		return vPanel;

	}

	/**
	 * Displays Places info
	 * @param searchCityStr
	 */
	private void displayPlacesInfo(final String searchCityStr) {
		try {
			myDisplayPanel.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// RETRIEVES DATA IF PRESENT IN DB
		myTravelAppService.fetchDBData(searchCityStr, new AsyncCallback<List<PlaceDBResult>>() {
			public void onFailure(Throwable caught) {
				fetchFromApi(searchCityStr);
			}

			public void onSuccess(List<PlaceDBResult> results) {
				{
					if (results != null && results.size() > 0) {
						for (int i = 0; i < results.size(); i++) {
							PlaceDBResult result = (PlaceDBResult) results.get(i);
							String resultStr = result.getName() + "<br/>" + "<a href=\"" + result.getWebsite()
									+ "\" /> Website </a><br/>"
							// result.getIcon() +
							//		+ "(Reviews : " + result.getReviewStr() + ") " 
							+ result.getReviewStr() + " " + result.getRating();

							VerticalPanel vpanel = new VerticalPanel();
							String imageIcon = result.getIcon();

							Button button = new Button();
							String url = imageIcon;
							String html = "<div><center><img src = '" + url + "'></img></center><label>" + resultStr
									+ "</label></br></div>";

							button.setHTML(html);
							vpanel.add(button);
							button.setSize("400px", "200px");

							final PlaceDBResult presult = result;
							button.addClickListener(new ClickListener() {

								@Override
								public void onClick(Widget sender) {
									onPlaceClick(searchCityStr, presult.getIdstr(), 
											presult.getName(), presult.getRating(), 
											presult.getReviewStr());
								}
							});
							myDisplayPanel.add(vpanel);

						}
					} else {
						fetchFromApi(searchCityStr);
					}

				}
			}
		});
	}

	/**
	 * Updates Respective Values
	 * @param searchCity
	 * @param placeID
	 * @param newRate
	 * @param mustSee
	 */
	private void updateValues(final String searchCity, String placeID, String newRate, boolean mustSee) {
		String mustSeeStr = mustSee ? "Y" : "N";
		myTravelAppService.updateValues(placeID, newRate, mustSeeStr, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				//Window.alert("Error while Updating values..");
			}
			public void onSuccess(String str) {
				Window.alert("Success Saving Values");
				displayPlacesInfo(searchCity);
			}
		});
	}

	/**
	 * Removes Places
	 * @param searchCity
	 * @param placeID
	 */
	private void removePlace(final String searchCity, String placeID) {
		myTravelAppService.removePlace(placeID, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				//Window.alert("Error while Removing Place..");
			}
			public void onSuccess(String str) {
				Window.alert("Success Removing Place");
				displayPlacesInfo(searchCity);
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
						int len = components.length();
						String address = result.getFormatted_Address();
						GeocoderGeometry geo = result.getGeometry();
						searchRequest(geo.getLocation(), searchCity);
					}
				}
			}
		});
	}

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
	private void searchRequest(LatLng clickLocation, final String searchCityStr) {
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
							getPlaceDetails(reference, searchCityStr);

							String json = new JSONObject(result).toString();
							GWT.log("details=" + json);
							try {
								dbResults.add(convertToDBR(result,searchCityStr));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						// Persist to DB - creation
						System.out.println("Persisting to DB");
						myTravelAppService.saveDBData(dbResults, new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								System.out.println("Error saving to DB");
							}

							public void onSuccess(String str) {
								System.out.println("success saving to DB");
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
	private void getPlaceDetails(String reference, final String searchCityStr) {
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
							onPlaceClick(searchCityStr, presult.getId(), 
									presult.getName(), presult.getRating()+"", 
									(presult.getReviews() == null 
										? 0 : presult.getReviews().length()) + "");
						}
					});
					myDisplayPanel.add(vpanel);

				} else {
					String json = new JSONObject(result).toString();
					System.out.println("details=" + json);
				}
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
	private void onPlaceClick(final String searchCityStr, final String placeid, 
			String pName, String rating, String reviewCntStr) {

		// TODO Auto-generated method stub
		final DialogBox dialog = new DialogBox();
		VerticalPanel vdPanel = new VerticalPanel();
		vdPanel.add(new Label(pName));
		vdPanel.add(new Label("(Reviews : " + reviewCntStr + ") "));

		HorizontalPanel hRatePanel = new HorizontalPanel();
		hRatePanel.add(new Label("Ratings : "));
		final TextBox rateBox = new TextBox();
		rateBox.setText(rating + "");
		hRatePanel.add(rateBox);
		vdPanel.add(hRatePanel);

		HorizontalPanel mustSeePanel = new HorizontalPanel();
		final CheckBox cb = new CheckBox("Must See");
		cb.setValue(false);
		mustSeePanel.add(cb);
		vdPanel.add(mustSeePanel);

		Button closeButton = new Button("Close");
		closeButton.addClickListener(new ClickListener() {

			@Override
			public void onClick(Widget sender) {
				// TODO Auto-generated method stub
				dialog.hide();
			}
		});

		Button saveButton = new Button("Save");
		saveButton.addClickListener(new ClickListener() {

			@Override
			public void onClick(Widget sender) {
				// TODO Auto-generated method stub
				String newRate = rateBox.getValue();
				boolean mustSee = cb.isChecked();
				// Save these values in DB
				String pid = placeid;
				updateValues(searchCityStr, pid, newRate, mustSee);
			}
		});
		Button removeButton = new Button("Remove");
		removeButton.addClickListener(new ClickListener() {

			@Override
			public void onClick(Widget sender) {
				// TODO Auto-generated method stub
				// Remove this entry from DB
				String pid = placeid;
				removePlace(searchCityStr, pid);
			}
		});

		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setSpacing(2);
		buttonPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		buttonPanel.add(saveButton);
		buttonPanel.add(closeButton);
		buttonPanel.add(removeButton);
		vdPanel.setSpacing(2);
		vdPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vdPanel.add(buttonPanel);
		dialog.add(vdPanel);
		dialog.show();
		dialog.center();
	
	}
}
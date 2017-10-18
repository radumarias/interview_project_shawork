package com.redcloud.travelapp.client.widgets;

import com.google.gwt.user.client.ui.Button;
import com.redcloud.travelapp.shared.PlaceDBResult;

	/**
	 * Customized Place Tile Display
	 * @author Sharadha
	 */
	public class PlaceTile extends Button {
		private PlaceDBResult result;
		public PlaceTile(PlaceDBResult dbresult) {
			super();
			result = dbresult;
			createUI();
		}
		private void createUI() {
			String resultStr = result.getName() ;
				//	+ "<br/> Rating : " + result.getRating();
			String url = getAPIURL(result.getPhotosURL());//result.getIcon();
			String html = "<div><center>"
					+ "<img src = '" + url + "'  height=\"300\" width=\"654\"></img></center>"
					+ "<label>" + resultStr + "</label></br></div>";
			setHTML(html);
			setSize("662px", "360px");
		}
		public String getPlaceType() {
			return result.getType().toUpperCase();
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
		
	}
	
	
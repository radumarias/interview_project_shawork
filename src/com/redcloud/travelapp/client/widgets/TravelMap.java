package com.redcloud.travelapp.client.widgets;

import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Map Widget
 * @author Sharadha
 *
 */
public class TravelMap extends Composite {

	private MapWidget myMap;
	
	public TravelMap() {
		VerticalPanel vp = new VerticalPanel();
		MapOptions options = MapOptions.newInstance(true);
		LatLng latlng = LatLng.newInstance(37.4419, -122.1419);
		myMap = new MapWidget(options);
		myMap.setCenter(latlng);
		myMap.setSize("520px", "820px");  
		vp.add(myMap.asWidget());
		vp.setSpacing(10);
		myMap.setSize("500px", "800px");
		initWidget(vp);
	}
}
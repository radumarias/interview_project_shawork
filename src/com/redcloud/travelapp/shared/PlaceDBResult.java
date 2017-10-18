package com.redcloud.travelapp.shared;

import java.io.Serializable;
/**
 * Serializable PlacesDB pojo
 * @author Sharadha
 */
public class PlaceDBResult implements Serializable
{
	private static final long serialVersionUID = 1L;
	// Important Attributes of the Places Model
	private String idstr;
	private String name;
	private String myPhotosURL;
	private String myPhotosURL2;
	private String type;
	private String rating;
	private String mustSee;
	private String myAddress;
	private String searchcity;
	
	// Not Mandatory
	private String website;
	private String icon;
	private String reviewStr;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getReviewStr() {
		return reviewStr;
	}
	public void setReviewStr(String reviewStr) {
		this.reviewStr = reviewStr;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getMustSee() {
		return mustSee;
	}
	public void setMustSee(String mustSee) {
		this.mustSee = mustSee;
	}
	/**
	 * @return the id
	 */
	public String getIdstr() {
		return idstr;
	}
	/**
	 * @param id the id to set
	 */
	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	/**
	 * @return the searchcity
	 */
	public String getSearchcity() {
		return searchcity;
	}
	/**
	 * @param searchcity the searchcity to set
	 */
	public void setSearchcity(String searchcity) {
		try {
			searchcity = searchcity.replaceAll(",", " ");
		} catch(Exception e) {}
		this.searchcity = searchcity;
	}
	
	public void setAddress(String address) {
		myAddress = address;
	}
	public void setPhotosURL(String photosURL) {
		myPhotosURL = photosURL;
	}
	public String getAddress() {
		return myAddress;
	}
	public String getPhotosURL() {
		return myPhotosURL;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMyPhotosURL2() {
		return myPhotosURL2;
	}
	public void setMyPhotosURL2(String myPhotosURL2) {
		this.myPhotosURL2 = myPhotosURL2;
	}

}
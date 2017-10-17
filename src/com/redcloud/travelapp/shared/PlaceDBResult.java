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
	private String website;
	private String icon;
	private String reviewStr;
	private String rating;
	private String mustSee;
	private String searchcity;
	
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

}

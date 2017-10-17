package com.redcloud.travelapp.server.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Places DB - Model DB
 * @author Sharadha
 *
 */
@Entity
@Table(name = "places")
public class PlacesDB {
	private Integer id;
	private String userName;
	private String password;
	private String idstr;
	private String name;
	private String website;
	private String icon;
	private String reviewStr;
	private String rating;
	private String mustSee;
	private String searchcity;
	
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "website")
	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}
	@Column(name = "icon")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	@Column(name = "reviewstr")
	public String getReviewStr() {
		return reviewStr;
	}

	public void setReviewStr(String reviewStr) {
		this.reviewStr = reviewStr;
	}
	@Column(name = "rating")
	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}
	@Column(name = "mustsee")
	public String getMustSee() {
		return mustSee;
	}

	public void setMustSee(String mustSee) {
		this.mustSee = mustSee;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "username")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "password")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the idstr
	 */
	@Column(name = "idstr")
	public String getIdstr() {
		return idstr;
	}

	/**
	 * @param idstr the idstr to set
	 */
	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	
	@Column(name = "searchcity")
	public String getSearchcity() {
		return searchcity;
	}

	/**
	 * @param idstr the idstr to set
	 */
	public void setSearchcity(String searchcity) {
		try {
			searchcity = searchcity.replaceAll(",", " ");
		} catch(Exception e) {}
		this.searchcity = searchcity;
	}

}
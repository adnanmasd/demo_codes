package com.madeinw.search.business;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "country", catalog = "madeinw")
public class Country implements java.io.Serializable, Comparable<Country> {

	private static final long serialVersionUID = -1603155598705043663L;
	private Integer countryId;
	private String countryCode;
	private Integer continentId;
	private String URLFriendlyCountryName;
	private String callingCode;

	// Constructors

	/** default constructor */
	public Country() {
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "CountryID", unique = true, nullable = false)
	public Integer getCountryId() {
		return this.countryId;
	}

	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}

	@Column(name = "CountryCode")
	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Column(name = "ContinentID")
	public Integer getContinentId() {
		return this.continentId;
	}

	public void setContinentId(Integer continentId) {
		this.continentId = continentId;
	}

	@Column(name = "URLFriendlyCountryName", nullable = false, length = 60)
	public String getURLFriendlyCountryName() {
		return this.URLFriendlyCountryName;
	}

	public void setURLFriendlyCountryName(String countryName) {
		this.URLFriendlyCountryName = countryName;
	}

	@Override
	public int compareTo(Country o) {
		return this.URLFriendlyCountryName.compareTo(o.URLFriendlyCountryName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((continentId == null) ? 0 : continentId.hashCode());
		result = prime * result
				+ ((countryId == null) ? 0 : countryId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Country other = (Country) obj;
		if (continentId == null) {
			if (other.continentId != null)
				return false;
		} else if (!continentId.equals(other.continentId))
			return false;
		if (countryId == null) {
			if (other.countryId != null)
				return false;
		} else if (!countryId.equals(other.countryId))
			return false;
		return true;
	}

	@Column(name = "CallingCode")
	public String getCallingCode() {
		return callingCode;
	}

	public void setCallingCode(String callingCode) {
		this.callingCode = callingCode;
	}

}
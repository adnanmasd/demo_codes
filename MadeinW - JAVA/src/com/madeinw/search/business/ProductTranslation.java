package com.madeinw.search.business;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product_translation", catalog = "madeinw")
@Access(AccessType.FIELD)
public class ProductTranslation implements Serializable {
	private static final long serialVersionUID = -7234619151988588605L;

	@Id
	@Column(name = "ID", unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional=true)
	@JoinColumn(name = "ProductID", nullable = false)
	private Product product;

	@Column(name = "Lang", nullable = false, unique = true)
	private String lang;

	@Column(name = "T_ProductName", length = 255, nullable = false)
	private String translatedProductName;

	@Column(name = "T_City", length = 255, nullable = false)
	private String translatedCity;

	@Column(name = "T_ProductDetails", nullable = false)
	private String translatedProductDetails;

	@Column(name = "T_CompanyProfile", nullable = false)
	private String translatedCompanyProfile;

	@Column(name = "T_CompanyName", length = 255, nullable = false)
	private String translatedCompanyName;

	@Column(name = "T_CompanyAddress", length = 255, nullable = false)
	private String translatedCompanyAddress;

	@Column(name = "T_isActive", nullable = false)
	private boolean isActive;

	@Column(name = "Message", nullable = true)
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTranslatedProductName() {
		return translatedProductName;
	}

	public void setTranslatedProductName(String translatedProductName) {
		this.translatedProductName = translatedProductName;
	}

	public String getTranslatedCity() {
		return translatedCity;
	}

	public void setTranslatedCity(String translatedCity) {
		this.translatedCity = translatedCity;
	}

	public String getTranslatedProductDetails() {
		return translatedProductDetails;
	}

	public void setTranslatedProductDetails(String translatedProductDetails) {
		this.translatedProductDetails = translatedProductDetails;
	}

	public String getTranslatedCompanyProfile() {
		return translatedCompanyProfile;
	}

	public void setTranslatedCompanyProfile(String translatedCompanyProfile) {
		this.translatedCompanyProfile = translatedCompanyProfile;
	}

	public String getTranslatedCompanyName() {
		return translatedCompanyName;
	}

	public void setTranslatedCompanyName(String translatedCompanyName) {
		this.translatedCompanyName = translatedCompanyName;
	}

	public String getTranslatedCompanyAddress() {
		return translatedCompanyAddress;
	}

	public void setTranslatedCompanyAddress(String translatedCompanyAddress) {
		this.translatedCompanyAddress = translatedCompanyAddress;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}

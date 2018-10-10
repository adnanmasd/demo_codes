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
import javax.persistence.Transient;

@Entity
@Table(name = "products_of_the_day", catalog = "madeinw")
@Access(AccessType.FIELD)
public class ProductsOfTheDay implements Serializable {
	@Transient
	private static final long serialVersionUID = 4010805864108444820L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ProductID", nullable = false)
	private Product product;

	@Column(name = "Day")
	private String day;

	@Column(name = "ProductName")
	private String productName;

	public ProductsOfTheDay() {

	}

	public ProductsOfTheDay(Product p, String d, String n) {
		this.day = d;
		this.product = p;
		this.productName = n;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}

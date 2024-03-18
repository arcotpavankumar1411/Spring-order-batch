package com.training.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "Productdto")
@Data
public class Product {

	@Id
	@GeneratedValue
	private Long p_Id;
	@Column
	private String productName;
	@Column
	private int price;

	public Product() {
		super();
	}

	public Long getP_Id() {
		return p_Id;
	}

	public void setP_Id(Long p_Id) {
		this.p_Id = p_Id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}

package com.training.config;

import org.springframework.batch.item.ItemProcessor;

import com.training.model.Product;

public class ProductProcess implements ItemProcessor<Product, Product> {

	@Override
	public Product process(Product item) throws Exception {
		
		return item;
	}

}

package com.devlpjruan.catalogproject.tests;

import java.time.Instant;

import com.devlpjruan.catalogproject.DTO.ProductDTO;
import com.devlpjruan.catalogproject.entities.Category;
import com.devlpjruan.catalogproject.entities.Product;

public class Factory {
	public static Product createProduct() {
		Product product = new Product(1L, "Phone", "Good phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(createCategory());
		return product;
	}
	public static ProductDTO createProductDTOO() {
		Product product= createProduct();
		return new ProductDTO(product, product.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(2L, "Electronics");
	}
}

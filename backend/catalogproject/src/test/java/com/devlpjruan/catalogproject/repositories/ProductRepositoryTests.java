package com.devlpjruan.catalogproject.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devlpjruan.catalogproject.entities.Product;
import com.devlpjruan.catalogproject.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	@Autowired
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	private void setUp() throws Exception{
		existingId=1L;
		nonExistingId=1000L;
		countTotalProducts=25L;
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExist() {
		
		repository.deleteById(existingId);
		Optional<Product> result= repository.findById(existingId);
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void saveshouldPersistwithAutoincrement() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts+1, product.getId());
	}
	
	/*@Test
	public void deleteShouldThrowsEmptyResultDataAcessExceptionWhenIdDoesNotexist() {
		Assertions.assertThrows(EmptyResultDataAccessException.class, ()->{
			repository.deleteById(nonExistingId);
		});
	}*/
	
	@Test
	public void findByIdShouldReturnNonNullWhenExistId() {
		
		Optional<Product> result = repository.findById(nonExistingId);
		Assertions.assertNotNull(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnNullWhenNotExistId() {
		
		Optional<Product> result = repository.findById(nonExistingId);
		Assertions.assertTrue(result.isEmpty());
	}
}

package com.devlpjruan.catalogproject.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.devlpjruan.catalogproject.DTO.ProductDTO;
import com.devlpjruan.catalogproject.exceptions.ResourceNotFoundException;
import com.devlpjruan.catalogproject.repositories.ProductRepository;

@SpringBootTest
public class ProductServiceIntegrationTest {

	@Autowired
	private ProductService service;
	private ProductRepository repository;

	private Long existsId;
	private Long nonExistsId;
	private Long totalProduct;

	@BeforeEach
	void setUp() {
		existsId = 1L;
		nonExistsId = 1000L;
		totalProduct = 25L;
	}

	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		service.delete(existsId);
		Assertions.assertTrue(totalProduct - 1 == repository.count());
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.delete(nonExistsId);
		});
	}
	
	@Test
	public void findAllPagedShouldReturnPageWhenPagesize10() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAll(pageRequest);
		Assertions.assertTrue(result.isEmpty());
	}

	@Test
	public void findAllPagedShouldOrderedPageWhenPagesize10() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = service.findAll(pageRequest);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
	}
}

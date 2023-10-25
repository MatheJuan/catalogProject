package com.devlpjruan.catalogproject.services;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devlpjruan.catalogproject.DTO.ProductDTO;
import com.devlpjruan.catalogproject.entities.Category;
import com.devlpjruan.catalogproject.entities.Product;
import com.devlpjruan.catalogproject.exceptions.DatabaseException;
import com.devlpjruan.catalogproject.exceptions.ResourceNotFoundException;
import com.devlpjruan.catalogproject.repositories.CategoryRepository;
import com.devlpjruan.catalogproject.repositories.ProductRepository;
import com.devlpjruan.catalogproject.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	@InjectMocks
	private ProductService service;
	@Mock
	private ProductRepository repository;
	@Mock
	private CategoryRepository catRepository;
	
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private Product product;
	private Category category;
	@BeforeEach
	void setUp() {
		existingId= 1L;
		nonExistingId=1000L;
		dependentId= 4L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		category= Factory.createCategory();
		
		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.when(repository.existsById(existingId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.doThrow(ResourceNotFoundException.class).when(repository.findById(nonExistingId));
		
		Mockito.when(catRepository.getReferenceById(existingId)).thenReturn(category);
		Mockito.when(catRepository.getReferenceById(nonExistingId)).thenThrow(ResourceNotFoundException .class);
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenExistingId() {
		ProductDTO productDto= new ProductDTO();
		ProductDTO result = service.update(existingId, productDto);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void throwResourceNotFoundExceptionWhenUpdateIdDoesNotexists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class , () ->{
			ProductDTO productDto= new ProductDTO();
			service.update(nonExistingId, productDto);
		});
	}
	
	
	
	@Test
	public void findByIdWhenExistingId() {
		ProductDTO productDto= new ProductDTO();
		ProductDTO result = service.findById(existingId);
		Assertions.assertNotNull(result);
	}
	@Test
	public void throwResourceNotFoundExceptionWhenDoesNotexistingId() {
		
		Assertions.assertThrows(ResourceNotFoundException.class , () ->{
			service.findById(nonExistingId);
		});
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow(()-> {
			service.findById(existingId);
			//
		});
		
	//	Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
	}
	
	 
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.delete(dependentId);
		});
	}
	
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		
		Page<ProductDTO> result = service.findAll(pageable);
		Assertions.assertNotNull(result);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
			
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->{
			service.delete(nonExistingId);
		
		});
	}
	
	
}

package com.devlpjruan.catalogproject.resources;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devlpjruan.catalogproject.DTO.ProductDTO;
import com.devlpjruan.catalogproject.exceptions.DatabaseException;
import com.devlpjruan.catalogproject.exceptions.ResourceNotFoundException;
import com.devlpjruan.catalogproject.services.ProductService;
import com.devlpjruan.catalogproject.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourcesTests {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper mapper;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;

	@BeforeEach	
	void setUp() throws Exception {
		existingId=1L;
		nonExistingId=2L;
		dependentId=3L;
		productDTO = Factory.createProductDTOO();
		page = new PageImpl<>(List.of(productDTO));

		Mockito.when(service.findAll(ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.update(eq(existingId), any())).thenReturn(productDTO);
		Mockito.when(service.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
		
		Mockito.when(service.update(any(),productDTO)).thenReturn(productDTO);
		
		Mockito.when(service.insert(any())).thenReturn(productDTO);
	}
	@Test
	public void insertShouldReturnCreatedWhenProductDTOexists() throws Exception {
		String jsonBody= mapper.writeValueAsString(productDTO);
		
		ResultActions result =
		mockMvc.perform(post("/product/{id}")
		.content(jsonBody)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void deleteShouldReturnNoContetWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
			.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNoContent());
		
		
	}
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
		String jsonB = mapper.writeValueAsString(productDTO);
		
		ResultActions result=
		mockMvc.perform(put("/product/{id}",existingId)
		.content(jsonB)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());

	}
	@Test
	public void updateShouldReturnNotFoundWhenIdExists() throws Exception{
		String jsonB = mapper.writeValueAsString(productDTO);
		
		ResultActions result= mockMvc.perform(put("/product/{id}",nonExistingId)
		.content(jsonB)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		ResultActions result =
		mockMvc.perform(get("/product")
		.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}
	
	@Test
	public void findByIdShouldReturnProductWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/product/{id}",existingId)
				.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
	}
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		
		ResultActions result = mockMvc.perform(get("/product/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
				result.andExpect(status().isNotFound());
				result.andExpect(jsonPath("$.name").exists());
				result.andExpect(jsonPath("$.description").doesNotExist());
	}
	
}

package com.devlpjruan.catalogproject.resources;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devlpjruan.catalogproject.DTO.ProductDTO;
import com.devlpjruan.catalogproject.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	
	private Long existingId;
	private Long nonExistingId;
	private Long totalProducts;
	private ObjectMapper mapper;
	@BeforeEach
	void setUp() throws Exception{
		existingId=1L;
		nonExistingId=1000L;
		totalProducts=25L;
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception {
		ResultActions result =
				mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
						.accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(totalProducts));
		result.andExpect(jsonPath("$.content").exists());
		result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));
	}
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception{
		ProductDTO productDTO = Factory.createProductDTOO();
		String jsonB =  mapper.writeValueAsString(productDTO);
		String expectedName= productDTO.getName();
		String expectedDescription= productDTO.getDescription();
		
		ResultActions result=
		mockMvc.perform(put("/product/{id}",existingId)
		.content(jsonB)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").value(expectedName));
		result.andExpect(jsonPath("$.description").value(expectedDescription));

	}
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception{
		ProductDTO productDTO = Factory.createProductDTOO();
		String jsonB =  mapper.writeValueAsString(productDTO);
		String expectedName= productDTO.getName();
		String expectedDescription= productDTO.getDescription();
		
		ResultActions result=
		mockMvc.perform(put("/product/{id}",nonExistingId)
		.content(jsonB)
		.contentType(MediaType.APPLICATION_JSON)
		.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(status().isNotFound());
	}
}

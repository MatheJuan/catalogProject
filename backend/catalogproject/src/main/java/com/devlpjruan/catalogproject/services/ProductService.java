package com.devlpjruan.catalogproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devlpjruan.catalogproject.DTO.CategoryDTO;
import com.devlpjruan.catalogproject.DTO.ProductDTO;
import com.devlpjruan.catalogproject.entities.Category;
import com.devlpjruan.catalogproject.entities.Product;
import com.devlpjruan.catalogproject.exceptions.DatabaseException;
import com.devlpjruan.catalogproject.exceptions.ResourceNotFoundException;
import com.devlpjruan.catalogproject.repositories.CategoryRepository;
import com.devlpjruan.catalogproject.repositories.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private CategoryRepository catRepository;
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable){
		Page<Product> list = repository.findAll(pageable);
		return list.map(x -> new ProductDTO(x));
		
	}
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Id not found."));
		return new ProductDTO(entity, entity.getCategories());
	}
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
			Product entity = new Product();
			dtoToEntity(dto, entity);
			return new ProductDTO(entity);
	}
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
		Product entity = repository.getReferenceById(id);
		dtoToEntity(dto, entity);
		return new ProductDTO(entity);
		}catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found "+ id);
		}
	}
	
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
	        	repository.deleteById(id);    		
		}
	    	catch (DataIntegrityViolationException e) {
	        	throw new DatabaseException("Falha de integridade referencial");
	   	}
	}
	private Product dtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories() ) {
			Category categoty = catRepository.getReferenceById(catDto.getId());
			entity.getCategories().add(categoty);
			
		}
		entity= repository.save(entity);
		return entity;
	}
}

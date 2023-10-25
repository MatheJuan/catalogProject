package com.devlpjruan.catalogproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devlpjruan.catalogproject.DTO.CategoryDTO;
import com.devlpjruan.catalogproject.entities.Category;
import com.devlpjruan.catalogproject.exceptions.DatabaseException;
import com.devlpjruan.catalogproject.exceptions.ResourceNotFoundException;
import com.devlpjruan.catalogproject.repositories.CategoryRepository;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	@Transactional(readOnly = true)
	public Page<CategoryDTO> findAll(Pageable pageable){
		Page<Category> list = repository.findAll(pageable);
		return list.map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj= repository.findById(id);
		Category  entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
		return new CategoryDTO(entity);
	}
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {  
		Category entity = new Category();
		entity.setName(dto.getName());
		entity= repository.save(entity);
		return new CategoryDTO(entity);
	}
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
		Category entity = repository.getReferenceById(id);
		entity.setName(dto.getName());
		entity=repository.save(entity);
		return new CategoryDTO(entity);
		}catch (jakarta.persistence.EntityNotFoundException e) {
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
}

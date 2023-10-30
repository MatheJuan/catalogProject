package com.devlpjruan.catalogproject.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devlpjruan.catalogproject.DTO.RoleDTO;
import com.devlpjruan.catalogproject.DTO.UserDTO;
import com.devlpjruan.catalogproject.DTO.UserInsertDTO;
import com.devlpjruan.catalogproject.DTO.UserUpdateDTO;
import com.devlpjruan.catalogproject.entities.Role;
import com.devlpjruan.catalogproject.entities.User;
import com.devlpjruan.catalogproject.exceptions.DatabaseException;
import com.devlpjruan.catalogproject.exceptions.ResourceNotFoundException;
import com.devlpjruan.catalogproject.repositories.RoleRepository;
import com.devlpjruan.catalogproject.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> findAll(Pageable pageable) {
		Page<User> list = repository.findAll(pageable);
		return list.map(x -> new UserDTO(x));
	}

	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {
		Optional<User> obj = repository.findById(id);
		User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Resource not found."));
		return new UserDTO(entity);
	}

	@Transactional
	public UserDTO insert(UserInsertDTO user) {
		User entity = new User();
		DtoToEntity(user, entity);
		entity.setPassword(passwordEncoder.encode(user.getPassword()));
		entity=repository.save(entity);
		return new UserDTO(entity);
	}
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {
		try {
			User entity = repository.getReferenceById(id);
			DtoToEntity(dto, entity);
			entity=repository.save(entity);
			return new UserDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);

		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha na integridade referencial");
		}
	}

	public void DtoToEntity(UserDTO dto, User user) {
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());
		user.getRoles().clear();
		for(RoleDTO roleDto : dto.getRole()) {
			Role role = roleRepository.getReferenceById(roleDto.getId());
			user.getRoles().add(role);
		}
	}
}

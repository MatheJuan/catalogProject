package com.devlpjruan.catalogproject.DTO;

import java.util.HashSet;
import java.util.Set;

import com.devlpjruan.catalogproject.entities.User;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserDTO {
	private Long id;
	@NotBlank(message = "Campo obrigatório")
	private String firstName;
	private String lastName;
	@Email(message = "Por Favor entrar com email válido.")
	private String email;
	private String password;
	
	Set<RoleDTO> roleList = new HashSet<>();
	
	public UserDTO() {
	}
	
	public UserDTO(Long id, String firstName, String lastName, String email, String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}
	public UserDTO(User entity) {
		id = entity.getId();
		firstName = entity.getFirstName();
		lastName = entity.getLastName();
		email = entity.getEmail();
		entity.getRoles().forEach(role -> this.roleList.add(new RoleDTO(role)));
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<RoleDTO> getRole() {
		return roleList;
	}

	public void setRole(Set<RoleDTO> role) {
		this.roleList = role;
	}
	
}

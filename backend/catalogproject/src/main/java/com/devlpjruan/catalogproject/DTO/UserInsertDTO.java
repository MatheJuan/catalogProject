package com.devlpjruan.catalogproject.DTO;

public class UserInsertDTO extends UserDTO{
	
	private String authority;
	
	public UserInsertDTO() {
		super();
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}
	
	
}

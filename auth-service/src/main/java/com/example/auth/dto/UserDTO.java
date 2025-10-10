package com.example.auth.dto;

public class UserDTO {
	private String id;
	private String email;
	private String username;
	private String phone;

	public UserDTO() {
	}

	public UserDTO(String id, String email, String username, String phone) {
		this.id = id;
		this.email = email;
		this.username = username;
		this.phone = phone;
	}

	public String getId() {
		return id;
	}

	public void setId(String userId) {
		this.id = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}

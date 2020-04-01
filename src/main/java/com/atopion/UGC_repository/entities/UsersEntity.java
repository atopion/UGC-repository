package com.atopion.UGC_repository.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "users")
public class UsersEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false, unique = true, columnDefinition = "int")
	private int user_id;

	@Size(max = 150)
	@Column(name = "user_token", nullable = false, unique = true, columnDefinition = "varchar(150)")
	private String user_token;

	@Size(max = 150)
	@Column(name = "user_name", columnDefinition = "varchar(150)")
	private String user_name;

	@Size(max = 150)
	@Column(name = "user_email", columnDefinition = "varchar(150)")
	private String user_email;

	protected UsersEntity() {}

	public UsersEntity(int user_id, String user_token, String user_name, String user_email) {
		this.user_id = user_id;
		this.user_token = user_token;
		this.user_name = user_name;
		this.user_email = user_email;
	}

	public UsersEntity(String user_token, String user_name, String user_email) {
		this.user_token = user_token;
		this.user_name = user_name;
		this.user_email = user_email;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public int getUser_id() {
		return this.user_id;
	}

	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}

	public String getUser_token() {
		return this.user_token;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_name() {
		return this.user_name;
	}

	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}

	public String getUser_email() {
		return this.user_email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof UsersEntity)) return false;
		UsersEntity that = (UsersEntity) o;
		return Objects.equals(user_id, that.user_id) && Objects.equals(user_token, that.user_token) && Objects.equals(user_name, that.user_name) && Objects.equals(user_email, that.user_email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(user_id, user_token, user_name, user_email);
	}

	@Override
	public String toString() {
		return "UsersEntity{user_id=" + user_id + ", user_token='" + user_token + "', user_name='" + user_name + "', user_email='" + user_email + "'}";
	}
}
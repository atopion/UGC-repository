package com.atopion.UGC_repository.rest.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "content_lists")
public class ContentListsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "list_id", nullable = false, unique = true, columnDefinition = "int")
	private int list_id;

	@Size(max = 200)
	@Column(name = "list_title", nullable = false, columnDefinition = "varchar(200)")
	private String list_title;

	@Size(max = 3000)
	@Column(name = "list_description", nullable = false, columnDefinition = "varchar(3000)")
	private String list_description;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "list_created", nullable = false, columnDefinition = "datetime")
	private Date list_created;

	@Column(name = "application_id", nullable = false, columnDefinition = "int")
	private int application_id;

	@Size(max = 150)
	@Column(name = "user_token", columnDefinition = "varchar(150)")
	private String user_token;

	protected ContentListsEntity() {}

	public ContentListsEntity(int list_id, String list_title, String list_description, Date list_created, int application_id, String user_token) {
		this.list_id = list_id;
		this.list_title = list_title;
		this.list_description = list_description;
		this.list_created = list_created;
		this.application_id = application_id;
		this.user_token = user_token;
	}

	public ContentListsEntity(String list_title, String list_description, Date list_created, int application_id, String user_token) {
		this.list_title = list_title;
		this.list_description = list_description;
		this.list_created = list_created;
		this.application_id = application_id;
		this.user_token = user_token;
	}

	public void setList_id(int list_id) {
		this.list_id = list_id;
	}

	public int getList_id() {
		return this.list_id;
	}

	public void setList_title(String list_title) {
		this.list_title = list_title;
	}

	public String getList_title() {
		return this.list_title;
	}

	public void setList_description(String list_description) {
		this.list_description = list_description;
	}

	public String getList_description() {
		return this.list_description;
	}

	public void setList_created(Date list_created) {
		this.list_created = list_created;
	}

	public Date getList_created() {
		return this.list_created;
	}

	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}

	public int getApplication_id() {
		return this.application_id;
	}

	public void setUser_token(String user_token) {
		this.user_token = user_token;
	}

	public String getUser_token() {
		return this.user_token;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContentListsEntity)) return false;
		ContentListsEntity that = (ContentListsEntity) o;
		return Objects.equals(list_id, that.list_id) && Objects.equals(list_title, that.list_title) && Objects.equals(list_description, that.list_description) && Objects.equals(list_created, that.list_created) && Objects.equals(application_id, that.application_id) && Objects.equals(user_token, that.user_token);
	}

	@Override
	public int hashCode() {
		return Objects.hash(list_id, list_title, list_description, list_created, application_id, user_token);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "ContentListsEntity{list_id=" + list_id + ", list_title='" + list_title + "', list_description='" + list_description + "', list_created=" + format.format(list_created) + ", application_id=" + application_id + ", user_token='" + user_token + "'}";
	}
}
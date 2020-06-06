package com.atopion.UGC_repository.rest.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "content_comments")
public class ContentCommentsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id", nullable = false, unique = true, columnDefinition = "int")
	private int comment_id;

	@Size(max = 3000)
	@Column(name = "comment_text", nullable = false, columnDefinition = "varchar(3000)")
	private String comment_text;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "comment_created", nullable = false, columnDefinition = "datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date comment_created;

	@Column(name = "application_id", nullable = false, columnDefinition = "int")
	private int application_id;

	@Column(name = "record_id", nullable = false, columnDefinition = "int")
	private int record_id;

	@Size(max = 150)
	@Column(name = "user_token", columnDefinition = "varchar(150)")
	private String user_token;

	protected ContentCommentsEntity() {}

	public ContentCommentsEntity(int comment_id, String comment_text, Date comment_created, int application_id, int record_id, String user_token) {
		this.comment_id = comment_id;
		this.comment_text = comment_text;
		this.comment_created = comment_created;
		this.application_id = application_id;
		this.record_id = record_id;
		this.user_token = user_token;
	}

	public ContentCommentsEntity(String comment_text, Date comment_created, int application_id, int record_id, String user_token) {
		this.comment_text = comment_text;
		this.comment_created = comment_created;
		this.application_id = application_id;
		this.record_id = record_id;
		this.user_token = user_token;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getComment_id() {
		return this.comment_id;
	}

	public void setComment_text(String comment_text) {
		this.comment_text = comment_text;
	}

	public String getComment_text() {
		return this.comment_text;
	}

	public void setComment_created(Date comment_created) {
		this.comment_created = comment_created;
	}

	public Date getComment_created() {
		return this.comment_created;
	}

	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}

	public int getApplication_id() {
		return this.application_id;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public int getRecord_id() {
		return this.record_id;
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
		if (!(o instanceof ContentCommentsEntity)) return false;
		ContentCommentsEntity that = (ContentCommentsEntity) o;
		return Objects.equals(comment_id, that.comment_id) && Objects.equals(comment_text, that.comment_text) && Objects.equals(comment_created, that.comment_created) && Objects.equals(application_id, that.application_id) && Objects.equals(record_id, that.record_id) && Objects.equals(user_token, that.user_token);
	}

	@Override
	public int hashCode() {
		return Objects.hash(comment_id, comment_text, comment_created, application_id, record_id, user_token);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "ContentCommentsEntity{comment_id=" + comment_id + ", comment_text='" + comment_text + "', comment_created=" + format.format(comment_created) + ", application_id=" + application_id + ", record_id=" + record_id + ", user_token='" + user_token + "'}";
	}
}
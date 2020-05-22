package com.atopion.UGC_repository.rest.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "content_annotations")
public class ContentAnnotationsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "annotation_id", nullable = false, unique = true, columnDefinition = "int")
	private int annotation_id;

	@Size(max = 500)
	@Column(name = "annotation_url", nullable = false, columnDefinition = "varchar(500)")
	private String annotation_url;

	@Size(max = 5000)
	@Column(name = "annotation_content", nullable = false, columnDefinition = "varchar(5000)")
	private String annotation_content;

	@Column(name = "annotation_canvas", columnDefinition = "int")
	private int annotation_canvas;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "annotation_created", nullable = false, columnDefinition = "datetime")
	private Date annotation_created;

	@Column(name = "application_id", nullable = false, columnDefinition = "int")
	private int application_id;

	@Column(name = "record_id", nullable = false, columnDefinition = "int")
	private int record_id;

	@Size(max = 150)
	@Column(name = "user_token", nullable = false, columnDefinition = "varchar(150)")
	private String user_token;

	protected ContentAnnotationsEntity() {}

	public ContentAnnotationsEntity(int annotation_id, String annotation_url, String annotation_content, int annotation_canvas, Date annotation_created, int application_id, int record_id, String user_token) {
		this.annotation_id = annotation_id;
		this.annotation_url = annotation_url;
		this.annotation_content = annotation_content;
		this.annotation_canvas = annotation_canvas;
		this.annotation_created = annotation_created;
		this.application_id = application_id;
		this.record_id = record_id;
		this.user_token = user_token;
	}

	public ContentAnnotationsEntity(String annotation_url, String annotation_content, int annotation_canvas, Date annotation_created, int application_id, int record_id, String user_token) {
		this.annotation_url = annotation_url;
		this.annotation_content = annotation_content;
		this.annotation_canvas = annotation_canvas;
		this.annotation_created = annotation_created;
		this.application_id = application_id;
		this.record_id = record_id;
		this.user_token = user_token;
	}

	public void setAnnotation_id(int annotation_id) {
		this.annotation_id = annotation_id;
	}

	public int getAnnotation_id() {
		return this.annotation_id;
	}

	public void setAnnotation_url(String annotation_url) {
		this.annotation_url = annotation_url;
	}

	public String getAnnotation_url() {
		return this.annotation_url;
	}

	public void setAnnotation_content(String annotation_content) {
		this.annotation_content = annotation_content;
	}

	public String getAnnotation_content() {
		return this.annotation_content;
	}

	public void setAnnotation_canvas(int annotation_canvas) {
		this.annotation_canvas = annotation_canvas;
	}

	public int getAnnotation_canvas() {
		return this.annotation_canvas;
	}

	public void setAnnotation_created(Date annotation_created) {
		this.annotation_created = annotation_created;
	}

	public Date getAnnotation_created() {
		return this.annotation_created;
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
		if (!(o instanceof ContentAnnotationsEntity)) return false;
		ContentAnnotationsEntity that = (ContentAnnotationsEntity) o;
		return Objects.equals(annotation_id, that.annotation_id) && Objects.equals(annotation_url, that.annotation_url) && Objects.equals(annotation_content, that.annotation_content) && Objects.equals(annotation_canvas, that.annotation_canvas) && Objects.equals(annotation_created, that.annotation_created) && Objects.equals(application_id, that.application_id) && Objects.equals(record_id, that.record_id) && Objects.equals(user_token, that.user_token);
	}

	@Override
	public int hashCode() {
		return Objects.hash(annotation_id, annotation_url, annotation_content, annotation_canvas, annotation_created, application_id, record_id, user_token);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "ContentAnnotationsEntity{annotation_id=" + annotation_id + ", annotation_url='" + annotation_url + "', annotation_content='" + annotation_content + "', annotation_canvas=" + annotation_canvas + ", annotation_created=" + format.format(annotation_created) + ", application_id=" + application_id + ", record_id=" + record_id + ", user_token='" + user_token + "'}";
	}
}
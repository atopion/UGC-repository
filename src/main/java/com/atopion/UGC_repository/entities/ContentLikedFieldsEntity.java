package com.atopion.UGC_repository.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "content_liked_fields")
public class ContentLikedFieldsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "field_id", nullable = false, unique = true, columnDefinition = "int")
	private int field_id;

	@Size(max = 200)
	@Column(name = "field_name", nullable = false, columnDefinition = "varchar(200)")
	private String field_name;

	@Column(name = "field_like_count", nullable = false, columnDefinition = "int")
	private int field_like_count;

	@Column(name = "record_id", nullable = false, columnDefinition = "int")
	private int record_id;

	protected ContentLikedFieldsEntity() {}

	public ContentLikedFieldsEntity(int field_id, String field_name, int field_like_count, int record_id) {
		this.field_id = field_id;
		this.field_name = field_name;
		this.field_like_count = field_like_count;
		this.record_id = record_id;
	}

	public ContentLikedFieldsEntity(String field_name, int field_like_count, int record_id) {
		this.field_name = field_name;
		this.field_like_count = field_like_count;
		this.record_id = record_id;
	}

	public void setField_id(int field_id) {
		this.field_id = field_id;
	}

	public int getField_id() {
		return this.field_id;
	}

	public void setField_name(String field_name) {
		this.field_name = field_name;
	}

	public String getField_name() {
		return this.field_name;
	}

	public void setField_like_count(int field_like_count) {
		this.field_like_count = field_like_count;
	}

	public int getField_like_count() {
		return this.field_like_count;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public int getRecord_id() {
		return this.record_id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContentLikedFieldsEntity)) return false;
		ContentLikedFieldsEntity that = (ContentLikedFieldsEntity) o;
		return Objects.equals(field_id, that.field_id) && Objects.equals(field_name, that.field_name) && Objects.equals(field_like_count, that.field_like_count) && Objects.equals(record_id, that.record_id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(field_id, field_name, field_like_count, record_id);
	}

	@Override
	public String toString() {
		return "ContentLikedFieldsEntity{field_id=" + field_id + ", field_name='" + field_name + "', field_like_count=" + field_like_count + ", record_id=" + record_id + "}";
	}
}
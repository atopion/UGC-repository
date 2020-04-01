package com.atopion.UGC_repository.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "content_likes")
public class ContentLikesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "record_id", nullable = false, unique = true, columnDefinition = "int")
	private int record_id;

	@Column(name = "cuby_like_level_1", nullable = false, columnDefinition = "int")
	private int cuby_like_level_1;

	@Column(name = "cuby_like_level_2", nullable = false, columnDefinition = "int")
	private int cuby_like_level_2;

	@Column(name = "cuby_like_level_3", nullable = false, columnDefinition = "int")
	private int cuby_like_level_3;

	protected ContentLikesEntity() {}

	public ContentLikesEntity(int record_id, int cuby_like_level_1, int cuby_like_level_2, int cuby_like_level_3) {
		this.record_id = record_id;
		this.cuby_like_level_1 = cuby_like_level_1;
		this.cuby_like_level_2 = cuby_like_level_2;
		this.cuby_like_level_3 = cuby_like_level_3;
	}

	public ContentLikesEntity(int cuby_like_level_1, int cuby_like_level_2, int cuby_like_level_3) {
		this.cuby_like_level_1 = cuby_like_level_1;
		this.cuby_like_level_2 = cuby_like_level_2;
		this.cuby_like_level_3 = cuby_like_level_3;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public int getRecord_id() {
		return this.record_id;
	}

	public void setCuby_like_level_1(int cuby_like_level_1) {
		this.cuby_like_level_1 = cuby_like_level_1;
	}

	public int getCuby_like_level_1() {
		return this.cuby_like_level_1;
	}

	public void setCuby_like_level_2(int cuby_like_level_2) {
		this.cuby_like_level_2 = cuby_like_level_2;
	}

	public int getCuby_like_level_2() {
		return this.cuby_like_level_2;
	}

	public void setCuby_like_level_3(int cuby_like_level_3) {
		this.cuby_like_level_3 = cuby_like_level_3;
	}

	public int getCuby_like_level_3() {
		return this.cuby_like_level_3;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContentLikesEntity)) return false;
		ContentLikesEntity that = (ContentLikesEntity) o;
		return Objects.equals(record_id, that.record_id) && Objects.equals(cuby_like_level_1, that.cuby_like_level_1) && Objects.equals(cuby_like_level_2, that.cuby_like_level_2) && Objects.equals(cuby_like_level_3, that.cuby_like_level_3);
	}

	@Override
	public int hashCode() {
		return Objects.hash(record_id, cuby_like_level_1, cuby_like_level_2, cuby_like_level_3);
	}

	@Override
	public String toString() {
		return "ContentLikesEntity{record_id=" + record_id + ", cuby_like_level_1=" + cuby_like_level_1 + ", cuby_like_level_2=" + cuby_like_level_2 + ", cuby_like_level_3=" + cuby_like_level_3 + "}";
	}
}
package com.atopion.UGC_repository.webannotation.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationBodyEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationStateEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationSelectorEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationStylesheetEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationAudienceEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationAgentEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationBodyEntity;
import com.atopion.UGC_repository.webannotation.entities.WebAnnotationTargetEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "web_annotation_cached")
public class WebAnnotationCachedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "cached_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int cached_id;

	@Size(max = 255)
	@Column(name = "cached", columnDefinition = "varchar(255)")
	private String cached;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationStateEntity stateEntity;

	protected WebAnnotationCachedEntity() {}

	public WebAnnotationCachedEntity(int cached_id, String cached, WebAnnotationStateEntity stateEntity) {
		this.cached_id = cached_id;
		this.cached = cached;
		this.stateEntity = stateEntity;
	}

	public WebAnnotationCachedEntity(String cached, WebAnnotationStateEntity stateEntity) {
		this.cached = cached;
		this.stateEntity = stateEntity;
	}

	public void setCached_id(int cached_id) {
		this.cached_id = cached_id;
	}

	public int getCached_id() {
		return this.cached_id;
	}

	public void setCached(String cached) {
		this.cached = cached;
	}

	public String getCached() {
		return this.cached;
	}

	public void setStateEntity(WebAnnotationStateEntity stateEntity) {
		this.stateEntity = stateEntity;
	}

	public WebAnnotationStateEntity getStateEntity() {
		return this.stateEntity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationCachedEntity)) return false;
		WebAnnotationCachedEntity that = (WebAnnotationCachedEntity) o;
		return Objects.equals(cached_id, that.cached_id) && Objects.equals(cached, that.cached) && Objects.equals(stateEntity, that.stateEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(cached_id, cached, stateEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationCachedEntity{cached_id=" + cached_id + ", cached='" + cached + "', stateEntity=" + stateEntity + "}";
	}
}
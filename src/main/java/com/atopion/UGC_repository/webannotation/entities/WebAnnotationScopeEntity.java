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
@Table(name = "web_annotation_scope")
public class WebAnnotationScopeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "scope_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int scope_id;

	@Size(max = 255)
	@Column(name = "id", nullable = false, columnDefinition = "varchar(255)")
	private String id;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationTargetEntity targetEntity;

	protected WebAnnotationScopeEntity() {}

	public WebAnnotationScopeEntity(int scope_id, String id, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.scope_id = scope_id;
		this.id = id;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public WebAnnotationScopeEntity(String id, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.id = id;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public void setScope_id(int scope_id) {
		this.scope_id = scope_id;
	}

	public int getScope_id() {
		return this.scope_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setBodyEntity(WebAnnotationBodyEntity bodyEntity) {
		this.bodyEntity = bodyEntity;
	}

	public WebAnnotationBodyEntity getBodyEntity() {
		return this.bodyEntity;
	}

	public void setTargetEntity(WebAnnotationTargetEntity targetEntity) {
		this.targetEntity = targetEntity;
	}

	public WebAnnotationTargetEntity getTargetEntity() {
		return this.targetEntity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationScopeEntity)) return false;
		WebAnnotationScopeEntity that = (WebAnnotationScopeEntity) o;
		return Objects.equals(scope_id, that.scope_id) && Objects.equals(id, that.id) && Objects.equals(bodyEntity, that.bodyEntity) && Objects.equals(targetEntity, that.targetEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(scope_id, id, bodyEntity, targetEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationScopeEntity{scope_id=" + scope_id + ", id='" + id + "', bodyEntity=" + bodyEntity + ", targetEntity=" + targetEntity + "}";
	}
}
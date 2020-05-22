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
@Table(name = "web_annotation_rights")
public class WebAnnotationRightsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "rights_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int rights_id;

	@Size(max = 255)
	@Column(name = "rights", nullable = false, columnDefinition = "varchar(255)")
	private String rights;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "annotation_id", columnDefinition = "int(10)")
	private WebAnnotationEntity annotationEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id", columnDefinition = "int(10)")
	private WebAnnotationTargetEntity targetEntity;

	protected WebAnnotationRightsEntity() {}

	public WebAnnotationRightsEntity(int rights_id, String rights, WebAnnotationEntity annotationEntity, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.rights_id = rights_id;
		this.rights = rights;
		this.annotationEntity = annotationEntity;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public WebAnnotationRightsEntity(String rights, WebAnnotationEntity annotationEntity, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.rights = rights;
		this.annotationEntity = annotationEntity;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public void setRights_id(int rights_id) {
		this.rights_id = rights_id;
	}

	public int getRights_id() {
		return this.rights_id;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	public String getRights() {
		return this.rights;
	}

	public void setAnnotationEntity(WebAnnotationEntity annotationEntity) {
		this.annotationEntity = annotationEntity;
	}

	public WebAnnotationEntity getAnnotationEntity() {
		return this.annotationEntity;
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
		if (!(o instanceof WebAnnotationRightsEntity)) return false;
		WebAnnotationRightsEntity that = (WebAnnotationRightsEntity) o;
		return Objects.equals(rights_id, that.rights_id) && Objects.equals(rights, that.rights) && Objects.equals(annotationEntity, that.annotationEntity) && Objects.equals(bodyEntity, that.bodyEntity) && Objects.equals(targetEntity, that.targetEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rights_id, rights, annotationEntity, bodyEntity, targetEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationRightsEntity{rights_id=" + rights_id + ", rights='" + rights + "', annotationEntity=" + annotationEntity + ", bodyEntity=" + bodyEntity + ", targetEntity=" + targetEntity + "}";
	}
}
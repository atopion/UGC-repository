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
@Table(name = "web_annotation_accessibility")
public class WebAnnotationAccessibilityEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "accessibility_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int accessibility_id;

	@Size(max = 255)
	@Column(name = "accessibility", nullable = false, columnDefinition = "varchar(255)")
	private String accessibility;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id", columnDefinition = "int(10)")
	private WebAnnotationTargetEntity targetEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	protected WebAnnotationAccessibilityEntity() {}

	public WebAnnotationAccessibilityEntity(int accessibility_id, String accessibility, WebAnnotationTargetEntity targetEntity, WebAnnotationBodyEntity bodyEntity) {
		this.accessibility_id = accessibility_id;
		this.accessibility = accessibility;
		this.targetEntity = targetEntity;
		this.bodyEntity = bodyEntity;
	}

	public WebAnnotationAccessibilityEntity(String accessibility, WebAnnotationTargetEntity targetEntity, WebAnnotationBodyEntity bodyEntity) {
		this.accessibility = accessibility;
		this.targetEntity = targetEntity;
		this.bodyEntity = bodyEntity;
	}

	public void setAccessibility_id(int accessibility_id) {
		this.accessibility_id = accessibility_id;
	}

	public int getAccessibility_id() {
		return this.accessibility_id;
	}

	public void setAccessibility(String accessibility) {
		this.accessibility = accessibility;
	}

	public String getAccessibility() {
		return this.accessibility;
	}

	public void setTargetEntity(WebAnnotationTargetEntity targetEntity) {
		this.targetEntity = targetEntity;
	}

	public WebAnnotationTargetEntity getTargetEntity() {
		return this.targetEntity;
	}

	public void setBodyEntity(WebAnnotationBodyEntity bodyEntity) {
		this.bodyEntity = bodyEntity;
	}

	public WebAnnotationBodyEntity getBodyEntity() {
		return this.bodyEntity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationAccessibilityEntity)) return false;
		WebAnnotationAccessibilityEntity that = (WebAnnotationAccessibilityEntity) o;
		return Objects.equals(accessibility_id, that.accessibility_id) && Objects.equals(accessibility, that.accessibility) && Objects.equals(targetEntity, that.targetEntity) && Objects.equals(bodyEntity, that.bodyEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(accessibility_id, accessibility, targetEntity, bodyEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationAccessibilityEntity{accessibility_id=" + accessibility_id + ", accessibility='" + accessibility + "', targetEntity=" + targetEntity + ", bodyEntity=" + bodyEntity + "}";
	}
}
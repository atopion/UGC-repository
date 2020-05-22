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
@Table(name = "web_annotation_style_class")
public class WebAnnotationStyleClassEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "style_class_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int style_class_id;

	@Size(max = 255)
	@Column(name = "style_class", nullable = false, columnDefinition = "varchar(255)")
	private String style_class;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationTargetEntity targetEntity;

	protected WebAnnotationStyleClassEntity() {}

	public WebAnnotationStyleClassEntity(int style_class_id, String style_class, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.style_class_id = style_class_id;
		this.style_class = style_class;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public WebAnnotationStyleClassEntity(String style_class, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.style_class = style_class;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public void setStyle_class_id(int style_class_id) {
		this.style_class_id = style_class_id;
	}

	public int getStyle_class_id() {
		return this.style_class_id;
	}

	public void setStyle_class(String style_class) {
		this.style_class = style_class;
	}

	public String getStyle_class() {
		return this.style_class;
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
		if (!(o instanceof WebAnnotationStyleClassEntity)) return false;
		WebAnnotationStyleClassEntity that = (WebAnnotationStyleClassEntity) o;
		return Objects.equals(style_class_id, that.style_class_id) && Objects.equals(style_class, that.style_class) && Objects.equals(bodyEntity, that.bodyEntity) && Objects.equals(targetEntity, that.targetEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(style_class_id, style_class, bodyEntity, targetEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationStyleClassEntity{style_class_id=" + style_class_id + ", style_class='" + style_class + "', bodyEntity=" + bodyEntity + ", targetEntity=" + targetEntity + "}";
	}
}
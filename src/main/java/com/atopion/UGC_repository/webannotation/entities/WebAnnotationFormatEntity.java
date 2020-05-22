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
@Table(name = "web_annotation_format")
public class WebAnnotationFormatEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "format_id", nullable = false, unique = true, columnDefinition = "int(11)")
	private int format_id;

	@Size(max = 255)
	@Column(name = "format", nullable = false, columnDefinition = "varchar(255)")
	private String format;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id", columnDefinition = "int(10)")
	private WebAnnotationTargetEntity targetEntity;

	protected WebAnnotationFormatEntity() {}

	public WebAnnotationFormatEntity(int format_id, String format, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.format_id = format_id;
		this.format = format;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public WebAnnotationFormatEntity(String format, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.format = format;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public void setFormat_id(int format_id) {
		this.format_id = format_id;
	}

	public int getFormat_id() {
		return this.format_id;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormat() {
		return this.format;
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
		if (!(o instanceof WebAnnotationFormatEntity)) return false;
		WebAnnotationFormatEntity that = (WebAnnotationFormatEntity) o;
		return Objects.equals(format_id, that.format_id) && Objects.equals(format, that.format) && Objects.equals(bodyEntity, that.bodyEntity) && Objects.equals(targetEntity, that.targetEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(format_id, format, bodyEntity, targetEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationFormatEntity{format_id=" + format_id + ", format='" + format + "', bodyEntity=" + bodyEntity + ", targetEntity=" + targetEntity + "}";
	}
}
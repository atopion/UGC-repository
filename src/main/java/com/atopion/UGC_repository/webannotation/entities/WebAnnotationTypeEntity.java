package com.atopion.UGC_repository.webannotation.entities;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.beans.ConstructorProperties;
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
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import net.bytebuddy.asm.Advice;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "web_annotation_type")
public class WebAnnotationTypeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "type_id", nullable = false, unique = true, columnDefinition = "int(11)")
	private int type_id;

	@Size(max = 255)
	@Column(name = "type", nullable = false, columnDefinition = "varchar(255)")
	private String type;

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

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "agent_id", columnDefinition = "int(10)")
	private WebAnnotationAgentEntity agentEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "audience_id", columnDefinition = "int(10)")
	private WebAnnotationAudienceEntity audienceEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "stylesheet_id", columnDefinition = "int(10)")
	private WebAnnotationStylesheetEntity stylesheetEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "selector_id", columnDefinition = "int(10)")
	private WebAnnotationSelectorEntity selectorEntity;

	protected WebAnnotationTypeEntity() {}

	public WebAnnotationTypeEntity(int type_id, String type, @JacksonInject WebAnnotationEntity annotationEntity, @JacksonInject WebAnnotationBodyEntity bodyEntity, @JacksonInject WebAnnotationTargetEntity targetEntity, @JacksonInject WebAnnotationAgentEntity agentEntity, @JacksonInject WebAnnotationAudienceEntity audienceEntity, @JacksonInject WebAnnotationStylesheetEntity stylesheetEntity, @JacksonInject WebAnnotationSelectorEntity selectorEntity) {
		this.type_id = type_id;
		this.type = type;
		this.annotationEntity = annotationEntity;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
		this.agentEntity = agentEntity;
		this.audienceEntity = audienceEntity;
		this.stylesheetEntity = stylesheetEntity;
		this.selectorEntity = selectorEntity;
	}

	@ConstructorProperties("abv")
	public WebAnnotationTypeEntity(String type, @JacksonInject WebAnnotationEntity annotationEntity, @JacksonInject WebAnnotationBodyEntity bodyEntity, @JacksonInject WebAnnotationTargetEntity targetEntity, @JacksonInject WebAnnotationAgentEntity agentEntity, @JacksonInject WebAnnotationAudienceEntity audienceEntity, @JacksonInject WebAnnotationStylesheetEntity stylesheetEntity, @JacksonInject WebAnnotationSelectorEntity selectorEntity) {
		this.type = type;
		this.annotationEntity = annotationEntity;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
		this.agentEntity = agentEntity;
		this.audienceEntity = audienceEntity;
		this.stylesheetEntity = stylesheetEntity;
		this.selectorEntity = selectorEntity;
	}

	public void setType_id(int type_id) {
		this.type_id = type_id;
	}

	public int getType_id() {
		return this.type_id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
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

	public void setAgentEntity(WebAnnotationAgentEntity agentEntity) {
		this.agentEntity = agentEntity;
	}

	public WebAnnotationAgentEntity getAgentEntity() {
		return this.agentEntity;
	}

	public void setAudienceEntity(WebAnnotationAudienceEntity audienceEntity) {
		this.audienceEntity = audienceEntity;
	}

	public WebAnnotationAudienceEntity getAudienceEntity() {
		return this.audienceEntity;
	}

	public void setStylesheetEntity(WebAnnotationStylesheetEntity stylesheetEntity) {
		this.stylesheetEntity = stylesheetEntity;
	}

	public WebAnnotationStylesheetEntity getStylesheetEntity() {
		return this.stylesheetEntity;
	}

	public void setSelectorEntity(WebAnnotationSelectorEntity selectorEntity) {
		this.selectorEntity = selectorEntity;
	}

	public WebAnnotationSelectorEntity getSelectorEntity() {
		return this.selectorEntity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationTypeEntity)) return false;
		WebAnnotationTypeEntity that = (WebAnnotationTypeEntity) o;
		return Objects.equals(type_id, that.type_id) && Objects.equals(type, that.type) && Objects.equals(annotationEntity, that.annotationEntity) && Objects.equals(bodyEntity, that.bodyEntity) && Objects.equals(targetEntity, that.targetEntity) && Objects.equals(agentEntity, that.agentEntity) && Objects.equals(audienceEntity, that.audienceEntity) && Objects.equals(stylesheetEntity, that.stylesheetEntity) && Objects.equals(selectorEntity, that.selectorEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type_id, type, annotationEntity, bodyEntity, targetEntity, agentEntity, audienceEntity, stylesheetEntity, selectorEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationTypeEntity{type_id=" + type_id + ", type='" + type + "}"; //+ "', annotationEntity=" + annotationEntity + ", bodyEntity=" + bodyEntity + ", targetEntity=" + targetEntity + ", agentEntity=" + agentEntity + ", audienceEntity=" + audienceEntity + ", stylesheetEntity=" + stylesheetEntity + ", selectorEntity=" + selectorEntity + "}";
	}
}
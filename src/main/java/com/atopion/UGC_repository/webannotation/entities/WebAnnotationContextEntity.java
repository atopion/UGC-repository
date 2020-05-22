package com.atopion.UGC_repository.webannotation.entities;

import com.atopion.UGC_repository.util.JSONDeserializer;
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
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "web_annotation_context")
@JsonDeserialize(using = JSONDeserializer.ContextEntity.class)
public class WebAnnotationContextEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "context_id", nullable = false, unique = true, columnDefinition = "int(11)")
	private int context_id;

	@Size(max = 255)
	@Column(name = "context", nullable = false, columnDefinition = "varchar(255)")
	private String context;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "annotation_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationEntity annotationEntity;

	protected WebAnnotationContextEntity() {}

	public WebAnnotationContextEntity(int context_id, String context, WebAnnotationEntity annotationEntity) {
		this.context_id = context_id;
		this.context = context;
		this.annotationEntity = annotationEntity;
	}

	public WebAnnotationContextEntity(String context, WebAnnotationEntity annotationEntity) {
		this.context = context;
		this.annotationEntity = annotationEntity;
	}

	public void setContext_id(int context_id) {
		this.context_id = context_id;
	}

	public int getContext_id() {
		return this.context_id;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getContext() {
		return this.context;
	}

	public void setAnnotationEntity(WebAnnotationEntity annotationEntity) {
		this.annotationEntity = annotationEntity;
	}

	public WebAnnotationEntity getAnnotationEntity() {
		return this.annotationEntity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationContextEntity)) return false;
		WebAnnotationContextEntity that = (WebAnnotationContextEntity) o;
		return Objects.equals(context_id, that.context_id) && Objects.equals(context, that.context) && Objects.equals(annotationEntity, that.annotationEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(context_id, context, annotationEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationContextEntity{context_id=" + context_id + ", context='" + context + "}"; //+ "', annotationEntity=" + annotationEntity + "}";
	}
}
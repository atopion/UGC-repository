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
@Table(name = "web_annotation_motivation")
@JsonDeserialize(using = JSONDeserializer.MotivationEntity.class)
public class WebAnnotationMotivationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "motivation_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int motivation_id;

	@Size(max = 255)
	@Column(name = "motivation", nullable = false, columnDefinition = "varchar(255)")
	private String motivation;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "annotation_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationEntity annotationEntity;

	protected WebAnnotationMotivationEntity() {}

	public WebAnnotationMotivationEntity(int motivation_id, String motivation, WebAnnotationEntity annotationEntity) {
		this.motivation_id = motivation_id;
		this.motivation = motivation;
		this.annotationEntity = annotationEntity;
	}

	public WebAnnotationMotivationEntity(String motivation, WebAnnotationEntity annotationEntity) {
		this.motivation = motivation;
		this.annotationEntity = annotationEntity;
	}

	public void setMotivation_id(int motivation_id) {
		this.motivation_id = motivation_id;
	}

	public int getMotivation_id() {
		return this.motivation_id;
	}

	public void setMotivation(String motivation) {
		this.motivation = motivation;
	}

	public String getMotivation() {
		return this.motivation;
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
		if (!(o instanceof WebAnnotationMotivationEntity)) return false;
		WebAnnotationMotivationEntity that = (WebAnnotationMotivationEntity) o;
		return Objects.equals(motivation_id, that.motivation_id) && Objects.equals(motivation, that.motivation) && Objects.equals(annotationEntity, that.annotationEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(motivation_id, motivation, annotationEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationMotivationEntity{motivation_id=" + motivation_id + ", motivation='" + motivation + "}"; //+ "', annotationEntity=" + annotationEntity + "}";
	}
}
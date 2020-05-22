package com.atopion.UGC_repository.webannotation.entities;

import com.atopion.UGC_repository.util.JSONDeserializer;
import com.atopion.UGC_repository.util.JSONSetSerializer;
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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Set;

@Entity
@Table(name = "web_annotation_audience")
public class WebAnnotationAudienceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    //@JsonIgnore
	@Column(name = "audience_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int audience_id;

	@Size(max = 255)
	@Column(name = "id", columnDefinition = "varchar(255)")
	private String id;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "annotation_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationEntity annotationEntity;

	@OneToMany(mappedBy = "audienceEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.TypeSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.TypeEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationTypeEntity> type;

	protected WebAnnotationAudienceEntity() {}

	public WebAnnotationAudienceEntity(int audience_id, String id, WebAnnotationEntity annotationEntity, Set<WebAnnotationTypeEntity> typeEntity) {
		this.audience_id = audience_id;
		this.id = id;
		this.annotationEntity = annotationEntity;
		this.type = typeEntity;
	}

	public WebAnnotationAudienceEntity(String id, WebAnnotationEntity annotationEntity, Set<WebAnnotationTypeEntity> typeEntity) {
		this.id = id;
		this.annotationEntity = annotationEntity;
		this.type = typeEntity;
	}

	public void setAudienceId(int audience_id) {
		this.audience_id = audience_id;
	}

	@JsonIgnore
	public int getAudienceId() {
		return this.audience_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setAnnotationEntity(WebAnnotationEntity annotationEntity) {
		this.annotationEntity = annotationEntity;
	}

	public WebAnnotationEntity getAnnotationEntity() {
		return this.annotationEntity;
	}

	public Set<WebAnnotationTypeEntity> getType() {
		return type;
	}

	public void setType(Set<WebAnnotationTypeEntity> typeEntity) {
		this.type = typeEntity;
	}

	@JsonSetter("type")
	public void setSingleType(WebAnnotationTypeEntity type) {
		this.type = Set.of(type);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationAudienceEntity)) return false;
		WebAnnotationAudienceEntity that = (WebAnnotationAudienceEntity) o;
		return Objects.equals(audience_id, that.audience_id) && Objects.equals(id, that.id) && Objects.equals(annotationEntity, that.annotationEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(audience_id, id, annotationEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationAudienceEntity{audience_id=" + audience_id + ", id='" + id + "', annotationEntity=" + (annotationEntity == null ? "null" : annotationEntity.getWebAnnotationId()) + "}";
	}
}
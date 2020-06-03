package com.atopion.UGC_repository.webannotation.entities;

import com.atopion.UGC_repository.util.JSONDeserializer;
import com.atopion.UGC_repository.util.JSONSetSerializer;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Set;

@Entity
@Table(name = "web_annotation_stylesheet")
public class WebAnnotationStylesheetEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "stylesheet_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int stylesheet_id;

	@Size(max = 5000)
	@Column(name = "value", columnDefinition = "varchar(5000)")
	private String value;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "annotation_id", columnDefinition = "int(10)")
	@JsonIgnore
	private WebAnnotationEntity annotationEntity;

	@OneToMany(mappedBy = "stylesheetEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL },fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.TypeSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.TypeEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationTypeEntity> type;

	protected WebAnnotationStylesheetEntity() {}

	public WebAnnotationStylesheetEntity(int stylesheet_id, String value, Set<WebAnnotationTypeEntity> type) {
		this.stylesheet_id = stylesheet_id;
		this.value = value;
		this.type = type;
	}

	public WebAnnotationStylesheetEntity(String value, Set<WebAnnotationTypeEntity> type) {
		this.value = value;
		this.type = type;
	}

	public void setStylesheetId(int stylesheet_id) {
		this.stylesheet_id = stylesheet_id;
	}

	@JsonIgnore
	public int getStylesheetId() {
		return this.stylesheet_id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public WebAnnotationEntity getAnnotationEntity() {
		return annotationEntity;
	}

	public void setAnnotationEntity(WebAnnotationEntity annotationEntity) {
		this.annotationEntity = annotationEntity;
	}

	public Set<WebAnnotationTypeEntity> getType() {
		return type;
	}

	public void setType(Set<WebAnnotationTypeEntity> type) {
		this.type = type;
	}

	@JsonSetter("type")
	public void setSingleType(WebAnnotationTypeEntity type) {
		this.type = Set.of(type);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationStylesheetEntity)) return false;
		WebAnnotationStylesheetEntity that = (WebAnnotationStylesheetEntity) o;
		return Objects.equals(stylesheet_id, that.stylesheet_id) && Objects.equals(value, that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(stylesheet_id, value);
	}

	@Override
	public String toString() {
		return "WebAnnotationStylesheetEntity{stylesheet_id=" + stylesheet_id + ", value='" + value + "'}";
	}
}
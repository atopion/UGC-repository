package com.atopion.UGC_repository.webannotation.entities;

import com.atopion.UGC_repository.util.JSONDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Objects;
    
@Entity
@Table(name = "web_annotation_via")
@JsonDeserialize(using = JSONDeserializer.ViaEntity.class)
public class WebAnnotationViaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "via_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int via_id;

	@Size(max = 255)
	@Column(name = "via", nullable = false, columnDefinition = "varchar(255)")
	private String via;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "annotation_id", columnDefinition = "int(10)")
	private WebAnnotationEntity annotationEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id", columnDefinition = "int(10)")
	private WebAnnotationTargetEntity targetEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	protected WebAnnotationViaEntity() {}

	public WebAnnotationViaEntity(int via_id, String via, WebAnnotationEntity annotationEntity, WebAnnotationTargetEntity targetEntity, WebAnnotationBodyEntity bodyEntity) {
		this.via_id = via_id;
		this.via = via;
		this.annotationEntity = annotationEntity;
		this.targetEntity = targetEntity;
		this.bodyEntity = bodyEntity;
	}

	public WebAnnotationViaEntity(String via, WebAnnotationEntity annotationEntity, WebAnnotationTargetEntity targetEntity, WebAnnotationBodyEntity bodyEntity) {
		this.via = via;
		this.annotationEntity = annotationEntity;
		this.targetEntity = targetEntity;
		this.bodyEntity = bodyEntity;
	}

	public void setVia_id(int via_id) {
		this.via_id = via_id;
	}

	public int getVia_id() {
		return this.via_id;
	}

	public void setVia(String via) {
		this.via = via;
	}

	public String getVia() {
		return this.via;
	}

	public void setAnnotationEntity(WebAnnotationEntity annotationEntity) {
		this.annotationEntity = annotationEntity;
	}

	public WebAnnotationEntity getAnnotationEntity() {
		return this.annotationEntity;
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
		if (!(o instanceof WebAnnotationViaEntity)) return false;
		WebAnnotationViaEntity that = (WebAnnotationViaEntity) o;
		return Objects.equals(via_id, that.via_id) && Objects.equals(via, that.via) && Objects.equals(annotationEntity, that.annotationEntity) && Objects.equals(targetEntity, that.targetEntity) && Objects.equals(bodyEntity, that.bodyEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(via_id, via, annotationEntity, targetEntity, bodyEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationViaEntity{via_id=" + via_id + ", via='" + via + "}"; //+ "', annotationEntity=" + annotationEntity + ", targetEntity=" + targetEntity + ", bodyEntity=" + bodyEntity + "}";
	}
}
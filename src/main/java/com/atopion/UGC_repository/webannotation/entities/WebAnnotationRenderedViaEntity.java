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
@Table(name = "web_annotation_rendered_via")
public class WebAnnotationRenderedViaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "rendered_via_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int rendered_via_id;

	@Size(max = 255)
	@Column(name = "id", nullable = false, columnDefinition = "varchar(255)")
	private String id;

	@Size(max = 255)
	@Column(name = "type", columnDefinition = "varchar(255)")
	private String type;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id", columnDefinition = "int(10)")
	private WebAnnotationTargetEntity targetEntity;

	protected WebAnnotationRenderedViaEntity() {}

	public WebAnnotationRenderedViaEntity(int rendered_via_id, String id, String type, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.rendered_via_id = rendered_via_id;
		this.id = id;
		this.type = type;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public WebAnnotationRenderedViaEntity(String id, String type, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity) {
		this.id = id;
		this.type = type;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
	}

	public void setRendered_via_id(int rendered_via_id) {
		this.rendered_via_id = rendered_via_id;
	}

	public int getRendered_via_id() {
		return this.rendered_via_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
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
		if (!(o instanceof WebAnnotationRenderedViaEntity)) return false;
		WebAnnotationRenderedViaEntity that = (WebAnnotationRenderedViaEntity) o;
		return Objects.equals(rendered_via_id, that.rendered_via_id) && Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(bodyEntity, that.bodyEntity) && Objects.equals(targetEntity, that.targetEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rendered_via_id, id, type, bodyEntity, targetEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationRenderedViaEntity{rendered_via_id=" + rendered_via_id + ", id='" + id + "', type='" + type + "', bodyEntity=" + bodyEntity + ", targetEntity=" + targetEntity + "}";
	}
}
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
@Table(name = "web_annotation_source_date")
public class WebAnnotationSourceDateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "source_date_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int source_date_id;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "source_date", nullable = false, columnDefinition = "date")
	private Date source_date;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationStateEntity stateEntity;

	protected WebAnnotationSourceDateEntity() {}

	public WebAnnotationSourceDateEntity(int source_date_id, Date source_date, WebAnnotationStateEntity stateEntity) {
		this.source_date_id = source_date_id;
		this.source_date = source_date;
		this.stateEntity = stateEntity;
	}

	public WebAnnotationSourceDateEntity(Date source_date, WebAnnotationStateEntity stateEntity) {
		this.source_date = source_date;
		this.stateEntity = stateEntity;
	}

	public void setSource_date_id(int source_date_id) {
		this.source_date_id = source_date_id;
	}

	public int getSource_date_id() {
		return this.source_date_id;
	}

	public void setSource_date(Date source_date) {
		this.source_date = source_date;
	}

	public Date getSource_date() {
		return this.source_date;
	}

	public void setStateEntity(WebAnnotationStateEntity stateEntity) {
		this.stateEntity = stateEntity;
	}

	public WebAnnotationStateEntity getStateEntity() {
		return this.stateEntity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationSourceDateEntity)) return false;
		WebAnnotationSourceDateEntity that = (WebAnnotationSourceDateEntity) o;
		return Objects.equals(source_date_id, that.source_date_id) && Objects.equals(source_date, that.source_date) && Objects.equals(stateEntity, that.stateEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(source_date_id, source_date, stateEntity);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "WebAnnotationSourceDateEntity{source_date_id=" + source_date_id + ", source_date=" + format.format(source_date) + ", stateEntity=" + stateEntity + "}";
	}
}
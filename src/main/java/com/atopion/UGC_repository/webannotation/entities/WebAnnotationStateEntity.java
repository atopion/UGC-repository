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
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Set;

@Entity
@Table(name = "web_annotation_state")
public class WebAnnotationStateEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "state_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int state_id;

	@Size(max = 255)
	@Column(name = "id", columnDefinition = "varchar(255)")
	private String id;

	@Size(max = 255)
	@Column(name = "type", nullable = false, columnDefinition = "varchar(255)")
	private String type;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "sourceDateStart", columnDefinition = "date")
	private Date sourceDateStart;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "sourceDateEnd", columnDefinition = "date")
	private Date sourceDateEnd;

	@Size(max = 5000)
	@Column(name = "value", columnDefinition = "varchar(5000)")
	private String value;

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
	@JoinColumn(name = "refiningState", columnDefinition = "int(10)")
	private WebAnnotationStateEntity refiningState;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "refiningSelector", columnDefinition = "int(10)")
	private WebAnnotationSelectorEntity refiningSelector;

	@OneToMany(mappedBy = "stateEntity", orphanRemoval = true, cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.SourceDateSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.SourceDateEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationSourceDateEntity> sourceDate;

	@OneToMany(mappedBy = "stateEntity", orphanRemoval = true, cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.CachedSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.CachedEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationCachedEntity> cached;

	protected WebAnnotationStateEntity() {}

	public WebAnnotationStateEntity(int state_id, String id, String type, Date sourceDateStart, Date sourceDateEnd, String value, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity, WebAnnotationStateEntity refiningState, WebAnnotationSelectorEntity refiningSelector, Set<WebAnnotationSourceDateEntity> sourceDate, Set<WebAnnotationCachedEntity> cached) {
		this.state_id = state_id;
		this.id = id;
		this.type = type;
		this.sourceDateStart = sourceDateStart;
		this.sourceDateEnd = sourceDateEnd;
		this.value = value;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
		this.refiningState = refiningState;
		this.refiningSelector = refiningSelector;
		this.sourceDate = sourceDate;
		this.cached = cached;
	}

	public WebAnnotationStateEntity(String id, String type, Date sourceDateStart, Date sourceDateEnd, String value, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity, WebAnnotationStateEntity refiningState, WebAnnotationSelectorEntity refiningSelector, Set<WebAnnotationSourceDateEntity> sourceDate, Set<WebAnnotationCachedEntity> cached) {
		this.id = id;
		this.type = type;
		this.sourceDateStart = sourceDateStart;
		this.sourceDateEnd = sourceDateEnd;
		this.value = value;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
		this.refiningState = refiningState;
		this.refiningSelector = refiningSelector;
		this.sourceDate = sourceDate;
		this.cached = cached;
	}

	public void setStateId(int state_id) {
		this.state_id = state_id;
	}

	@JsonIgnore
	public int getStateId() {
		return this.state_id;
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

	public void setSourceDateStart(Date sourceDateStart) {
		this.sourceDateStart = sourceDateStart;
	}

	public Date getSourceDateStart() {
		return this.sourceDateStart;
	}

	public void setSourceDateEnd(Date sourceDateEnd) {
		this.sourceDateEnd = sourceDateEnd;
	}

	public Date getSourceDateEnd() {
		return this.sourceDateEnd;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
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

	public void setRefiningState(WebAnnotationStateEntity refiningState) {
		this.refiningState = refiningState;
	}

	public WebAnnotationStateEntity getRefiningState() {
		return this.refiningState;
	}

	public void setRefiningSelector(WebAnnotationSelectorEntity refiningSelector) {
		this.refiningSelector = refiningSelector;
	}

	public WebAnnotationSelectorEntity getRefiningSelector() {
		return this.refiningSelector;
	}

	public Set<WebAnnotationSourceDateEntity> getSourceDate() {
		return sourceDate;
	}

	public void setSourceDate(Set<WebAnnotationSourceDateEntity> sourceDate) {
		this.sourceDate = sourceDate;
	}

	@JsonSetter("sourceDate")
	public void setSingleSourceDate(WebAnnotationSourceDateEntity sourceDate) {
		this.sourceDate = Set.of(sourceDate);
	}

	public Set<WebAnnotationCachedEntity> getCached() {
		return cached;
	}

	public void setCached(Set<WebAnnotationCachedEntity> cached) {
		this.cached = cached;
	}

	@JsonSetter("cached")
	public void setSingleCached(WebAnnotationCachedEntity cached) {
		this.cached = Set.of(cached);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationStateEntity)) return false;
		WebAnnotationStateEntity that = (WebAnnotationStateEntity) o;
		return Objects.equals(state_id, that.state_id) && Objects.equals(id, that.id) && Objects.equals(type, that.type) && Objects.equals(sourceDateStart, that.sourceDateStart) && Objects.equals(sourceDateEnd, that.sourceDateEnd) && Objects.equals(value, that.value) && Objects.equals(bodyEntity, that.bodyEntity) && Objects.equals(targetEntity, that.targetEntity) && Objects.equals(refiningState, that.refiningState) && Objects.equals(refiningSelector, that.refiningSelector);
	}

	@Override
	public int hashCode() {
		return Objects.hash(state_id, id, type, sourceDateStart, sourceDateEnd, value, bodyEntity, targetEntity, refiningState, refiningSelector);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "WebAnnotationStateEntity{state_id=" + state_id + ", id='" + id + "', type='" + type + "', sourceDateStart=" + format.format(sourceDateStart) + ", sourceDateEnd=" + format.format(sourceDateEnd) + ", value='" + value + "', bodyEntity=" + bodyEntity + ", targetEntity=" + targetEntity + ", refiningState=" + refiningState + ", refiningSelector=" + refiningSelector + "}";
	}
}
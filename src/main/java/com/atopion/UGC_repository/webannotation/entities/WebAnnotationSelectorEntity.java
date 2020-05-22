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
@Table(name = "web_annotation_selector")
public class WebAnnotationSelectorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "selector_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int selector_id;

	@Size(max = 5000)
	@Column(name = "value", nullable = false, columnDefinition = "varchar(5000)")
	private String value;

	@Size(max = 255)
	@Column(name = "conformsTo", columnDefinition = "varchar(255)")
	private String conformsTo;

	@Size(max = 5000)
	@Column(name = "exact", columnDefinition = "varchar(5000)")
	private String exact;

	@Size(max = 5000)
	@Column(name = "prefix", columnDefinition = "varchar(5000)")
	private String prefix;

	@Size(max = 5000)
	@Column(name = "suffix", columnDefinition = "varchar(5000)")
	private String suffix;

	@Column(name = "startPos", columnDefinition = "int(10)")
	private Integer startPos;

	@Column(name = "endPos", columnDefinition = "int(10)")
	private Integer endPos;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "startSelector", columnDefinition = "int(10)")
	private WebAnnotationSelectorEntity startSelector;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "endSelector", columnDefinition = "int(10)")
	private WebAnnotationSelectorEntity endSelector;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "refiningSelector", columnDefinition = "int(10)")
	private WebAnnotationSelectorEntity refiningSelector;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "target_id", columnDefinition = "int(10)")
	private WebAnnotationTargetEntity targetEntity;

	@OneToMany(mappedBy = "selectorEntity", orphanRemoval = true, cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.TypeSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.TypeEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationTypeEntity> type;

	@OneToMany(mappedBy = "refiningSelector", orphanRemoval = true, cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationStateEntity> refinedByState;

	@OneToMany(mappedBy = "refiningSelector", orphanRemoval = true, cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationSelectorEntity> refinedBySelector;

	@OneToMany(mappedBy = "startSelector", orphanRemoval = true, cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationSelectorEntity> startingSelector;

	@OneToMany(mappedBy = "endSelector", orphanRemoval = true, cascade = CascadeType.PERSIST,fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationSelectorEntity> endingSelector;

	protected WebAnnotationSelectorEntity() {}

	public WebAnnotationSelectorEntity(int selector_id, String value, String conformsTo, String exact, String prefix, String suffix, Integer startPos, Integer endPos, WebAnnotationSelectorEntity startSelector, WebAnnotationSelectorEntity endSelector, WebAnnotationSelectorEntity refiningSelector, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity, Set<WebAnnotationTypeEntity> type, Set<WebAnnotationSelectorEntity> refinedBySelector, Set<WebAnnotationStateEntity> refinedByState, Set<WebAnnotationSelectorEntity> startingSelector, Set<WebAnnotationSelectorEntity> endingSelector) {
		this.selector_id = selector_id;
		this.value = value;
		this.conformsTo = conformsTo;
		this.exact = exact;
		this.prefix = prefix;
		this.suffix = suffix;
		this.startPos = startPos;
		this.endPos = endPos;
		this.startSelector = startSelector;
		this.endSelector = endSelector;
		this.refiningSelector = refiningSelector;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
		this.type = type;
		this.refinedByState = refinedByState;
		this.refinedBySelector = refinedBySelector;
		this.startingSelector = startingSelector;
		this.endingSelector = endingSelector;
	}

	public WebAnnotationSelectorEntity(String value, String conformsTo, String exact, String prefix, String suffix, Integer startPos, Integer endPos, WebAnnotationSelectorEntity startSelector, WebAnnotationSelectorEntity endSelector, WebAnnotationSelectorEntity refiningSelector, WebAnnotationBodyEntity bodyEntity, WebAnnotationTargetEntity targetEntity, Set<WebAnnotationTypeEntity> type, Set<WebAnnotationSelectorEntity> refinedBySelector, Set<WebAnnotationStateEntity> refinedByState, Set<WebAnnotationSelectorEntity> startingSelector, Set<WebAnnotationSelectorEntity> endingSelector) {
		this.value = value;
		this.conformsTo = conformsTo;
		this.exact = exact;
		this.prefix = prefix;
		this.suffix = suffix;
		this.startPos = startPos;
		this.endPos = endPos;
		this.startSelector = startSelector;
		this.endSelector = endSelector;
		this.refiningSelector = refiningSelector;
		this.bodyEntity = bodyEntity;
		this.targetEntity = targetEntity;
		this.type = type;
		this.refinedByState = refinedByState;
		this.startingSelector = startingSelector;
		this.endingSelector = endingSelector;
	}

	public void setSelectorId(int selector_id) {
		this.selector_id = selector_id;
	}

	@JsonIgnore
	public int getSelectorId() {
		return this.selector_id;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setConformsTo(String conformsTo) {
		this.conformsTo = conformsTo;
	}

	public String getConformsTo() {
		return this.conformsTo;
	}

	public void setExact(String exact) {
		this.exact = exact;
	}

	public String getExact() {
		return this.exact;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public String getSuffix() {
		return this.suffix;
	}

	public void setStartPos(Integer startPos) {
		this.startPos = startPos;
	}

	public Integer getStartPos() {
		return this.startPos;
	}

	public void setEndPos(Integer endPos) {
		this.endPos = endPos;
	}

	public Integer getEndPos() {
		return this.endPos;
	}

	public void setStartSelector(WebAnnotationSelectorEntity startSelector) {
		this.startSelector = startSelector;
	}

	public WebAnnotationSelectorEntity getStartSelector() {
		return this.startSelector;
	}

	public void setEndSelector(WebAnnotationSelectorEntity endSelector) {
		this.endSelector = endSelector;
	}

	public WebAnnotationSelectorEntity getEndSelector() {
		return this.endSelector;
	}

	public void setRefiningSelector(WebAnnotationSelectorEntity refiningSelector) {
		this.refiningSelector = refiningSelector;
	}

	public WebAnnotationSelectorEntity getRefiningSelector() {
		return this.refiningSelector;
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

	public Set<WebAnnotationStateEntity> getRefinedByState() {
		return refinedByState;
	}

	public void setRefinedByState(Set<WebAnnotationStateEntity> refinedByState) {
		this.refinedByState = refinedByState;
	}

	@JsonSetter("refinedByState")
	public void setSingleRefinedBy(WebAnnotationStateEntity refinedBy) {
		this.refinedByState = Set.of(refinedBy);
	}

	public Set<WebAnnotationSelectorEntity> getRefinedBySelector() {
		return refinedBySelector;
	}

	public void setRefinedBySelector(Set<WebAnnotationSelectorEntity> refinedBySelector) {
		this.refinedBySelector = refinedBySelector;
	}

	@JsonSetter("refinedBySelector")
	public void setSingleRefinedBySelector(WebAnnotationSelectorEntity refinedBySelector) {
		this.refinedBySelector = Set.of(refinedBySelector);
	}

	public Set<WebAnnotationSelectorEntity> getStartingSelector() {
		return startingSelector;
	}

	public void setStartingSelector(Set<WebAnnotationSelectorEntity> startingSelector) {
		this.startingSelector = startingSelector;
	}

	@JsonSetter("startingSelector")
	public void setSingleStartingSelector(WebAnnotationSelectorEntity startingSelector) {
		this.startingSelector = Set.of(startingSelector);
	}

	public Set<WebAnnotationSelectorEntity> getEndingSelector() {
		return endingSelector;
	}

	public void setEndingSelector(Set<WebAnnotationSelectorEntity> endingSelector) {
		this.endingSelector = endingSelector;
	}

	@JsonSetter("endingSelector")
	public void setSingleEndingSelector(WebAnnotationSelectorEntity endingSelector) {
		this.endingSelector = Set.of(endingSelector);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationSelectorEntity)) return false;
		WebAnnotationSelectorEntity that = (WebAnnotationSelectorEntity) o;
		return Objects.equals(selector_id, that.selector_id) && Objects.equals(value, that.value) && Objects.equals(conformsTo, that.conformsTo) && Objects.equals(exact, that.exact) && Objects.equals(prefix, that.prefix) && Objects.equals(suffix, that.suffix) && Objects.equals(startPos, that.startPos) && Objects.equals(endPos, that.endPos) && Objects.equals(startSelector, that.startSelector) && Objects.equals(endSelector, that.endSelector) && Objects.equals(refiningSelector, that.refiningSelector) && Objects.equals(bodyEntity, that.bodyEntity) && Objects.equals(targetEntity, that.targetEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(selector_id, value, conformsTo, exact, prefix, suffix, startPos, endPos, startSelector, endSelector, refiningSelector, bodyEntity, targetEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationSelectorEntity{selector_id=" + selector_id + ", value='" + value + "', conformsTo='" + conformsTo + "', exact='" + exact + "', prefix='" + prefix + "', suffix='" + suffix + "', startPos=" + startPos + ", endPos=" + endPos + ", startSelector=" + startSelector + ", endSelector=" + endSelector + ", refiningSelector=" + refiningSelector + ", bodyEntity=" + bodyEntity + ", targetEntity=" + targetEntity + "}";
	}
}
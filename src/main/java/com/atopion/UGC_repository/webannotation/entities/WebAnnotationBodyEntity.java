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
import org.springframework.boot.WebApplicationType;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Set;

@Entity
@Table(name = "web_annotation_body")
public class WebAnnotationBodyEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "body_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int body_id;

	@Size(max = 255)
	@Column(name = "id", unique = true, columnDefinition = "varchar(255)")
	private String id;

	@Size(max = 5)
	@Column(name = "processing_language", columnDefinition = "varchar(5)")
	private String processing_language;

	@Size(max = 5)
	@Column(name = "text_direction", columnDefinition = "varchar(5)")
	private String text_direction;

	@Size(max = 5000)
	@Column(name = "value", columnDefinition = "varchar(5000)")
	private String value;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "modified", columnDefinition = "date")
	private Date modified;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "created", columnDefinition = "date")
	private Date created;

	@Size(max = 255)
	@Column(name = "canonical", columnDefinition = "varchar(255)")
	private String canonical;

	@Size(max = 255)
	@Column(name = "source", columnDefinition = "varchar(255)")
	private String source;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "annotation_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationEntity annotationEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "choice_id", columnDefinition = "int(10)")
	private WebAnnotationBodyEntity choiceEntity;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.AgentEntity.class)
	private Set<WebAnnotationAgentEntity> creator;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.ViaSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.ViaEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationViaEntity> via;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.StateEntity.class)
	private Set<WebAnnotationStateEntity> state;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.TypeSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.TypeEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationTypeEntity> type;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.LanguageSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.LanguageEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationLanguageEntity> language;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.FormatSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.FormatEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationFormatEntity> format;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.AccessibilitySetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.AccessibilityEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationAccessibilityEntity> accessibility;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonDeserialize(using = JSONDeserializer.ViaEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationRenderedViaEntity> renderedVia;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.PurposeSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.PurposeEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationPurposeEntity> purpose;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.ScopeSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.ScopeEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationScopeEntity> scope;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.StyleClassSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.StyleClassEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationStyleClassEntity> styleClass;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonDeserialize(using = JSONDeserializer.SelectorEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationSelectorEntity> selector;

	@OneToMany(mappedBy = "bodyEntity", orphanRemoval = true, cascade = { CascadeType.PERSIST, CascadeType.ALL }, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.RightsSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.RightsEntity.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	private Set<WebAnnotationRightsEntity> rights;

	protected WebAnnotationBodyEntity() {}

	public WebAnnotationBodyEntity(int body_id, String id, String processing_language, String text_direction, String value, Date modified, Date created, String canonical, String source, WebAnnotationEntity annotationEntity, WebAnnotationBodyEntity choiceEntity, Set<WebAnnotationAgentEntity> creator, Set<WebAnnotationViaEntity> via, Set<WebAnnotationStateEntity> state, Set<WebAnnotationTypeEntity> type, Set<WebAnnotationLanguageEntity> language, Set<WebAnnotationFormatEntity> format, Set<WebAnnotationAccessibilityEntity> accessibility, Set<WebAnnotationRenderedViaEntity> renderedVia, Set<WebAnnotationPurposeEntity> purpose, Set<WebAnnotationScopeEntity> scope, Set<WebAnnotationStyleClassEntity> styleClass, Set<WebAnnotationSelectorEntity> selector, Set<WebAnnotationRightsEntity> rights) {
		this.body_id = body_id;
		this.id = id;
		this.processing_language = processing_language;
		this.text_direction = text_direction;
		this.value = value;
		this.modified = modified;
		this.created = created;
		this.canonical = canonical;
		this.source = source;
		this.annotationEntity = annotationEntity;
		this.choiceEntity = choiceEntity;
		this.creator = creator;
		this.via = via;
		this.state = state;
		this.type = type;
		this.language = language;
		this.format = format;
		this.accessibility = accessibility;
		this.renderedVia = renderedVia;
		this.purpose = purpose;
		this.scope = scope;
		this.styleClass = styleClass;
		this.selector = selector;
		this.rights = rights;
	}

	public WebAnnotationBodyEntity(String id, String processing_language, String text_direction, String value, Date modified, Date created, String canonical, String source, WebAnnotationEntity annotationEntity, WebAnnotationBodyEntity choiceEntity, Set<WebAnnotationAgentEntity> creator, Set<WebAnnotationViaEntity> via, Set<WebAnnotationStateEntity> state, Set<WebAnnotationTypeEntity> type, Set<WebAnnotationLanguageEntity> language, Set<WebAnnotationFormatEntity> format, Set<WebAnnotationAccessibilityEntity> accessibility, Set<WebAnnotationRenderedViaEntity> renderedVia, Set<WebAnnotationPurposeEntity> purpose, Set<WebAnnotationScopeEntity> scope, Set<WebAnnotationStyleClassEntity> styleClass, Set<WebAnnotationSelectorEntity> selector, Set<WebAnnotationRightsEntity> rights) {
		this.id = id;
		this.processing_language = processing_language;
		this.text_direction = text_direction;
		this.value = value;
		this.modified = modified;
		this.created = created;
		this.canonical = canonical;
		this.source = source;
		this.annotationEntity = annotationEntity;
		this.choiceEntity = choiceEntity;
		this.creator = creator;
		this.via = via;
		this.state = state;
		this.type = type;
		this.language = language;
		this.format = format;
		this.accessibility = accessibility;
		this.renderedVia = renderedVia;
		this.purpose = purpose;
		this.scope = scope;
		this.styleClass = styleClass;
		this.selector = selector;
		this.rights = rights;
	}

	public void setBodyId(int body_id) {
		this.body_id = body_id;
	}

	@JsonIgnore
	public int getBodyId() {
		return this.body_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setProcessing_language(String processing_language) {
		this.processing_language = processing_language;
	}

	public String getProcessing_language() {
		return this.processing_language;
	}

	public void setText_direction(String text_direction) {
		this.text_direction = text_direction;
	}

	public String getText_direction() {
		return this.text_direction;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getModified() {
		return this.modified;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setCanonical(String canonical) {
		this.canonical = canonical;
	}

	public String getCanonical() {
		return this.canonical;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}

	public void setAnnotationEntity(WebAnnotationEntity annotationEntity) {
		this.annotationEntity = annotationEntity;
	}

	public WebAnnotationEntity getAnnotationEntity() {
		return this.annotationEntity;
	}

	public void setChoiceEntity(WebAnnotationBodyEntity choiceEntity) {
		this.choiceEntity = choiceEntity;
	}

	public WebAnnotationBodyEntity getChoiceEntity() {
		return this.choiceEntity;
	}

	public Set<WebAnnotationAgentEntity> getCreator() {
		return creator;
	}

	public void setCreator(Set<WebAnnotationAgentEntity> creator) {
		this.creator = creator;
	}

	@JsonSetter("creator")
	public void setSingleCreator(WebAnnotationAgentEntity creator) {
		this.creator = Set.of(creator);
	}

	public Set<WebAnnotationViaEntity> getVia() {
		return via;
	}

	public void setVia(Set<WebAnnotationViaEntity> via) {
		this.via = via;
	}

	@JsonSetter("via")
	public void setSingleVia(WebAnnotationViaEntity via) {
		this.via = Set.of(via);
	}

	public Set<WebAnnotationStateEntity> getState() {
		return state;
	}

	public void setState(Set<WebAnnotationStateEntity> state) {
		this.state = state;
	}

	@JsonSetter("state")
	public void setSingleState(WebAnnotationStateEntity state) {
		this.state = Set.of(state);
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

	public Set<WebAnnotationLanguageEntity> getLanguage() {
		return language;
	}

	public void setLanguage(Set<WebAnnotationLanguageEntity> language) {
		this.language = language;
	}

	@JsonSetter("language")
	public void setSingleLanguage(WebAnnotationLanguageEntity language) {
		this.language = Set.of(language);
	}

	public Set<WebAnnotationFormatEntity> getFormat() {
		return format;
	}

	public void setFormat(Set<WebAnnotationFormatEntity> format) {
		this.format = format;
	}

	@JsonSetter("format")
	public void setSingleFormat(WebAnnotationFormatEntity format) {
		this.format = Set.of(format);
	}

	public Set<WebAnnotationAccessibilityEntity> getAccessibility() {
		return accessibility;
	}

	public void setAccessibility(Set<WebAnnotationAccessibilityEntity> accessibility) {
		this.accessibility = accessibility;
	}

	@JsonSetter("accessibility")
	public void setSingleAccessibility(WebAnnotationAccessibilityEntity accessibility) {
		this.accessibility = Set.of(accessibility);
	}

	public Set<WebAnnotationRenderedViaEntity> getRenderedVia() {
		return renderedVia;
	}

	public void setRenderedVia(Set<WebAnnotationRenderedViaEntity> renderedVia) {
		this.renderedVia = renderedVia;
	}

	@JsonSetter("renderedVia")
	public void setSingleRenderedVia(WebAnnotationRenderedViaEntity renderedVia) {
		this.renderedVia = Set.of(renderedVia);
	}

	public Set<WebAnnotationPurposeEntity> getPurpose() {
		return purpose;
	}

	public void setPurpose(Set<WebAnnotationPurposeEntity> purpose) {
		this.purpose = purpose;
	}

	@JsonSetter("purpose")
	public void setSinglePurpose(WebAnnotationPurposeEntity purpose) {
		this.purpose = Set.of(purpose);
	}

	public Set<WebAnnotationScopeEntity> getScope() {
		return scope;
	}

	public void setScope(Set<WebAnnotationScopeEntity> scope) {
		this.scope = scope;
	}

	@JsonSetter("scope")
	public void setSingleScope(WebAnnotationScopeEntity scope) {
		this.scope = Set.of(scope);
	}

	public Set<WebAnnotationStyleClassEntity> getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(Set<WebAnnotationStyleClassEntity> styleClass) {
		this.styleClass = styleClass;
	}

	@JsonSetter("styleClass")
	public void setSingleStyleClass(WebAnnotationStyleClassEntity styleClass) {
		this.styleClass = Set.of(styleClass);
	}

	public Set<WebAnnotationSelectorEntity> getSelector() {
		return selector;
	}

	public void setSelector(Set<WebAnnotationSelectorEntity> selector) {
		this.selector = selector;
	}

	@JsonSetter("selector")
	public void setSingleSelector(WebAnnotationSelectorEntity selector) {
		this.selector = Set.of(selector);
	}

	public Set<WebAnnotationRightsEntity> getRights() {
		return rights;
	}

	public void setRights(Set<WebAnnotationRightsEntity> rights) {
		this.rights = rights;
	}

	@JsonSetter("rights")
	public void setSingleRights(WebAnnotationRightsEntity rights) {
		this.rights = Set.of(rights);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationBodyEntity)) return false;
		WebAnnotationBodyEntity that = (WebAnnotationBodyEntity) o;
		return Objects.equals(body_id, that.body_id) && Objects.equals(id, that.id) && Objects.equals(processing_language, that.processing_language) && Objects.equals(text_direction, that.text_direction) && Objects.equals(value, that.value) && Objects.equals(modified, that.modified) && Objects.equals(created, that.created) && Objects.equals(canonical, that.canonical) && Objects.equals(source, that.source) && Objects.equals(annotationEntity, that.annotationEntity) && Objects.equals(choiceEntity, that.choiceEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(body_id, id, processing_language, text_direction, value, modified, created, canonical, source, annotationEntity, choiceEntity);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "WebAnnotationBodyEntity{body_id=" + body_id + ", id='" + id + "', processing_language='" + processing_language + "', text_direction='" + text_direction + "', value='" + value + "', modified=" + (modified != null ? format.format(modified) : "null") + ", created=" + (created != null ? format.format(created) : "null") + ", canonical='" + canonical + "', source='" + source + "}"; //+ "', annotationEntity=" + annotationEntity + ", choiceEntity=" + choiceEntity + "}";
	}
}
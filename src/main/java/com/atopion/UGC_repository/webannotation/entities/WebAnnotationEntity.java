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
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Set;

@Entity
@Table(name = "web_annotation")
//@JsonDeserialize(using = JSONDeserializer.AnnotationEntity.class)
public class WebAnnotationEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "web_annotation_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int web_annotation_id;

	@Size(max = 255)
	@Column(name = "id", nullable = false, unique = true, columnDefinition = "varchar(255)")
	private String id;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "created", columnDefinition = "date")
	private Date created;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "modified", columnDefinition = "date")
	private Date modified;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "generated", columnDefinition = "date")
	private Date generated;

	@Size(max = 5000)
	@Column(name = "bodyValue", columnDefinition = "varchar(5000)")
	private String bodyValue;

	@Size(max = 500)
	@Column(name = "canonical", columnDefinition = "varchar(500)")
	private String canonical;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.AgentEntity.class)
	private Set<WebAnnotationAgentEntity> generator;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.MotivationSetSerializer.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.MotivationEntity.class)
	private Set<WebAnnotationMotivationEntity> motivation;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.ContextSetSerializer.class)
	@JsonDeserialize(using = JSONDeserializer.ContextEntity.class)
	@JacksonXmlProperty(localName = "context")
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonProperty("@context")
	private Set<WebAnnotationContextEntity> context;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.AgentEntity.class)
	private Set<WebAnnotationAgentEntity> creator;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.ViaSetSerializer.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.ViaEntity.class)
	private Set<WebAnnotationViaEntity> via;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.BodyEntity.class)
	private Set<WebAnnotationBodyEntity> body;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.TargetEntity.class)
	private Set<WebAnnotationTargetEntity> target;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.RightsSetSerializer.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.RightsEntity.class)
	private Set<WebAnnotationRightsEntity> rights;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.AudienceEntity.class)
	private Set<WebAnnotationAudienceEntity> audience;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.TypeSetSerializer.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.TypeEntity.class)
	private Set<WebAnnotationTypeEntity> type;

	@OneToMany(mappedBy = "annotationEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.StylesheetEntity.class)
	private Set<WebAnnotationStylesheetEntity> stylesheet;

	protected WebAnnotationEntity() {}

	public WebAnnotationEntity(int web_annotation_id, String id, Date created, Date modified, Date generated, String bodyValue, String canonical, Set<WebAnnotationAgentEntity> generator, Set<WebAnnotationMotivationEntity> motivation, Set<WebAnnotationContextEntity> context, Set<WebAnnotationAgentEntity> creator, Set<WebAnnotationViaEntity> via, Set<WebAnnotationBodyEntity> body, Set<WebAnnotationTargetEntity> target, Set<WebAnnotationRightsEntity> rights, Set<WebAnnotationAudienceEntity> audience, Set<WebAnnotationTypeEntity> type, Set<WebAnnotationStylesheetEntity> stylesheet) {
		this.web_annotation_id = web_annotation_id;
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.generated = generated;
		this.bodyValue = bodyValue;
		this.canonical = canonical;
		this.generator = generator;
		this.motivation = motivation;
		this.context = context;
		this.creator = creator;
		this.via = via;
		this.body = body;
		this.target = target;
		this.rights = rights;
		this.audience = audience;
		this.type = type;
		this.stylesheet = stylesheet;
	}

	public WebAnnotationEntity(String id, Date created, Date modified, Date generated, String bodyValue, String canonical, Set<WebAnnotationAgentEntity> generator, Set<WebAnnotationMotivationEntity> motivation, Set<WebAnnotationContextEntity> context, Set<WebAnnotationAgentEntity> creator, Set<WebAnnotationViaEntity> via, Set<WebAnnotationBodyEntity> body, Set<WebAnnotationTargetEntity> target, Set<WebAnnotationRightsEntity> rights, Set<WebAnnotationAudienceEntity> audience, Set<WebAnnotationTypeEntity> type, Set<WebAnnotationStylesheetEntity> stylesheet) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.generated = generated;
		this.bodyValue = bodyValue;
		this.canonical = canonical;
		this.generator = generator;
		this.motivation = motivation;
		this.context = context;
		this.creator = creator;
		this.via = via;
		this.body = body;
		this.target = target;
		this.rights = rights;
		this.audience = audience;
		this.type = type;
		this.stylesheet = stylesheet;
	}

	public void setWebAnnotationId(int web_annotation_id) {
		this.web_annotation_id = web_annotation_id;
	}

	@JsonIgnore
	public int getWebAnnotationId() {
		return this.web_annotation_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCreated() {
		return this.created;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Date getModified() {
		return this.modified;
	}

	public void setGenerated(Date generated) {
		this.generated = generated;
	}

	public Date getGenerated() {
		return this.generated;
	}

	public void setBodyValue(String bodyValue) {
		this.bodyValue = bodyValue;
	}

	public String getBodyValue() {
		return this.bodyValue;
	}

	public void setCanonical(String canonical) {
		this.canonical = canonical;
	}

	public String getCanonical() {
		return this.canonical;
	}

	public Set<WebAnnotationAgentEntity> getGenerator() {
		return generator;
	}

	public void setGenerator(Set<WebAnnotationAgentEntity> generator) {
		this.generator = generator;
	}

	@JsonSetter("generator")
	public void setSingleGenerator(WebAnnotationAgentEntity generator) {
		this.generator = Set.of(generator);
	}

	public Set<WebAnnotationMotivationEntity> getMotivation() {
		return motivation;
	}

	public void setMotivation(Set<WebAnnotationMotivationEntity> motivation) {
		this.motivation = motivation;
	}

	@JsonSetter("motivation")
	public void setSingleMotivation(WebAnnotationMotivationEntity motivation) {
		this.motivation = Set.of(motivation);
	}

	public Set<WebAnnotationContextEntity> getContext() {
		return context;
	}

	public void setContext(Set<WebAnnotationContextEntity> context) {
		this.context = context;
	}

	@JsonSetter("context")
	public void setSingleContext(WebAnnotationContextEntity context) {
		this.context = Set.of(context);
	}

	@JsonSetter("@context")
	public void setSingleAtContext(WebAnnotationContextEntity context) {
		this.context = Set.of(context);
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

	public Set<WebAnnotationBodyEntity> getBody() {
		return body;
	}

	public void setBody(Set<WebAnnotationBodyEntity> body) {
		this.body = body;
	}

	@JsonSetter("body")
	public void setSingleBody(WebAnnotationBodyEntity body) {
		this.body = Set.of(body);
	}

	public Set<WebAnnotationTargetEntity> getTarget() {
		return target;
	}

	public void setTarget(Set<WebAnnotationTargetEntity> target) {
		this.target = target;
	}

	@JsonSetter("target")
	public void setSingleTarget(WebAnnotationTargetEntity target) {
		this.target = Set.of(target);
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

	public Set<WebAnnotationAudienceEntity> getAudience() {
		return audience;
	}

	public void setAudience(Set<WebAnnotationAudienceEntity> audience) {
		this.audience = audience;
	}

	@JsonSetter("audience")
	public void setSingleAudience(WebAnnotationAudienceEntity audience) {
		this.audience = Set.of(audience);
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

	public Set<WebAnnotationStylesheetEntity> getStylesheet() {
		return stylesheet;
	}

	public void setStylesheet(Set<WebAnnotationStylesheetEntity> stylesheet) {
		this.stylesheet = stylesheet;
	}

	@JsonSetter("stylesheet")
	public void setSingleStylesheet(WebAnnotationStylesheetEntity stylesheet) {
		this.stylesheet = Set.of(stylesheet);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationEntity)) return false;
		WebAnnotationEntity that = (WebAnnotationEntity) o;
		return Objects.equals(web_annotation_id, that.web_annotation_id) && Objects.equals(id, that.id) && Objects.equals(created, that.created) && Objects.equals(modified, that.modified) && Objects.equals(generated, that.generated) && Objects.equals(bodyValue, that.bodyValue) && Objects.equals(canonical, that.canonical);
	}

	@Override
	public int hashCode() {
		return Objects.hash(web_annotation_id, id, created, modified, generated, bodyValue, canonical);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "WebAnnotationEntity{web_annotation_id=" + web_annotation_id +
				", id='" + id +
				"', created=" + format.format(created) +
				", modified=" + format.format(modified) +
				", generated=" + format.format(generated) +
				", bodyValue='" + bodyValue +
				"', canonical='" + canonical +
				"', generator='" + generator +
				"', motivation='" + motivation +
				"', @context='" + context +
				"', creator='" + creator +
				"', via='" + via +
				"', body='" + body +
				"', target='" + target +
				"', rights='" + rights +
				"', audience='" + audience +
				"', type='" + type +
				"', stylesheet='" + stylesheet +
				"}";
	}
}
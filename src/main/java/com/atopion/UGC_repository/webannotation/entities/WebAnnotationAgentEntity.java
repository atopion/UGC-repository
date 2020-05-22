package com.atopion.UGC_repository.webannotation.entities;

import com.atopion.UGC_repository.util.JSONDeserializer;
import com.atopion.UGC_repository.util.JSONSetSerializer;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.Set;

@Entity
@Table(name = "web_annotation_agent")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebAnnotationAgentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@JoinColumn(name = "agent_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int agent_id;

	@Size(max = 255)
	@Column(name = "id", unique = true, columnDefinition = "varchar(255)")
	private String id;

	@Size(max = 255)
	@Column(name = "nickname", columnDefinition = "varchar(255)")
	private String nickname;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "annotation_id", columnDefinition = "int(10)")
	private WebAnnotationEntity annotationEntity;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "body_id", columnDefinition = "int(10)")
	private WebAnnotationBodyEntity bodyEntity;

	@OneToMany(mappedBy = "agentEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.AgentNameSetSerializer.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.AgentNameEntity.class)
	private Set<WebAnnotationAgentNameEntity> name;

	@OneToMany(mappedBy = "agentEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.AgentEmailSetSerializer.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.AgentEmailEntity.class)
	private Set<WebAnnotationAgentEmailEntity> email;

	@OneToMany(mappedBy = "agentEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.EmailSha1SetSerializer.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.EmailSha1Entity.class)
	private Set<WebAnnotationEmailSha1Entity> emailsSha1;

	@OneToMany(mappedBy = "agentEntity", orphanRemoval = true, cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
	@JsonSerialize(using = JSONSetSerializer.AgentHomepageSetSerializer.class)
	@JacksonXmlElementWrapper(useWrapping = false)
	@JsonDeserialize(using = JSONDeserializer.AgentHomepageEntity.class)
	private Set<WebAnnotationAgentHomepageEntity> homepage;

	protected WebAnnotationAgentEntity() {}

	public WebAnnotationAgentEntity(int agent_id, String id, String nickname, WebAnnotationEntity annotationEntity, WebAnnotationBodyEntity bodyEntity, Set<WebAnnotationAgentNameEntity> names, Set<WebAnnotationAgentEmailEntity> emails, Set<WebAnnotationEmailSha1Entity> emailsSha1s, Set<WebAnnotationAgentHomepageEntity> homepages) {
		this.agent_id = agent_id;
		this.id = id;
		this.nickname = nickname;
		this.annotationEntity = annotationEntity;
		this.bodyEntity = bodyEntity;
		this.name = names;
		this.email = emails;
		this.emailsSha1 = emailsSha1s;
		this.homepage = homepages;
	}

	public WebAnnotationAgentEntity(String id, String nickname, WebAnnotationEntity annotationEntity, WebAnnotationBodyEntity bodyEntity, Set<WebAnnotationAgentNameEntity> names, Set<WebAnnotationAgentEmailEntity> emails, Set<WebAnnotationEmailSha1Entity> emailsSha1s, Set<WebAnnotationAgentHomepageEntity> homepages) {
		this.id = id;
		this.nickname = nickname;
		this.annotationEntity = annotationEntity;
		this.bodyEntity = bodyEntity;
		this.name = names;
		this.email = emails;
		this.emailsSha1 = emailsSha1s;
		this.homepage = homepages;
	}

	public void setAgentId(int agent_id) {
		this.agent_id = agent_id;
	}

	@JsonIgnore
	public int getAgentId() {
		return this.agent_id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setAnnotationEntity(WebAnnotationEntity annotationEntity) {
		this.annotationEntity = annotationEntity;
	}

	public WebAnnotationEntity getAnnotationEntity() {
		return this.annotationEntity;
	}

	public void setBodyEntity(WebAnnotationBodyEntity bodyEntity) {
		this.bodyEntity = bodyEntity;
	}

	public WebAnnotationBodyEntity getBodyEntity() {
		return this.bodyEntity;
	}

	public Set<WebAnnotationAgentNameEntity> getName() {
		return name;
	}

	public void setName(Set<WebAnnotationAgentNameEntity> names) {
		this.name = names;
	}

	@JsonSetter("name")
	public void setSingleName(WebAnnotationAgentNameEntity name) {
		this.name = Set.of(name);
	}

	public Set<WebAnnotationAgentEmailEntity> getEmail() {
		return email;
	}

	public void setEmail(Set<WebAnnotationAgentEmailEntity> emails) {
		this.email = emails;
	}

	@JsonSetter("email")
	public void setSingleEmail(WebAnnotationAgentEmailEntity email) {
		this.email = Set.of(email);
	}

	public Set<WebAnnotationEmailSha1Entity> getEmailsSha1() {
		return emailsSha1;
	}

	public void setEmailsSha1(Set<WebAnnotationEmailSha1Entity> emailsSha1s) {
		this.emailsSha1 = emailsSha1s;
	}

	@JsonSetter("emailsSha1")
	public void setSingleEmailsSha1(WebAnnotationEmailSha1Entity emailsSha1) {
		this.emailsSha1 = Set.of(emailsSha1);
	}

	public Set<WebAnnotationAgentHomepageEntity> getHomepage() {
		return homepage;
	}

	public void setHomepage(Set<WebAnnotationAgentHomepageEntity> homepages) {
		this.homepage = homepages;
	}

	@JsonSetter("homepage")
	public void setSingleHomepage(WebAnnotationAgentHomepageEntity homepage) {
		this.homepage = Set.of(homepage);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationAgentEntity)) return false;
		WebAnnotationAgentEntity that = (WebAnnotationAgentEntity) o;
		return Objects.equals(agent_id, that.agent_id) && Objects.equals(id, that.id) && Objects.equals(nickname, that.nickname) && Objects.equals(annotationEntity, that.annotationEntity) && Objects.equals(bodyEntity, that.bodyEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agent_id, id, nickname, annotationEntity, bodyEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationAgentEntity{agent_id=" + agent_id + ", id='" + id + "', nickname='" + nickname + "'}"; //"', annotationEntity=" + annotationEntity + ", bodyEntity=" + bodyEntity + "}";
	}
}
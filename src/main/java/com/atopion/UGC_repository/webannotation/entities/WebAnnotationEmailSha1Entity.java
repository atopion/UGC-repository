package com.atopion.UGC_repository.webannotation.entities;

import com.fasterxml.jackson.annotation.JacksonInject;
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
@Table(name = "web_annotation_email_sha1")
public class WebAnnotationEmailSha1Entity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "agent_email_sha1_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int agent_email_sha1_id;

	@Size(max = 255)
	@Column(name = "email_sha1", nullable = false, columnDefinition = "varchar(255)")
	private String email_sha1;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "agent_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationAgentEntity agentEntity;

	protected WebAnnotationEmailSha1Entity() {}

	public WebAnnotationEmailSha1Entity(int agent_email_sha1_id, String email_sha1, @JacksonInject WebAnnotationAgentEntity agentEntity) {
		this.agent_email_sha1_id = agent_email_sha1_id;
		this.email_sha1 = email_sha1;
		this.agentEntity = agentEntity;
	}

	public WebAnnotationEmailSha1Entity(String email_sha1, @JacksonInject WebAnnotationAgentEntity agentEntity) {
		this.email_sha1 = email_sha1;
		this.agentEntity = agentEntity;
	}

	public void setAgent_email_sha1_id(int agent_email_sha1_id) {
		this.agent_email_sha1_id = agent_email_sha1_id;
	}

	public int getAgent_email_sha1_id() {
		return this.agent_email_sha1_id;
	}

	public void setEmail_sha1(String email_sha1) {
		this.email_sha1 = email_sha1;
	}

	public String getEmail_sha1() {
		return this.email_sha1;
	}

	public void setAgentEntity(WebAnnotationAgentEntity agentEntity) {
		this.agentEntity = agentEntity;
	}

	public WebAnnotationAgentEntity getAgentEntity() {
		return this.agentEntity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof WebAnnotationEmailSha1Entity)) return false;
		WebAnnotationEmailSha1Entity that = (WebAnnotationEmailSha1Entity) o;
		return Objects.equals(agent_email_sha1_id, that.agent_email_sha1_id) && Objects.equals(email_sha1, that.email_sha1) && Objects.equals(agentEntity, that.agentEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agent_email_sha1_id, email_sha1, agentEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationEmailSha1Entity{agent_email_sha1_id=" + agent_email_sha1_id + ", email_sha1='" + email_sha1 + "', agentEntity=" + agentEntity + "}";
	}
}
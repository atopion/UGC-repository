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
@Table(name = "web_annotation_agent_email")
public class WebAnnotationAgentEmailEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "agent_email_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int agent_email_id;

	@Size(max = 255)
	@Column(name = "email", nullable = false, columnDefinition = "varchar(255)")
	private String email;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "agent_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationAgentEntity agentEntity;

	protected WebAnnotationAgentEmailEntity() {}

	public WebAnnotationAgentEmailEntity(int agent_email_id, String email, WebAnnotationAgentEntity agentEntity) {
		this.agent_email_id = agent_email_id;
		this.email = email;
		this.agentEntity = agentEntity;
	}

	public WebAnnotationAgentEmailEntity(String email, WebAnnotationAgentEntity agentEntity) {
		this.email = email;
		this.agentEntity = agentEntity;
	}

	public void setAgent_email_id(int agent_email_id) {
		this.agent_email_id = agent_email_id;
	}

	public int getAgent_email_id() {
		return this.agent_email_id;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return this.email;
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
		if (!(o instanceof WebAnnotationAgentEmailEntity)) return false;
		WebAnnotationAgentEmailEntity that = (WebAnnotationAgentEmailEntity) o;
		return Objects.equals(agent_email_id, that.agent_email_id) && Objects.equals(email, that.email) && Objects.equals(agentEntity, that.agentEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agent_email_id, email, agentEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationAgentEmailEntity{agent_email_id=" + agent_email_id + ", email='" + email + "', agentEntity=" + agentEntity + "}";
	}
}
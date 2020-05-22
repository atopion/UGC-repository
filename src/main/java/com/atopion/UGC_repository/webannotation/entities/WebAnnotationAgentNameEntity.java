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
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "web_annotation_agent_name")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebAnnotationAgentNameEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "agent_name_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int agent_name_id;

	@Size(max = 255)
	@Column(name = "name", nullable = false, columnDefinition = "varchar(255)")
	private String name;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "agent_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationAgentEntity agentEntity;

	protected WebAnnotationAgentNameEntity() {}

	public WebAnnotationAgentNameEntity(int agent_name_id, String name, WebAnnotationAgentEntity agentEntity) {
		this.agent_name_id = agent_name_id;
		this.name = name;
		this.agentEntity = agentEntity;
	}

	public WebAnnotationAgentNameEntity(String name, WebAnnotationAgentEntity agentEntity) {
		this.name = name;
		this.agentEntity = agentEntity;
	}

	public void setAgent_name_id(int agent_name_id) {
		this.agent_name_id = agent_name_id;
	}

	public int getAgent_name_id() {
		return this.agent_name_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
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
		if (!(o instanceof WebAnnotationAgentNameEntity)) return false;
		WebAnnotationAgentNameEntity that = (WebAnnotationAgentNameEntity) o;
		return Objects.equals(agent_name_id, that.agent_name_id) && Objects.equals(name, that.name) && Objects.equals(agentEntity, that.agentEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agent_name_id, name, agentEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationAgentNameEntity{agent_name_id=" + agent_name_id + ", name='" + name + "', agentEntity=" + agentEntity + "}";
	}
}
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
@Table(name = "web_annotation_agent_homepage")
public class WebAnnotationAgentHomepageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
	@Column(name = "agent_homepage_id", nullable = false, unique = true, columnDefinition = "int(10)")
	private int agent_homepage_id;

	@Size(max = 255)
	@Column(name = "homepage", nullable = false, columnDefinition = "varchar(255)")
	private String homepage;

	@JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "agent_id", nullable = false, columnDefinition = "int(10)")
	private WebAnnotationAgentEntity agentEntity;

	protected WebAnnotationAgentHomepageEntity() {}

	public WebAnnotationAgentHomepageEntity(int agent_homepage_id, String homepage, @JacksonInject WebAnnotationAgentEntity agentEntity) {
		this.agent_homepage_id = agent_homepage_id;
		this.homepage = homepage;
		this.agentEntity = agentEntity;
	}

	public WebAnnotationAgentHomepageEntity(String homepage, @JacksonInject WebAnnotationAgentEntity agentEntity) {
		this.homepage = homepage;
		this.agentEntity = agentEntity;
	}

	public void setAgent_homepage_id(int agent_homepage_id) {
		this.agent_homepage_id = agent_homepage_id;
	}

	public int getAgent_homepage_id() {
		return this.agent_homepage_id;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getHomepage() {
		return this.homepage;
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
		if (!(o instanceof WebAnnotationAgentHomepageEntity)) return false;
		WebAnnotationAgentHomepageEntity that = (WebAnnotationAgentHomepageEntity) o;
		return Objects.equals(agent_homepage_id, that.agent_homepage_id) && Objects.equals(homepage, that.homepage) && Objects.equals(agentEntity, that.agentEntity);
	}

	@Override
	public int hashCode() {
		return Objects.hash(agent_homepage_id, homepage, agentEntity);
	}

	@Override
	public String toString() {
		return "WebAnnotationAgentHomepageEntity{agent_homepage_id=" + agent_homepage_id + ", homepage='" + homepage + "', agentEntity=" + agentEntity + "}";
	}
}
package com.atopion.UGC_repository.rest.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;

@Entity
@Table(name = "applications")
public class ApplicationsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "application_id", nullable = false, unique = true, columnDefinition = "int")
	private int application_id;

	@Size(max = 100)
	@Column(name = "application_name", nullable = false, unique = true, columnDefinition = "varchar(100)")
	private String application_name;

	protected ApplicationsEntity() {}

	public ApplicationsEntity(int application_id, String application_name) {
		this.application_id = application_id;
		this.application_name = application_name;
	}

	public ApplicationsEntity(String application_name) {
		this.application_name = application_name;
	}

	public void setApplication_id(int application_id) {
		this.application_id = application_id;
	}

	public int getApplication_id() {
		return this.application_id;
	}

	public void setApplication_name(String application_name) {
		this.application_name = application_name;
	}

	public String getApplication_name() {
		return this.application_name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ApplicationsEntity)) return false;
		ApplicationsEntity that = (ApplicationsEntity) o;
		return Objects.equals(application_id, that.application_id) && Objects.equals(application_name, that.application_name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(application_id, application_name);
	}

	@Override
	public String toString() {
		return "ApplicationsEntity{application_id=" + application_id + ", application_name='" + application_name + "'}";
	}
}
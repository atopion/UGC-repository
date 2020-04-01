package com.atopion.UGC_repository.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "records")
public class RecordsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "record_id", nullable = false, unique = true, columnDefinition = "int")
	private int record_id;

	@Size(max = 50)
	@Column(name = "record_identifier", nullable = false, columnDefinition = "varchar(50)")
	private String record_identifier;

	protected RecordsEntity() {}

	public RecordsEntity(int record_id, String record_identifier) {
		this.record_id = record_id;
		this.record_identifier = record_identifier;
	}

	public RecordsEntity(String record_identifier) {
		this.record_identifier = record_identifier;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public int getRecord_id() {
		return this.record_id;
	}

	public void setRecord_identifier(String record_identifier) {
		this.record_identifier = record_identifier;
	}

	public String getRecord_identifier() {
		return this.record_identifier;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof RecordsEntity)) return false;
		RecordsEntity that = (RecordsEntity) o;
		return Objects.equals(record_id, that.record_id) && Objects.equals(record_identifier, that.record_identifier);
	}

	@Override
	public int hashCode() {
		return Objects.hash(record_id, record_identifier);
	}

	@Override
	public String toString() {
		return "RecordsEntity{record_id=" + record_id + ", record_identifier='" + record_identifier + "'}";
	}
}
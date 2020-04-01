package com.atopion.UGC_repository.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
 
import javax.persistence.*;
import javax.validation.constraints.Size;

import java.util.Objects;
import java.util.Date;
import java.text.SimpleDateFormat;
    
@Entity
@Table(name = "content_lists_records")
public class ContentListsRecordsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "list_content_id", nullable = false, unique = true, columnDefinition = "int")
	private int list_content_id;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="Europe/Berlin")
	@Column(name = "entry_created", nullable = false, columnDefinition = "datetime")
	private Date entry_created;

	@Column(name = "list_id", nullable = false, columnDefinition = "int")
	private int list_id;

	@Column(name = "record_id", nullable = false, columnDefinition = "int")
	private int record_id;

	protected ContentListsRecordsEntity() {}

	public ContentListsRecordsEntity(int list_content_id, Date entry_created, int list_id, int record_id) {
		this.list_content_id = list_content_id;
		this.entry_created = entry_created;
		this.list_id = list_id;
		this.record_id = record_id;
	}

	public ContentListsRecordsEntity(Date entry_created, int list_id, int record_id) {
		this.entry_created = entry_created;
		this.list_id = list_id;
		this.record_id = record_id;
	}

	public void setList_content_id(int list_content_id) {
		this.list_content_id = list_content_id;
	}

	public int getList_content_id() {
		return this.list_content_id;
	}

	public void setEntry_created(Date entry_created) {
		this.entry_created = entry_created;
	}

	public Date getEntry_created() {
		return this.entry_created;
	}

	public void setList_id(int list_id) {
		this.list_id = list_id;
	}

	public int getList_id() {
		return this.list_id;
	}

	public void setRecord_id(int record_id) {
		this.record_id = record_id;
	}

	public int getRecord_id() {
		return this.record_id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ContentListsRecordsEntity)) return false;
		ContentListsRecordsEntity that = (ContentListsRecordsEntity) o;
		return Objects.equals(list_content_id, that.list_content_id) && Objects.equals(entry_created, that.entry_created) && Objects.equals(list_id, that.list_id) && Objects.equals(record_id, that.record_id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(list_content_id, entry_created, list_id, record_id);
	}

	@Override
	public String toString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return "ContentListsRecordsEntity{list_content_id=" + list_content_id + ", entry_created=" + format.format(entry_created) + ", list_id=" + list_id + ", record_id=" + record_id + "}";
	}
}
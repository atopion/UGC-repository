package com.atopion.UGC_repository.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Objects;

@Entity
@Table(name="applications")
@XmlRootElement(name = "application")
public class ApplicationsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id", nullable = false, unique = true)
    @XmlElement
    private int application_id;

    @Column(name = "application_name", nullable = false, unique = true)
    @XmlElement
    private String application_name;

    protected ApplicationsEntity() {}

    public ApplicationsEntity(int application_id, String application_name) {
        this.application_id = application_id;
        this.application_name = application_name;
    }

    public int getApplication_id() {
        return application_id;
    }

    public void setApplication_id(int application_id) {
        this.application_id = application_id;
    }

    public String getApplication_name() {
        return application_name;
    }

    public void setApplication_name(String application_name) {
        this.application_name = application_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ApplicationsEntity)) return false;
        ApplicationsEntity that = (ApplicationsEntity) o;
        return application_id == that.application_id &&
                Objects.equals(application_name, that.application_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(application_id, application_name);
    }

    @Override
    public String toString() {
        return "ApplicationsEntity{" +
                "application_id=" + application_id +
                ", application_name='" + application_name + '\'' +
                '}';
    }
}

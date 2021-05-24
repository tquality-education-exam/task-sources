package com.a1qa.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by p.ordenko on 11.05.2015, 17:33.
 */
@Entity
@Table(name = "project")
@NamedQueries({
        @NamedQuery(name = Project.NAME_FIND_BY_NAME, query = "SELECT p FROM Project p WHERE p.name = :projectName"),
        @NamedQuery(name = Project.NAME_FIND_ALL, query = "SELECT p FROM Project p")
})
public class Project extends ABaseEntity {
    public static final String NAME_FIND_BY_NAME = "Project.findByName";
    public static final String NAME_FIND_ALL = "Project.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

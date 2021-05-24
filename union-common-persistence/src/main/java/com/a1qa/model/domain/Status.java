package com.a1qa.model.domain;

import com.a1qa.model.constants.StatusEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Created by p.ordenko on 14.05.2015, 14:14.
 */
@Entity
@Table(name = "status")
@NamedQueries({
        @NamedQuery(name = Status.NAME_FIND_BY_NAME, query = "SELECT s FROM Status s WHERE UPPER(s.name) = :statusName"),
        @NamedQuery(name = Status.NAME_AND_ID, query = "SELECT s FROM Status s")
})
public class Status extends ABaseEntity {
    public static final String NAME_FIND_BY_NAME = "Status.findByName";
    public static final String NAME_AND_ID = "Status.nameAndId";

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

    public StatusEnum getStatusEnum() {
        return StatusEnum.valueOf(name);
    }
}

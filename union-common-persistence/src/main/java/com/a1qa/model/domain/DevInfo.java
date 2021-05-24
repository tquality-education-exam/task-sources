package com.a1qa.model.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by p.ordenko on 14.05.2015, 14:14.
 */
@Entity
@Table(name = "dev_info")
@NamedQueries({
        @NamedQuery(name = DevInfo.NAME_FIND_BY_TEST_ID, query = "SELECT di FROM DevInfo di WHERE di.test.id = :testId")
})
public class DevInfo extends ABaseEntity {
    public static final String NAME_FIND_BY_TEST_ID = "DevInfo.findByTestId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "DEV_TIME")
    private float devTime;

    @JoinColumn(name = "TEST_ID", referencedColumnName = "ID", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Test test;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getDevTime() {
        return devTime;
    }

    public void setDevTime(float devTime) {
        this.devTime = devTime;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}

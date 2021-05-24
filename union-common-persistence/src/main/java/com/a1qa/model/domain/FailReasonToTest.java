package com.a1qa.model.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Created by p.ordenko on 18.09.2015, 14:23.
 */
@Entity
@Table(name = "rel_fail_reason_test")
@NamedQueries({
        @NamedQuery(name = FailReasonToTest.NAME_RELATIONS_BY_TEST_ID, query = "SELECT frt FROM FailReasonToTest frt WHERE frt.test.id = :testId")
})
public class FailReasonToTest extends ABaseEntity {

    public static final String NAME_RELATIONS_BY_TEST_ID = "FailReasonToTest.relationsByTestId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @JoinColumn(name = "FAIL_REASON_ID")
    @ManyToOne(optional = false)
    private FailReason failReason;

    @JoinColumn(name = "TEST_ID")
    @OneToOne(optional = false)
    private Test test;

    @Column(name = "COMMENT")
    private String comment;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FailReason getFailReason() {
        return failReason;
    }

    public void setFailReason(FailReason failReason) {
        this.failReason = failReason;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

package com.a1qa.model.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.Formula;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Created by p.ordenko on 11.05.2015, 17:33.
 */
@Entity
@Table(name = "session")
@NamedQueries({
        @NamedQuery(name = Session.NAME_FIND_BY_KEY,
                query = "SELECT s FROM Session s WHERE session_key = :key"),
        @NamedQuery(name = Session.NAME_FIND_TESTS_COUNT_BY_STATUS,
                query = "SELECT count(t.id) FROM Test t WHERE t.session.id = :sid AND t.status.id = :statusId"),
        @NamedQuery(name = Session.NAME_FIND_IN_PROGRESS_TESTS_COUNT,
                query = "SELECT count(t.id) FROM Test t WHERE t.session.id = :sid AND t.status.id IS NULL"),
        @NamedQuery(name = Session.NAME_SESSIONS_BY_PROJECT_ID, query = "SELECT DISTINCT t.session FROM Test t WHERE t.project.id = :projectId ORDER BY t.session.createdTime DESC"),
        @NamedQuery(name = Session.NAME_COUNT_SESSIONS_BY_PROJECT_ID, query = "SELECT count(DISTINCT t.session) FROM Test t WHERE t.project.id = :projectId")
})
public class Session extends ABaseEntity {
    public static final String NAME_FIND_BY_KEY = "Session.findByKey";
    public static final String NAME_FIND_TESTS_COUNT_BY_STATUS = "Session.findTestsCountByStatus";
    public static final String NAME_FIND_IN_PROGRESS_TESTS_COUNT= "Session.findInProgressTestsCount";
    public static final String NAME_SESSIONS_BY_PROJECT_ID = "Session.findSessionsByProjectId";
    public static final String NAME_COUNT_SESSIONS_BY_PROJECT_ID = "Session.countSessionsByProjectId";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private long id;

    @Column(name = "SESSION_KEY")
    private String sessionKey;

    @Column(name = "CREATED_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "BUILD_NUMBER")
    private long buildNumber;

    @Formula("(SELECT MAX(t.end_time) FROM test t WHERE t.session_id = id)")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Test> tests;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public long getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(long buildNumber) {
        this.buildNumber = buildNumber;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public FailReason getSessionFailReason() {
        FailReason failReason = new FailReason();
        failReason.setId(-1L);
        failReason.setIsGlobal(true);
        failReason.setIsSession(true);
        failReason.setIsTest(false);
        failReason.setName("None");
        failReason.setIsUnchangeable(false);
        if (tests == null) {
            return failReason;
        }
        FailReasonToTest tmpFailReason = tests.get(0).getFailReasonToTest();
        boolean nonSessionWide = false;
        for (Test test : tests) {
            if (test.getFailReasonToTest() == null || test.getFailReasonToTest().getFailReason().getId() != tmpFailReason.getFailReason().getId()) {
                nonSessionWide = true;
            } else if (test.getFailReasonToTest().getFailReason().isUnchangeable()) {
                failReason = test.getFailReasonToTest().getFailReason();
                break;
            } else if (!nonSessionWide) {
                failReason = test.getFailReasonToTest().getFailReason();
            }
        }
        return failReason;
    }
}

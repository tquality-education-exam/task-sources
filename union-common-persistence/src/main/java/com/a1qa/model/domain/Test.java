package com.a1qa.model.domain;

import com.a1qa.common.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by p.ordenko on 11.05.2015, 17:04.
 */
@Entity
@Table(name = "test")
@NamedQueries({
        @NamedQuery(name = Test.NAME_TESTS_BY_SID, query = "SELECT t FROM Test t WHERE t.session.id = :sid ORDER BY t.startTime ASC"),
        @NamedQuery(name = Test.NAME_COUNT_TESTS_BY_SID, query = "SELECT count(t) FROM Test t WHERE t.session.id = :sid"),
        @NamedQuery(name = Test.NAME_TEST_HISTORY, query = "SELECT t FROM Test t WHERE t.name = :name AND t.methodName = :methodName AND t.project = :project ORDER BY t.startTime DESC"),
        @NamedQuery(name = Test.NAME_UNIQUE_PROJECT_TESTS, query =
                " SELECT t FROM Test t WHERE CONCAT(t.methodName, t.startTime) IN ( " +
                    " SELECT CONCAT(t1.methodName, MAX(t1.startTime)) FROM Test t1 " +
                    " WHERE t1.project.id = :projectId " +
                    " GROUP BY t1.methodName " +
                " ) " +
                " ORDER BY t.startTime DESC "),
        @NamedQuery(name = Test.NAME_COUNT_UNIQUE_PROJECT_TESTS, query =
                " SELECT count(t) FROM Test t WHERE CONCAT(t.methodName, t.startTime) IN ( " +
                    " SELECT CONCAT(t1.methodName, MAX(t1.startTime)) FROM Test t1 " +
                    " WHERE t1.project.id = :projectId " +
                    " GROUP BY t1.methodName " +
                " ) "),
        @NamedQuery(name = Test.NAME_COUNT_UNIQUE_PROJECT_TESTS_WITH_STATUS, query =
                " SELECT count(t) FROM Test t WHERE CONCAT(t.methodName, t.startTime) IN ( " +
                    " SELECT CONCAT(t1.methodName, MAX(t1.startTime)) FROM Test t1 " +
                    " WHERE t1.project.id = :projectId " +
                    " GROUP BY t1.methodName " +
                " ) AND t.status.name = :statusName "),
        @NamedQuery(name = Test.NAME_TESTS_BY_PROJECT_AND_END_TIME, query = "SELECT t FROM Test t WHERE t.project.id = :projectId AND t.endTime >= :endTime ORDER BY t.endTime, t.methodName"),
        @NamedQuery(name = Test.NAME_TESTS_BY_PROJECT_RANDOM_ORDER, query =
                " SELECT t FROM Test t WHERE CONCAT(t.methodName, t.startTime) IN ( " +
                        " SELECT CONCAT(t1.methodName, MAX(t1.startTime)) FROM Test t1 " +
                        " WHERE t1.project.id = :projectId " +
                        " GROUP BY t1.methodName " +
                        " ) " +
                        " ORDER BY RAND()"),

})
public class Test extends ABaseEntity {

    public static final String NAME_TESTS_BY_SID = "Test.findTestsBySid";
    public static final String NAME_COUNT_TESTS_BY_SID = "Test.countTestsBySid";
    public static final String NAME_TEST_HISTORY = "Test.findTestHistory";
    public static final String NAME_UNIQUE_PROJECT_TESTS = "Test.findUniqueProjectTests";
    public static final String NAME_COUNT_UNIQUE_PROJECT_TESTS = "Test.countUniqueProjectTests";
    public static final String NAME_COUNT_UNIQUE_PROJECT_TESTS_WITH_STATUS = "Test.countUniqueProjectTestsWithStatus";
    public static final String NAME_TESTS_BY_PROJECT_AND_END_TIME = "Test.testsByProjectAndEndTime";
    public static final String NAME_TESTS_BY_PROJECT_RANDOM_ORDER = "Test.testsByProjectRandomOrder";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "method_name")
    private String methodName;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Column(name = "env")
    private String envName;

    @Column(name = "browser")
    private String browser;

    @JoinColumn(name = "project_id")
    @ManyToOne
    private Project project;

    @JoinColumn(name = "session_id")
    @ManyToOne
    @JsonIgnore
    private Session session;

    @JoinColumn(name = "status_id")
    @ManyToOne
    private Status status;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Log> logs = new ArrayList<>();

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Attachment> attachments = new ArrayList<>();

    @JoinColumn(name = "author_id")
    @ManyToOne
    private Author author;

    @Transient
    @OneToOne(mappedBy = "test", cascade = CascadeType.ALL)
    @JsonManagedReference
    private DevInfo devInfo = new DevInfo();

    @OneToOne(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private FailReasonToTest failReasonToTest;

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEnvName() {
        return envName;
    }

    public void setEnvName(String envName) {
        this.envName = envName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public DevInfo getDevInfo() {
        return devInfo;
    }

    public void setDevInfo(DevInfo devInfo) {
        this.devInfo = devInfo;
    }

    public FailReasonToTest getFailReasonToTest() {
        return failReasonToTest;
    }

    public void setFailReasonToTest(FailReasonToTest failReasonToTest) {
        this.failReasonToTest = failReasonToTest;
    }

    ////////////////
    // Logic methods
    ////////////////

    public String getDuration() {
        return CommonUtils.getDurationAsString(endTime, startTime);
    }

    public boolean allLogsAreEmpty() {
        for (Log log : logs) {
            if (!log.getContent().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
}

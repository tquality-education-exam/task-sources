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
 * Created by p.ordenko on 18.09.2015, 14:23.
 */
@Entity
@Table(name = "fail_reason")
@NamedQueries({
        @NamedQuery(name = FailReason.NAME_FIND_ALL, query = "SELECT fr FROM FailReason fr")
})
public class FailReason extends ABaseEntity {

    public static final String NAME_FIND_ALL = "FailReason.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "IS_GLOBAL")
    private boolean isGlobal;

    @Column(name = "IS_UNREMOVABLE")
    private boolean isUnremovable;

    @Column(name = "IS_UNCHANGEABLE")
    private boolean isUnchangeable;

    @Column(name = "IS_STATS_IGNORED")
    private boolean isStatsIgnored;

    @Column(name = "IS_SESSION")
    private boolean isSession;

    @Column(name = "IS_TEST")
    private boolean isTest;

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

    public boolean isGlobal() {
        return isGlobal;
    }

    public void setIsGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
    }

    public boolean isUnremovable() {
        return isUnremovable;
    }

    public void setIsUnremovable(boolean isUnremovable) {
        this.isUnremovable = isUnremovable;
    }

    public boolean isSession() {
        return isSession;
    }

    public void setIsSession(boolean isSession) {
        this.isSession = isSession;
    }

    public boolean isTest() {
        return isTest;
    }

    public void setIsTest(boolean isTest) {
        this.isTest = isTest;
    }

    public boolean isUnchangeable() {
        return isUnchangeable;
    }

    public void setIsUnchangeable(boolean isUnchangeable) {
        this.isUnchangeable = isUnchangeable;
    }

    public boolean isStatsIgnored() {
        return isStatsIgnored;
    }

    public void setIsStatusIgnored(boolean isStatusIgnored) {
        this.isStatsIgnored = isStatusIgnored;
    }
}

package com.a1qa.common;

/**
 * Created by p.ordenko on 14.05.2015, 12:13.
 */
public enum NamedParam {
    STATUS_NAME("statusName"),
    PROJECT_NAME("projectName"),
    LOGIN("login"),
    PROJECT_ID("projectId"),
    PROJECT("project"),
    NAME("name"),
    METHOD_NAME("methodName"),
    TEST_ID("testId"),
    SID("sid"),
    STATUS_ID("statusId"),
    KEY("key"),
    END_TIME("endTime"),
    ID("id"),
    TOKEN_VALUE("tokenValue");

    private String paramName;

    NamedParam(String paramName) {
        this.paramName = paramName;
    }

    public String getParamName() {
        return paramName;
    }
}

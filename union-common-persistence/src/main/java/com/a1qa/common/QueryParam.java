package com.a1qa.common;

/**
 * Created by p.ordenko on 29.05.2015, 14:50.
 */
public class QueryParam {

    private final String name;
    private final Object value;

    public QueryParam(NamedParam name, Object value) {
        this.name = name.getParamName();
        this.value = value;
    }

    public QueryParam(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}

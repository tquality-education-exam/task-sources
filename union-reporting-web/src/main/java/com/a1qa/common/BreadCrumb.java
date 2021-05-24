package com.a1qa.common;

/**
 * Created by p.ordenko on 12.06.2015, 18:31.
 */
public class BreadCrumb {

    private final String name;
    private final String uri;

    public BreadCrumb(String name, String uri) {
        this.name = (name.length() > 30) ? name.substring(0, 30) + "..." : name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return "BreadCrumb{" +
                "name='" + name + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}

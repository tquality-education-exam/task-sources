package com.a1qa.model.constants;

/**
 * Created by p.ordenko on 29.05.2015, 16:25.
 */
public enum StatusEnum {
    PASSED(1L),
    FAILED(2L),
    SKIPPED(3L),
    UNFINISHED(4L), // NOT IN DATABASE
    TOTAL(5L); // NOT IN DATABASE

    private long id;

    StatusEnum(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

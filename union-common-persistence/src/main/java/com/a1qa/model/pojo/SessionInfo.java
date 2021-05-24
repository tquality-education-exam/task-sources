package com.a1qa.model.pojo;

import com.a1qa.common.utils.CommonUtils;
import com.a1qa.model.domain.Session;

/**
 * Created by p.ordenko on 29.05.2015, 15:18.
 */
public class SessionInfo {
    private final Session session;
    private final long passedCount;
    private final long failedCount;
    private final long skippedCount;
    private final long inProgressCount;

    public SessionInfo(Session session, long passedCount, long failedCount, long skippedCount, long inProgressCount) {
        this.session = session;
        this.passedCount = passedCount;
        this.failedCount = failedCount;
        this.skippedCount = skippedCount;
        this.inProgressCount = inProgressCount;
    }

    public Session getSession() {
        return session;
    }

    public long getPassedCount() {
        return passedCount;
    }

    public long getFailedCount() {
        return failedCount;
    }

    public long getSkippedCount() {
        return skippedCount;
    }

    public long getInProgressCount() {
        return inProgressCount;
    }

    public long getTotalCount() {
        return passedCount + failedCount + skippedCount + inProgressCount;
    }

    public String getDuration() {
        return CommonUtils.getDurationAsString(session.getEndTime(), session.getCreatedTime());
    }
}

package com.a1qa.controller;

import com.a1qa.common.NamedParam;
import com.a1qa.common.QueryParam;
import com.a1qa.common.utils.HibernateUtil;
import com.a1qa.model.constants.StatusEnum;
import com.a1qa.model.domain.Session;
import com.a1qa.model.pojo.SessionInfo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by p.ordenko on 27.05.2015, 18:10.
 */
@RestController
public class SessionController extends ABaseController {

    /**
     * Get sessions associated with specified project
     * @param projectId (required) Project Id
     * @return List of all sessions for project {@link SessionInfo}
     */
    @RequestMapping(value = "/session/get/by/projectId/{projectId}", method = RequestMethod.GET)
    public String getSessionsByProjectId(@PathVariable("projectId") long projectId) {
        List<Session> sessions = HibernateUtil.getEntities(Session.NAME_SESSIONS_BY_PROJECT_ID,
                new QueryParam(NamedParam.PROJECT_ID, projectId));
        if (sessions.isEmpty()) {
            throw new NoSuchElementException("No sessions for project with ID " + projectId);
        }
        List<SessionInfo> result = new LinkedList<>();
        long passedTestsCount, failedTestsCount, skippedTestsCount, inProgressTestsCount;
        for (Session session : sessions) {
            passedTestsCount = (Long) HibernateUtil.getObjects(Session.NAME_FIND_TESTS_COUNT_BY_STATUS,
                    new QueryParam(NamedParam.SID, session.getId()),
                    new QueryParam(NamedParam.STATUS_ID, StatusEnum.PASSED.getId())).get(0);
            failedTestsCount = (Long) HibernateUtil.getObjects(Session.NAME_FIND_TESTS_COUNT_BY_STATUS,
                    new QueryParam(NamedParam.SID, session.getId()),
                    new QueryParam(NamedParam.STATUS_ID, StatusEnum.FAILED.getId())).get(0);
            skippedTestsCount = (Long) HibernateUtil.getObjects(Session.NAME_FIND_TESTS_COUNT_BY_STATUS,
                    new QueryParam(NamedParam.SID, session.getId()),
                    new QueryParam(NamedParam.STATUS_ID, StatusEnum.SKIPPED.getId())).get(0);
            inProgressTestsCount = (Long) HibernateUtil.getObjects(Session.NAME_FIND_TESTS_COUNT_BY_STATUS,
                    new QueryParam(NamedParam.SID, session.getId()),
                    new QueryParam(NamedParam.STATUS_ID, null)).get(0);
            result.add(new SessionInfo(session, passedTestsCount, failedTestsCount, skippedTestsCount, inProgressTestsCount));
        }
        return getString(result);
    }

}

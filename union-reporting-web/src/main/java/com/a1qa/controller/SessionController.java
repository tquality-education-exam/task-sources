package com.a1qa.controller;

import com.a1qa.common.NamedParam;
import com.a1qa.common.Page;
import com.a1qa.common.QueryParam;
import com.a1qa.common.utils.CommonUtils;
import com.a1qa.common.utils.HibernateUtil;
import com.a1qa.model.constants.Config;
import com.a1qa.model.constants.StatusEnum;
import com.a1qa.model.domain.*;
import com.a1qa.model.pojo.GraphPoint;
import com.a1qa.model.pojo.SessionInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by p.ordenko on 01.06.2015, 12:38.
 */
@Controller
public class SessionController extends ABaseController  implements ServletContextAware {

    private ServletContext servletContext;
    private static final String DATE_PATTERN = "yyyy-MM-dd hh:mm:ss";

    @RequestMapping(value = "/sessions", method = RequestMethod.GET)
    public ModelAndView getSessionsByProject(long projectId, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        ModelAndView result = new ModelAndView("sessions");
        QueryParam projectIdParam = new QueryParam(NamedParam.PROJECT_ID, projectId);
        List<Session> sessions = HibernateUtil.getEntities(Session.NAME_SESSIONS_BY_PROJECT_ID, page, projectIdParam);
        sessions.get(0).getSessionFailReason();
        if (sessions.isEmpty()) {
            throw new NoSuchElementException("No sessions for project with ID " + projectId);
        }
        int sessionsCount = ((Long) HibernateUtil.getObjects(Session.NAME_COUNT_SESSIONS_BY_PROJECT_ID, projectIdParam).get(0)).intValue();
        int projectTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS, projectIdParam).get(0)).intValue();
        List<SessionInfo> sessionInfos = new LinkedList<>();
        long passedTestsCount, failedTestsCount, skippedTestsCount, inProgressTestsCount;
        List<FailReason> failReasons = getFailReasonsForProject(projectId);
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
            inProgressTestsCount = (Long) HibernateUtil.getObjects(Session.NAME_FIND_IN_PROGRESS_TESTS_COUNT,
                    new QueryParam(NamedParam.SID, session.getId())).get(0);
            sessionInfos.add(new SessionInfo(session, passedTestsCount, failedTestsCount, skippedTestsCount, inProgressTestsCount));
        }
        List<Test> test = HibernateUtil.getEntities(Test.NAME_TESTS_BY_SID, new QueryParam(NamedParam.SID, sessionInfos.get(0).getSession().getId()));
        buildBreadCrumbs(Page.SESSIONS, test, result);
        buildPaging(sessionsCount, page, result);
        result.addObject("sessions", sessionInfos);
        result.addObject("projectId", projectId);
        result.addObject("projectTestsCount", projectTestsCount);
        result.addObject("failReasons", failReasons);
        return result;
    }

    @RequestMapping(value = "/allTests", method = RequestMethod.GET)
    public ModelAndView getAllTestsByProject(long projectId, @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                                             @CookieValue(value = "token", required = false) String token) {
        ModelAndView result = new ModelAndView("allTests");
        Variant variant = null;
        try {
            variant = ((Token) HibernateUtil.getEntities(Token.TOKEN_BY_VALUE,
                    new QueryParam(NamedParam.TOKEN_VALUE, token)).get(0)).getVariant();
        }
        catch (Exception ignore) {}
        
        if (variant == null || !variant.isUseAjaxForTestsPage()) {
            QueryParam projectIdParam = new QueryParam(NamedParam.PROJECT_ID, projectId);
            List<Test> tests = HibernateUtil.getEntities(Test.NAME_UNIQUE_PROJECT_TESTS, page, projectIdParam);
            List<Test> testsForChronologyGraphBuilding = HibernateUtil.getEntities(Test.NAME_TESTS_BY_PROJECT_AND_END_TIME,
                    projectIdParam, new QueryParam(NamedParam.END_TIME, CommonUtils.getDateFromNow(-(Config.GRAPH_ONE_PERIOD * Config.GRAPH_PERIODS_COUNT))));
            int projectTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS, projectIdParam).get(0)).intValue();
            int passedTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS_WITH_STATUS,
                    projectIdParam, new QueryParam(NamedParam.STATUS_NAME, StatusEnum.PASSED.toString())).get(0)).intValue();
            int failedTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS_WITH_STATUS,
                    projectIdParam, new QueryParam(NamedParam.STATUS_NAME, StatusEnum.FAILED.toString())).get(0)).intValue();
            int skippedTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS_WITH_STATUS,
                    projectIdParam, new QueryParam(NamedParam.STATUS_NAME, StatusEnum.SKIPPED.toString())).get(0)).intValue();
            int unfinishedTestsCount = projectTestsCount - passedTestsCount - failedTestsCount - skippedTestsCount;
            buildBreadCrumbs(Page.ALL_TESTS, tests, result);
            buildPaging(projectTestsCount, page, result);
            result.addObject("tests", tests);
            result.addObject("projectTestsCount", projectTestsCount);
            result.addObject("passedTestsCount", passedTestsCount);
            result.addObject("failedTestsCount", failedTestsCount);
            result.addObject("skippedTestsCount", skippedTestsCount);
            result.addObject("unfinishedTestsCount", unfinishedTestsCount);
            result.addObject("chronologyGraph", getTestsStat(testsForChronologyGraphBuilding));
        }
        List<Status> statuses = HibernateUtil.getEntities(Status.NAME_AND_ID);
        result.addObject("statuses", statuses);
        result.addObject("projectId", projectId);
        result.addObject("pCurrentPage", page);
        return result;
    }

    @RequestMapping(value = "/allTests", method = RequestMethod.POST)
    public void addTest(
            @RequestParam("testName") String testName,
            @RequestParam("status") String status,
            @RequestParam("testMethod") String testMethod,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime,
            @RequestParam("environment") String environment,
            @RequestParam("browser") String browser,
            @RequestParam("log") String log,
            @RequestParam("projectId") long projectId,
            @RequestPart(value = "attachment", required = false) MultipartFile file) throws ParseException {


        Test test = new Test();
        test.setName(testName);
        test.setStatus((Status) HibernateUtil.getEntities(Status.NAME_FIND_BY_NAME,  new QueryParam(NamedParam.STATUS_NAME, status)).get(0));
        test.setMethodName(testMethod);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_PATTERN);
        test.setStartTime(simpleDateFormat.parse(startTime));
        test.setEndTime(simpleDateFormat.parse(endTime));

        test.setEnvName(environment);
        test.setBrowser(browser);
        test.setProject(HibernateUtil.getEntityById(Project.class, projectId));

        Session session = new Session();
        session.setSessionKey(String.valueOf(System.currentTimeMillis()));
        session.setBuildNumber(((Long) HibernateUtil.getObjects(Session.NAME_COUNT_SESSIONS_BY_PROJECT_ID, new QueryParam(NamedParam.PROJECT_ID, projectId)).get(0)).intValue() + 1);
        HibernateUtil.save(session);
        test.setSession(session);

        HibernateUtil.save(test);

        if (log != null) {
            Log testLog = new Log();
            testLog.setContent(log);
            testLog.setIsException(test.getStatus().getStatusEnum() != StatusEnum.PASSED);
            testLog.setTest(test);
            HibernateUtil.save(testLog);
        }

        if (file != null &&!file.isEmpty()) {
            String filename = file.getOriginalFilename();
            Attachment attachment = new Attachment();

            try {
                byte[] bytes = file.getBytes();
                attachment.setContent(bytes);
                attachment.setTest(test);
                if (filename.endsWith(".png")) {
                    attachment.setContentType("image/png");
                } else {
                    attachment.setContentType("text/html");
                }
                HibernateUtil.save(attachment);
            }
            catch (Exception ignore){}
        }

    }


    @RequestMapping(value = "/allTests/ajax", method = RequestMethod.GET)
    public ModelAndView getAllTestsAjax(long projectId, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        ModelAndView result = new ModelAndView("ajaxAllTest");
            QueryParam projectIdParam = new QueryParam(NamedParam.PROJECT_ID, projectId);
            List<Test> tests = HibernateUtil.getEntities(Test.NAME_UNIQUE_PROJECT_TESTS, page, projectIdParam);
            List<Test> testsForChronologyGraphBuilding = HibernateUtil.getEntities(Test.NAME_TESTS_BY_PROJECT_AND_END_TIME,
                    projectIdParam, new QueryParam(NamedParam.END_TIME, CommonUtils.getDateFromNow(-(Config.GRAPH_ONE_PERIOD * Config.GRAPH_PERIODS_COUNT))));
            int projectTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS, projectIdParam).get(0)).intValue();
            int passedTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS_WITH_STATUS,
                    projectIdParam, new QueryParam(NamedParam.STATUS_NAME, StatusEnum.PASSED.toString())).get(0)).intValue();
            int failedTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS_WITH_STATUS,
                    projectIdParam, new QueryParam(NamedParam.STATUS_NAME, StatusEnum.FAILED.toString())).get(0)).intValue();
            int skippedTestsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_UNIQUE_PROJECT_TESTS_WITH_STATUS,
                    projectIdParam, new QueryParam(NamedParam.STATUS_NAME, StatusEnum.SKIPPED.toString())).get(0)).intValue();
            int unfinishedTestsCount = projectTestsCount - passedTestsCount - failedTestsCount - skippedTestsCount;
            buildBreadCrumbs(Page.ALL_TESTS, tests, result);
            buildPaging(projectTestsCount, page, result);
            result.addObject("tests", tests);
            result.addObject("projectId", projectId);
            result.addObject("projectTestsCount", projectTestsCount);
            result.addObject("passedTestsCount", passedTestsCount);
            result.addObject("failedTestsCount", failedTestsCount);
            result.addObject("skippedTestsCount", skippedTestsCount);
            result.addObject("unfinishedTestsCount", unfinishedTestsCount);
            result.addObject("chronologyGraph", getTestsStat(testsForChronologyGraphBuilding));
            List<Status> statuses = HibernateUtil.getEntities(Status.NAME_AND_ID);
            result.addObject("statuses", statuses);
        return result;
    }

    @RequestMapping(value = "/sessions/setFailReason", method = RequestMethod.POST)
    public @ResponseBody boolean setFailReasonForSessionTests(long sessionId, long failReasonId,
                                             @RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
        try {
            List<Test> tests = HibernateUtil.getEntities(Test.NAME_TESTS_BY_SID, new QueryParam(NamedParam.SID, sessionId));
            if (failReasonId >= 0) {
                FailReason failReason = HibernateUtil.getEntityById(FailReason.class, failReasonId);
                if (failReason.isSession()) {
                    FailReasonToTest failReasonToTest = new FailReasonToTest();
                    failReasonToTest.setFailReason(failReason);
                    for (Test test : tests) {
                        failReasonToTest.setTest(test);
                        failReasonToTest.setComment(comment);
                        test.setFailReasonToTest(failReasonToTest);
                        HibernateUtil.update(test);
                    }
                } else {
                    return false;
                }
            } else {
                for (Test test : tests) {
                    test.setFailReasonToTest(null);
                    HibernateUtil.update(test);
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //////////////////
    // Private methods
    //////////////////

    /**
     * Count tests stats
     * @param tests List of tests ordered by end time and then - by method name
     * @return Collection of tests statistic with passed / failed / skipped / unfinished / total tests
     */
    // TODO Refactor it
    private List<Map<StatusEnum, GraphPoint>> getTestsStat(List<Test> tests) {
        List<Map<StatusEnum, GraphPoint>> result = new ArrayList<>();
        Map<String, StatusEnum> testsResultMap = new HashMap<>();
        Date tmpEndTime = null;
        Calendar periodTime = new GregorianCalendar();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        int testsSize = tests.size();
        int i = 0;
        for (Test test : tests) {
            i++;
            if (tmpEndTime == null) {
                tmpEndTime = test.getEndTime();
                periodTime.setTime(tmpEndTime);
                periodTime.add(Calendar.DAY_OF_YEAR, Config.GRAPH_ONE_PERIOD);
            }
            if (test.getFailReasonToTest() == null || !test.getFailReasonToTest().getFailReason().isStatsIgnored()) {
                if (test.getStatus() != null) {
                    testsResultMap.put(test.getMethodName(), test.getStatus().getStatusEnum());
                } else {
                    testsResultMap.put(test.getMethodName(), StatusEnum.UNFINISHED);
                }
            }
            if (testsSize == i || test.getEndTime().getTime() >= periodTime.getTimeInMillis()) {
                Map<StatusEnum, GraphPoint> onePeriodResult = new LinkedHashMap<>();
                Map<StatusEnum, Integer> stats = getStatsForResults(testsResultMap);
                String tmpXDate = dateFormat.format(periodTime.getTime());
                onePeriodResult.put(StatusEnum.TOTAL, new GraphPoint(tmpXDate, String.valueOf(stats.get(StatusEnum.TOTAL))));
                onePeriodResult.put(StatusEnum.PASSED, new GraphPoint(tmpXDate, String.valueOf(stats.get(StatusEnum.PASSED))));
                onePeriodResult.put(StatusEnum.FAILED, new GraphPoint(tmpXDate, String.valueOf(stats.get(StatusEnum.FAILED))));
                onePeriodResult.put(StatusEnum.SKIPPED, new GraphPoint(tmpXDate, String.valueOf(stats.get(StatusEnum.SKIPPED))));
                onePeriodResult.put(StatusEnum.UNFINISHED, new GraphPoint(tmpXDate, String.valueOf(stats.get(StatusEnum.UNFINISHED))));
                result.add(onePeriodResult);
                tmpEndTime = test.getEndTime();
                periodTime.add(Calendar.DAY_OF_YEAR, Config.GRAPH_ONE_PERIOD);
                testsResultMap.clear();
            }
        }
        return result;
    }

    /**
     * Count stats for collection of tests with it statuses
     * @param results Collections with test name / method name as key and it status - as value.
     * @return Collection with tests satuses count
     */
    private Map<StatusEnum, Integer> getStatsForResults(Map<String, StatusEnum> results) {
        Map<StatusEnum, Integer> result = new HashMap<>();
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        int unfinished = 0;
        for (String key : results.keySet()) {
            switch (results.get(key)) {
                case PASSED:
                    passed++;
                    break;
                case SKIPPED:
                    skipped++;
                    break;
                case FAILED:
                    failed++;
                    break;
                case UNFINISHED:
                    unfinished++;
                    break;
            }
        }
        result.put(StatusEnum.PASSED, passed);
        result.put(StatusEnum.SKIPPED, skipped);
        result.put(StatusEnum.FAILED, failed);
        result.put(StatusEnum.UNFINISHED, unfinished);
        result.put(StatusEnum.TOTAL, passed + skipped + failed + unfinished);
        return result;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
    }
}

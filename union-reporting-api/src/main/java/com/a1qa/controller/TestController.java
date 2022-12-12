package com.a1qa.controller;

import com.a1qa.common.NamedParam;
import com.a1qa.common.QueryParam;
import com.a1qa.common.utils.HibernateUtil;
import com.a1qa.configuration.Config;
import com.a1qa.model.domain.*;
import com.a1qa.utils.CSVGenerator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by p.ordenko on 13.05.2015, 20:24.
 */
@RestController
public class TestController extends ABaseController {

    /**
     * Store primary test data (first method which should be called before test execution starts)
     * @param sessionId (required) Any unique ID for current test execution iteration (E.g., current time-stamp)
     *                  IMPORTANT: This ID must be the same during all tests execution in current build (iteration)
     * @param projectName (required) Project name of tests
     * @param testName (required) Test name
     * @param methodName (required) Test method name
     * @param startTime (optional) Test execution start time in any Java available format
     *                  (if not specified, time of request receiving will be stored)
     * @param env (required) Test execution environment (computer hostname is recommended)
     * @param browser (optional) Test browser
     * @return Stored test Id
     */
    @RequestMapping(value = "/test/put", method = RequestMethod.POST)
    public @ResponseBody String putTest(@RequestParam(value = "SID") String sessionId,
                                        @RequestParam(value = "projectName") String projectName,
                                        @RequestParam(value = "testName") String testName,
                                        @RequestParam(value = "methodName") String methodName,
                                        @RequestParam(value = "startTime", required = false) Date startTime,
                                        @RequestParam(value = "env") String env,
                                        @RequestParam(value = "browser", required = false, defaultValue = "") String browser) {
        Test test = new Test();
        Project project = new Project();
        Session session = new Session();

        // Process project info
        List<Project> projects = HibernateUtil.getEntities(Project.NAME_FIND_BY_NAME,
                new QueryParam(NamedParam.PROJECT_NAME, projectName));
        if (projects.size() == 0) {
            project.setName(projectName);
            HibernateUtil.save(project);
        } else {
            project = projects.get(0);
        }
        test.setProject(project);

        // Process session info
        List<Session> sessions = HibernateUtil.getEntities(Session.NAME_FIND_BY_KEY,
                new QueryParam(NamedParam.KEY, sessionId));
        if (sessions.size() == 0) {
            session.setSessionKey(sessionId);
            session.setCreatedTime(new Date());
            List<Session> tmpSessions = HibernateUtil.getEntities(Session.NAME_SESSIONS_BY_PROJECT_ID, new QueryParam(NamedParam.PROJECT_ID, project.getId()));
            long buildNumber = 0L;
            for (Session tmpSession: tmpSessions) {
                if (tmpSession.getBuildNumber() > buildNumber) {
                    buildNumber = tmpSession.getBuildNumber();
                }
            }
            buildNumber++;
            session.setBuildNumber(buildNumber);
            HibernateUtil.save(session);
        } else {
            session = sessions.get(0);
        }

        test.setSession(session);
        test.setName(testName);
        test.setMethodName(methodName);
        if (startTime != null) {
            test.setStartTime(startTime);
        }
        test.setEnvName(env);
        test.setBrowser(browser);
        test.setStartTime((startTime == null) ? new Date() : startTime);
        HibernateUtil.save(test);
        return getString(test.getId());
    }

    /**
     * Delete test
     * @param testId (required) Test id
     */
    @RequestMapping(value = "/test/delete", method = RequestMethod.POST)
    public void deleteTest(@RequestParam(value = "testId") long testId){
        Test test = HibernateUtil.getEntityById(Test.class, testId);
        HibernateUtil.remove(test);
    }

    /**
     * Get full test info (with artifacts, development info and so on)
     * @param testId (required) Test id
     * @return Test info {@link Test}
     */
    @RequestMapping(value = "/test/get/id/{testId}", method = RequestMethod.GET)
    public @ResponseBody String getTest(@PathVariable("testId") long testId) {
        return getString(HibernateUtil.getEntityById(Test.class, testId));
    }

    /**
     * Store test execution logs
     * @param testId (required) Test id
     * @param content (required) Text logs as plain text
     * @param isException (optional) True - if posted logs - is exception stack-trace. False - otherwise (default false)
     */
    @RequestMapping(value = "/test/put/log", method = RequestMethod.POST)
    public void putLogs(@RequestParam(value = "testId") long testId,
                        @RequestParam(value = "content") String content,
                        @RequestParam(value = "isException", required = false, defaultValue = "false") boolean isException) {
        Test test = new Test();
        test.setId(testId);
        Log log = new Log();
        log.setContent(content);
        log.setIsException(isException);
        log.setTest(test);
        HibernateUtil.save(log);
    }

    /**
     * Store test attachments (page source, screenshots, any other files)
     * @param testId (required) Test id
     * @param content (required) An object. String with Base64-encoded content and content type (E.g., "text/html", "image/png", etc)
     */
    @RequestMapping(value = "/test/put/attachment", method = RequestMethod.POST)
    public void putAttachment(@RequestParam(value = "testId") long testId,
                              @RequestBody Content content) {
        Test test = new Test();
        test.setId(testId);
        Attachment attachment = new Attachment();
        attachment.setContent(Base64.getDecoder().decode(content.getContent()));
        attachment.setContentType(content.getContentType());
        attachment.setTest(test);
        HibernateUtil.save(attachment);
    }

    /**
     * Update test execution status
     * @param testId (required) Test id
     * @param endTime (optional) Test execution end time in any Java available format
     *                (if not specified, time of request receiving will be stored)
     * @param status (required) Test execution status (available values: "PASSED", "FAILED", "SKIPPED")
     */
    @RequestMapping(value = "/test/update", method = RequestMethod.POST)
    public void updateTest(@RequestParam(value = "testId") long testId,
                           @RequestParam(value = "endTime", required = false) Date endTime,
                           @RequestParam(value = "status") String status) {
        Test test = HibernateUtil.getEntityById(Test.class, testId);
        test.setEndTime((endTime == null) ? new Date() : endTime);
        List<Status> statuses = HibernateUtil.getEntities(Status.NAME_FIND_BY_NAME,
                new QueryParam(NamedParam.STATUS_NAME, status.toUpperCase()));
        if (statuses.isEmpty()) {
            throw new RuntimeException("Unknown status: " + status);
        }
        test.setStatus(statuses.get(0));
        HibernateUtil.update(test);
    }

    /**
     * Update test developer info
     * @param testId (required) Test id
     * @param authorName (required) Author name (E.g., "Pupken, Vasiliy")
     * @param authorLogin (required) Author login (E.g., "v.pupken")
     * @param authorEmail (required) Author e-mail (E.g., "v.pupken@mail.com")
     */
    @RequestMapping(value = "/test/update/author", method = RequestMethod.POST)
    public void updateTestAuthor(@RequestParam(value = "testId") long testId,
                                 @RequestParam(value = "name") String authorName,
                                 @RequestParam(value = "login") String authorLogin,
                                 @RequestParam(value = "email") String authorEmail) {
        Test test = HibernateUtil.getEntityById(Test.class, testId);
        List<Author> authors = HibernateUtil.getEntities(Author.NAME_FIND_BY_LOGIN,
                new QueryParam(NamedParam.LOGIN, authorLogin.toUpperCase()));
        Author author = new Author();
        if (authors.size() == 0) {
            author.setName(authorName);
            author.setLogin(authorLogin);
            author.setEmail(authorEmail);
            HibernateUtil.save(author);
        } else {
            author = authors.get(0);
        }
        test.setAuthor(author);
        HibernateUtil.update(test);
    }

    /**
     * Update development time info
     * @param testId (required) Test id
     * @param devTime (required) Test development time info (long. E.g.: 4, 4.0, 4.7, etc)
     */
    @RequestMapping(value = "/test/update/devInfo", method = RequestMethod.POST)
    public void updateTestDevInfo(@RequestParam(value = "testId") long testId,
                                  @RequestParam(value = "devTime") float devTime) {
        Test test = HibernateUtil.getEntityById(Test.class, testId);
        List<DevInfo> devInfos = HibernateUtil.getEntities(DevInfo.NAME_FIND_BY_TEST_ID,
                new QueryParam(NamedParam.TEST_ID, testId));
        DevInfo devInfo = new DevInfo();
        if (devInfos.size() == 0) {
            devInfo.setTest(test);
            devInfo.setDevTime(devTime);
            HibernateUtil.save(devInfo);
        }
    }

    /**
     * Get test list for specified session
     * @param sessionId (required) Session id
     * @return List of tests with all info {@link Test}
     */
    @RequestMapping(value = "/test/get/all/by/session/id/{sessionId}", method = RequestMethod.GET)
    public @ResponseBody String getTestsBySid(@PathVariable("sessionId") long sessionId) {
        return getString(HibernateUtil.getEntities(Test.NAME_TESTS_BY_SID, new QueryParam(NamedParam.SID, sessionId)));
    }


    @RequestMapping(value = "/test/get/json", method = RequestMethod.POST)
    public @ResponseBody String getAllTestsJson(@RequestParam(value = "projectId") long projectId) {
        int failProbability = Math.abs(new Random().nextInt()) % 100;
        if (failProbability < Config.FORMAT_FAIL_PROBABILITY_PERCENT) {
            return failProbability % 2 == 0 ? getCSVTests(projectId) :  getXMLTests(projectId);
        }
        else {
            return getJsonTests(projectId);
        }
    }


    @RequestMapping(value = "/test/get/csv", method = RequestMethod.POST)
    public @ResponseBody String getAllTestsCsv(@RequestParam(value = "projectId") long projectId) {
        int failProbability = Math.abs(new Random().nextInt()) % 100;
        if (failProbability < Config.FORMAT_FAIL_PROBABILITY_PERCENT) {
            return failProbability % 2 == 0 ? getJsonTests(projectId) :  getXMLTests(projectId);
        }
        else {
            return getCSVTests(projectId);
        }
    }

    @RequestMapping(value = "/test/get/xml", method = RequestMethod.POST)
    public @ResponseBody String getAllTestsXml(@RequestParam(value = "projectId") long projectId) {
        int failProbability = Math.abs(new Random().nextInt()) % 100;
        if (failProbability < Config.FORMAT_FAIL_PROBABILITY_PERCENT) {
            return failProbability % 2 == 0 ? getJsonTests(projectId) :  getCSVTests(projectId);
        }
        else {
            return getXMLTests(projectId);
        }
    }

    private String getJsonTests(long projectId) {
        List<Test> tests = HibernateUtil.getEntities(Test.NAME_TESTS_BY_PROJECT_RANDOM_ORDER, new QueryParam(NamedParam.PROJECT_ID, projectId));
        JSONArray result = new JSONArray();
        for (Test test : tests) {
            JSONObject jsonTest = new JSONObject();
            jsonTest.put("name", test.getName());
            jsonTest.put("method", test.getMethodName());
            jsonTest.put("status", test.getStatus() == null ? "In progress" : test.getStatus().getName());
            jsonTest.put("startTime", test.getStartTime());
            jsonTest.put("endTime", test.getEndTime());
            jsonTest.put("duration", test.getDuration());
            result.put(jsonTest);
        }
        return result.toString();
    }

    private String getXMLTests(long projectId) {
        return XML.toString(new JSONArray(getJsonTests(projectId)), "test");
    }

    private String getCSVTests(long projectId) {
        List<Test> tests = HibernateUtil.getEntities(Test.NAME_TESTS_BY_PROJECT_RANDOM_ORDER, new QueryParam(NamedParam.PROJECT_ID, projectId));
        CSVGenerator result = new CSVGenerator();
        for (Test test : tests) {
            result.addLine(test.getName(), test.getMethodName(), test.getStatus() == null ? "In progress" : test.getStatus().getName(),
                    test.getStartTime(), test.getEndTime(), test.getDuration());
        }
        return result.getData();
    }
}

package com.a1qa.controller;

import com.a1qa.common.NamedParam;
import com.a1qa.common.Page;
import com.a1qa.common.QueryParam;
import com.a1qa.common.utils.HibernateUtil;
import com.a1qa.model.domain.FailReason;
import com.a1qa.model.domain.FailReasonToTest;
import com.a1qa.model.domain.Test;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by p.ordenko on 01.06.2015, 12:38.
 */
@Controller
public class TestController extends ABaseController {

    @RequestMapping(value = "/tests", method = RequestMethod.GET)
    public ModelAndView getTestsBySid(long sid, @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        ModelAndView result = new ModelAndView("tests");
        QueryParam sidParam = new QueryParam(NamedParam.SID, sid);
        int testsCount = ((Long) HibernateUtil.getObjects(Test.NAME_COUNT_TESTS_BY_SID, sidParam).get(0)).intValue();
        List<Test> tests = HibernateUtil.getEntities(Test.NAME_TESTS_BY_SID, page, sidParam);
        buildBreadCrumbs(Page.TESTS, tests, result);
        buildPaging(testsCount, page, result);
        result.addObject("tests", tests);
        return result;
    }

    @RequestMapping(value = "/testInfo", method = RequestMethod.GET)
    public ModelAndView getTestInfo(long testId) {
        ModelAndView result = new ModelAndView("testInfo");
        Test testInfo = HibernateUtil.getEntityById(Test.class, testId);
        List<FailReason> failReasons = getFailReasonsForProject(testInfo.getProject().getId());
        buildBreadCrumbs(Page.TEST_INFO, testInfo, result);
        result.addObject("testInfo", testInfo);
        result.addObject("failReasons", failReasons);
        return result;
    }

    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ModelAndView getTestHistory(long testId) {
        ModelAndView result = new ModelAndView("history");
        Test test = HibernateUtil.getEntityById(Test.class, testId);
        List<Test> tests = HibernateUtil.getEntities(Test.NAME_TEST_HISTORY,
                new QueryParam(NamedParam.NAME, test.getName()),
                new QueryParam(NamedParam.METHOD_NAME, test.getMethodName()),
                new QueryParam(NamedParam.PROJECT, test.getProject()));
        buildBreadCrumbs(Page.TEST_HISTORY, tests, result);
        result.addObject("tests", tests);
        return result;
    }

    @RequestMapping(value = "/test/setFailReason", method = RequestMethod.POST)
    public @ResponseBody boolean setFailReasonForTest(long testId, long failReasonId,
                                         @RequestParam(value = "comment", required = false, defaultValue = "") String comment) {
        try {
            Test test = HibernateUtil.getEntityById(Test.class, testId);
            if (failReasonId >= 0) {
                FailReason failReason = HibernateUtil.getEntityById(FailReason.class, failReasonId);
                if (failReason.isTest()) {
                    FailReasonToTest failReasonToTest = new FailReasonToTest();
                    failReasonToTest.setFailReason(failReason);
                    failReasonToTest.setTest(test);
                    failReasonToTest.setComment(comment);
                    test.setFailReasonToTest(failReasonToTest);
                    HibernateUtil.update(test);
                } else {
                    return false;
                }
            } else {
                test.setFailReasonToTest(null);
                HibernateUtil.update(test);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}

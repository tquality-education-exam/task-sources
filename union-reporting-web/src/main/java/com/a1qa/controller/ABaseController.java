package com.a1qa.controller;

import com.a1qa.common.BreadCrumb;
import com.a1qa.common.Page;
import com.a1qa.common.utils.HibernateUtil;
import com.a1qa.model.constants.Config;
import com.a1qa.model.domain.FailReason;
import com.a1qa.model.domain.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by p.ordenko on 02.06.2015, 11:00.
 */
public abstract class ABaseController {
    protected static final List<BreadCrumb> breadCrumbs = new LinkedList<>();

    public void buildBreadCrumbs(Page page, Test test, ModelAndView modelAndView) {
        breadCrumbs.clear();
        if (test != null && test.getId() != 0L) {
            BreadCrumb breadCrumb;
            while ((breadCrumb = page.getBreadCrumb(test)) != null) {
                breadCrumbs.add(breadCrumb);
                page = page.getParent();
            }
        }
        Collections.reverse(breadCrumbs);
        modelAndView.addObject("breadCrumbs", breadCrumbs);
    }

    public void buildBreadCrumbs(Page page, List<Test> test, ModelAndView modelAndView) {
        if (test.isEmpty()) {
            buildBreadCrumbs(page, new Test(), modelAndView);
        } else {
            buildBreadCrumbs(page, test.get(0), modelAndView);
        }
    }

    public void buildPaging(int entitiesCount, int pageNumber, ModelAndView modelAndView){
        int totalPages = (int) Math.ceil((double) entitiesCount / Config.RESULTS_PER_PAGE);
        modelAndView.addObject("pTotalPages", totalPages);
        modelAndView.addObject("pCurrentPage", pageNumber);
    }

    /**
     * TODO Should be project specific except of IS_GLOBAL FailReasons
     * Get Fail Reasons which available for project
     * @param projectId Project Id
     * @return List with available Fail Reasons
     */
    public List<FailReason> getFailReasonsForProject(long projectId) {
        List<FailReason> failReasons = HibernateUtil.getEntities(FailReason.NAME_FIND_ALL);
        return failReasons;
    }
}


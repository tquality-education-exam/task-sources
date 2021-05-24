package com.a1qa.controller;

import com.a1qa.common.utils.HibernateUtil;
import com.a1qa.model.domain.Project;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by p.ordenko on 27.05.2015, 18:10.
 */
@RestController
public class ProjectController extends ABaseController {

    /**
     * Get all available projects
     * @return List of projects {@link Project}
     */
    @RequestMapping(value = "/project/get/all", method = RequestMethod.GET)
    public String getAllProject() {
        return getString(HibernateUtil.getEntities(Project.NAME_FIND_ALL));
    }

}

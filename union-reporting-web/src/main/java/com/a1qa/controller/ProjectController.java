package com.a1qa.controller;

import com.a1qa.common.NamedParam;
import com.a1qa.common.Page;
import com.a1qa.common.QueryParam;
import com.a1qa.common.utils.HibernateUtil;
import com.a1qa.entity.Result;
import com.a1qa.model.domain.Project;
import com.a1qa.model.domain.Test;
import com.a1qa.model.domain.Token;
import com.a1qa.model.domain.Variant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by p.ordenko on 01.06.2015, 12:38.
 */
@Controller
public class ProjectController extends ABaseController {

    @RequestMapping(value = "/projects", method = RequestMethod.GET)
    public ModelAndView getAllProjects(@RequestParam(value = "projectName", required = false) String projectName,
    @RequestParam(value = "isSubmitted", required = false, defaultValue = "false") boolean isSubmitted) {
        ModelAndView result = new ModelAndView("projects");
        List<Project> projects = HibernateUtil.getEntities(Project.NAME_FIND_ALL);
        buildBreadCrumbs(Page.PROJECTS, new Test(), result);
        result.addObject("projects", projects);
        Result resultMessage = saveProject(projectName);
        if (resultMessage != null) {
            result.addObject("resultMessage", resultMessage);
        }
        result.addObject("isSubmitted", isSubmitted);
        return result;
    }

    @RequestMapping(value = "/addProject", method = RequestMethod.GET)
    public ModelAndView addProject(@RequestParam(value = "projectName", required = false) String projectName,
                                   @CookieValue(value = "token", required = false) String token) {
        ModelAndView result = new ModelAndView("addProject");
        Variant variant = null;
        try {
            variant = ((Token) HibernateUtil.getEntities(Token.TOKEN_BY_VALUE,
                    new QueryParam(NamedParam.TOKEN_VALUE, token)).get(0)).getVariant();
        }
        catch (Exception ignore) {}
        if (variant != null) {
            result.addObject("variant", variant);
        }
        Result resultMessage = saveProject(projectName);
        if (resultMessage != null) {
            result.addObject("resultMessage", resultMessage);
        }

        return result;
    }

    private Result saveProject(String projectName) {
        Result resultMessage = null;
        if (projectName != null && !projectName.equals("")) {
            Project project = new Project();
            project.setName(projectName);
            try {
                HibernateUtil.save(project);
                resultMessage = new Result(true, String.format("Project %s saved", projectName));
            }
            catch (Exception e) {
                resultMessage = new Result(false, String.format("Error during saving %s project", projectName));
            }
        }
        return resultMessage;
    }

}

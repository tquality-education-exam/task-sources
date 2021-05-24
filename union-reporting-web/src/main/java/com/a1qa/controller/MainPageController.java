package com.a1qa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * Created by p.ordenko on 01.06.2015, 12:38.
 */
@Controller
@RequestMapping("/")
public class MainPageController extends ABaseController {
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getAllProjects() {
        return new ModelAndView("mainPage");
    }

}

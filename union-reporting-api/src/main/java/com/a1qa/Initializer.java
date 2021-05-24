package com.a1qa;

import com.a1qa.configuration.CommonConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by p.ordenko on 14.05.2015, 14:47.
 */
public class Initializer implements WebApplicationInitializer {

    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext acaCtx = new AnnotationConfigWebApplicationContext();
        acaCtx.register(CommonConfiguration.class);
        acaCtx.setServletContext(servletContext);
        ServletRegistration.Dynamic servlet = servletContext.addServlet(
                "dispatcher", new DispatcherServlet(acaCtx));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }
}

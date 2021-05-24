package com.a1qa.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by p.ordenko on 14.05.2015, 14:55.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.a1qa"})
public class CommonConfiguration {
}

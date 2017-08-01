/**
 * (C) Copyright 2016 HP Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are licensed
 * to the U.S. Government under vendor's standard commercial license.
 */

package com.hp.ccp.datetime;

import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

// CHECKSTYLE:OFF

/**
 * This class is responsible for starting the Spring application.
 */
@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
@SuppressWarnings({"PMD.UseUtilityClass"})
public class Application {

    /**
     * Logger object that is used to log messages for a specific application component.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    /**
     * Start Spring application.
     *
     * @param args Is used to externalize configuration.
     */
    public static void main(final String... args) {
        final ApplicationContext ctx = SpringApplication.run(Application.class, args);

        final String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (final String beanName : beanNames) {
            LOGGER.debug("Loaded bean: {}", beanName);
        }
    }
}
// CHECKSTYLE:ON

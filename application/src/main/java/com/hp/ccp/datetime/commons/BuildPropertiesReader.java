package com.hp.ccp.datetime.commons;

/**
 * (C) Copyright 2016 HP Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are licensed
 * to the U.S. Government under vendor's standard commercial license.
 */


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Implements a simple properties reader for build information.
 */
public final class BuildPropertiesReader {

    /**
     * Logger object that is used to log messages for a specific application component.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BuildPropertiesReader.class);

    /**
     * {@link Properties}
     */
    private static final Properties PROPERTIES;

    /**
     * Build file to read.
     */
    private static final String BUILD_PROPERTIES_FILE = "/build.properties";

    /**
     * Property containing the project version.
     */
    private static final String PROJECT_VERSION = "project.current.version";

    static {
        final InputStream inputStream = BuildPropertiesReader.class.getResourceAsStream(BUILD_PROPERTIES_FILE);
        PROPERTIES = new Properties();
        try {
            PROPERTIES.load(inputStream);
            LOGGER.debug("File " + BUILD_PROPERTIES_FILE + " loaded with success");
        } catch (IOException e) {
            LOGGER.error("Failed to read properties file. ExceptionMessage={}.", e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("Error closing the file stream. ExceptionMessage={}.", e.getMessage());
                }
            }
        }
    }

    /**
     * Hidden constructor
     */
    private BuildPropertiesReader() {
    }

    /**
     * Gets project version from property file.
     *
     * @return project version
     */
    public static String getProjectVersion() {
        if (PROPERTIES != null && PROPERTIES.containsKey(PROJECT_VERSION)) {
            return PROPERTIES.getProperty(PROJECT_VERSION);
        } else {
            return "No scm version info available";
        }
    }
}
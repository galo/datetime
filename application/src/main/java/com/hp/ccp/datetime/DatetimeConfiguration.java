/**
 * (C) Copyright 2016 HP Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are licensed
 * to the U.S. Government under vendor's standard commercial license.
 */

package com.hp.ccp.datetime;

// CHECKSTYLE:OFF

// DateTimeConfiguration is a utility class, and according to Checkstyle rules it should be
// declared as final. However, a @Configuration class cannot be final according to Spring
// documentation. Thus, the Checkstyle verification is suppressed here.

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Config class.
 */
@Configuration
@ConfigurationProperties(prefix = "endpoints.datetime")
@SuppressWarnings("PMD.UseUtilityClass")
public class DatetimeConfiguration {

    private static String authKey;

    private static String httpBinUrl;

    public static String getAuthKey() {
        return authKey;
    }

    public static void setAuthKey(final String authKey) {
        DatetimeConfiguration.authKey = authKey;
    }

    public static String getHttpBinUrl() {
        return httpBinUrl;
    }

    public static void setHttpBinUrl(String httpBinUrl) {
        DatetimeConfiguration.httpBinUrl = httpBinUrl;
    }
}

// CHECKSTYLE:ON

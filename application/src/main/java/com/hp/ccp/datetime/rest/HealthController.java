/**
 * (C) Copyright 2016 HP Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are licensed
 * to the U.S. Government under vendor's standard commercial license.
 */

package com.hp.ccp.datetime.rest;

import com.hp.ccp.datetime.commons.BuildPropertiesReader;
import org.springframework.http.HttpStatus;

import com.hp.ccp.datetime.DatetimeConfiguration;
import com.hp.ccp.datetime.commons.Constants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * This is the controller class that receives calls to check the health of the system.
 */
@RestController
@Api(
        value = Constants.HEALTH_ENDPOINT,
        description = "Operations to health the heartbeat of the services."
)
public class HealthController {

    /**
     * Logger object that is used to log messages for a specific application component.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthController.class);

    /**
     * {@link DatetimeConfiguration}.
     */
    @Autowired
    private DatetimeConfiguration dateTimeConfiguration;

    /**
     * String used to signalize that the extended health check was called.
     */
    private static final String EXTENDED_HEALTH_CHECK = "This is the extended health check";

    /**
     * This method handles the health request.
     *
     * @param authKey valid authentication key for extended checking
     * @return {@link ResponseEntity}
     * @throws IOException if an error occurs with OCR service.
     */
    @RequestMapping(value = Constants.HEALTH_ENDPOINT, method = RequestMethod.GET)
    @ApiOperation(value = "This method handles the health check request.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "The request was successful and the application is healthy."),
                    @ApiResponse(code = 500, message = "Some problems occurred and some of"
                            + " the components are DOWN. Application is unhealthy.")
            }
    )
    public ResponseEntity<?> health(@RequestParam(value = "authKey", required = false) final String authKey)
            throws IOException {
        LOGGER.info("health check endpoint invoked.");
        Health health;
        String hostname;
        final String version = BuildPropertiesReader.getProjectVersion();
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException exception) {
            LOGGER.error("Hostname could not be solved, using localhost instead");
            hostname = "localhost";
        }
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        if (authKey == null) {
            health = Health.up()
                    .withDetail("date-time", LocalDateTime.now().format(formatter))
                    .withDetail("hostname", hostname)
                    .withDetail("version", version)
                    .build();
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(health);
        } else {
            final String authKeyValue = dateTimeConfiguration.getAuthKey();
            if (authKeyValue.equals(authKey)) {
                health = Health.up()
                        .withDetail("date-time", LocalDateTime.now().format(formatter))
                        .withDetail("hostname", hostname)
                        .withDetail("version", version)
                        .withDetail("extended", EXTENDED_HEALTH_CHECK)
                        .build();
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(health);
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .build();
            }
        }
    }
}

/**
 * (C) Copyright 2016 HP Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are licensed
 * to the U.S. Government under vendor's standard commercial license.
 */

package com.hp.ccp.datetime.rest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import com.hp.ccp.datetime.DatetimeConfiguration;

import org.springframework.http.HttpStatus;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

@RunWith(MockitoJUnitRunner.class)
public class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @BeforeClass
    public static void setup() {
        DatetimeConfiguration.setAuthKey("authKey-dev");
    }

    @Test
    public void health_withSuccess_shouldReturnUp() throws IOException {
        String expectedVersion = "1.0-TEST-SNAPSHOT";

        ResponseEntity<?> response = healthController.health(null);
        Health health = (Health) response.getBody();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(health.getStatus(), is(Status.UP));
        assertThat(health.getDetails().get("version").toString(), is(expectedVersion));
        assertThat(health.getDetails().get("hostname").toString(), is(notNullValue()));
        assertThat(health.getDetails().get("date-time").toString(), is(notNullValue()));
    }

    @Test
    public void health_extendedHealthCheckwithSuccess_shouldReturnUp() throws IOException {
        String expectedVersion = "1.0-TEST-SNAPSHOT";
        String authKey = DatetimeConfiguration.getAuthKey();

        ResponseEntity<?> response = healthController.health(authKey);
        Health health = (Health) response.getBody();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(health.getStatus(), is(Status.UP));
        assertThat(health.getDetails().get("version").toString(), is(expectedVersion));
        assertThat(health.getDetails().get("hostname").toString(), is(notNullValue()));
        assertThat(health.getDetails().get("date-time").toString(), is(notNullValue()));
        assertThat(health.getDetails().get("extended").toString(), is(notNullValue()));
    }

    @Test
    public void health_withInvalidAuthKey_shouldReturnUnauthorized() throws IOException {
        ResponseEntity<?> response = healthController.health("INVALID_KEY");
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
    }
}

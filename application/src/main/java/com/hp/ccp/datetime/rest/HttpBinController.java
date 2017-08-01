/**
 * (C) Copyright 2016 HP Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are licensed
 * to the U.S. Government under vendor's standard commercial license.
 */

package com.hp.ccp.datetime.rest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hp.ccp.datetime.DatetimeConfiguration;
import com.hp.ccp.datetime.commons.Constants;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * This is the controller for the HttpBin API
 */
@RestController
public class HttpBinController {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpBinController.class);

    private static final String HTTP_NOW_HTTPBIN_ORG = "http://now.httpbin.org/";

    /**
     * {@link DatetimeConfiguration}.
     */
    @Autowired
    private DatetimeConfiguration dateTimeConfiguration;


    /**
     * This method handles the now request.
     *
     * @return {@link ResponseEntity}
     * @throws IOException if an error occurs.
     */
    @RequestMapping(value = Constants.NOW_ENDPOINT, method = RequestMethod.GET)
    @ApiOperation(value = "This method handles the now request.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "The request was successful and time responded.")
            }
    )
    public ResponseEntity<?> now()
            throws IOException {

        String httpBinUrl = dateTimeConfiguration.getHttpBinUrl();
        if (httpBinUrl == null) {
            httpBinUrl = HTTP_NOW_HTTPBIN_ORG;
        }

        RestTemplate restTemplate = new RestTemplate();
        NowService nowResp = restTemplate.getForObject(httpBinUrl, NowService.class);
        LOGGER.info(nowResp.toString());


        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(nowResp.now);
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class NowService {
        Now now;

        public Now getNow() {
            return now;
        }

        public void setNow(Now now) {
            this.now = now;
        }

        @Override
        public String toString() {
            return "NowService{" +
                    "now=" + now +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    static public class Now {
        Long epoch;

        @JsonProperty("slang_date")
        String slangDate;
        String iso8601;
        String rfc3339;

        public Long getEpoch() {
            return epoch;
        }

        public void setEpoch(Long epoch) {
            this.epoch = epoch;
        }

        public String getSlangDate() {
            return slangDate;
        }

        public void setSlangDate(String slangDate) {
            this.slangDate = slangDate;
        }

        public String getIso8601() {
            return iso8601;
        }

        public void setIso8601(String iso8601) {
            this.iso8601 = iso8601;
        }

        public String getRfc3339() {
            return rfc3339;
        }

        public void setRfc3339(String rfc3339) {
            this.rfc3339 = rfc3339;
        }

        @Override
        public String toString() {
            return "Now{" +
                    "epoch=" + epoch +
                    ", slangDate='" + slangDate + '\'' +
                    ", iso8601='" + iso8601 + '\'' +
                    ", rfc3339='" + rfc3339 + '\'' +
                    '}';
        }
    }
}


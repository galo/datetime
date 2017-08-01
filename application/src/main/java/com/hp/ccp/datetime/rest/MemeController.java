/**
 * (C) Copyright 2016 HP Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are licensed
 * to the U.S. Government under vendor's standard commercial license.
 */

package com.hp.ccp.datetime.rest;


import com.hp.ccp.datetime.DatetimeConfiguration;
import com.hp.ccp.datetime.commons.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;

/**
 * This is the controller for the meme generator API
 */
@RestController
@Api(
        value = Constants.MEME_ENDPOINT,
        description = "Operations to generate memes."
)
public class MemeController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MemeController.class);

    /**
     * {@link DatetimeConfiguration}.
     */
    @Autowired
    private DatetimeConfiguration dateTimeConfiguration;


    @Autowired
    private StringRedisTemplate redis;

    /**
     * This method handles the create meme request.
     *
     * @param authKey valid authentication key for extended checking
     * @return {@link ResponseEntity}
     * @throws IOException if an error occurs.
     */
    @RequestMapping(value = Constants.MEME_ENDPOINT + "/{meme-id}", method = RequestMethod.PUT)
    @ApiOperation(value = "This method handles create meme request.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "The request was successful."),
                    @ApiResponse(code = 403, message = "The request is forbidden, invalid auth key."),
            }
    )
    public ResponseEntity<?> newMeme(
            @RequestHeader(value = "x-auth-key", required = true) final String authKey,
            @RequestHeader(value = "x-user-id", required = true) final String userId,
            @PathVariable(value = "meme-id") final String memeId,
            @RequestParam(value = "message", required = true) final String message) {

        if (!validAuthKey(authKey)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        // Write the meme in the Db
        ValueOperations<String, String> ops = this.redis.opsForValue();
        String key = "spring.boot.redis.memes." + memeId;

        LOGGER.info("Adding meme {} for user {}", key, userId);
        ops.setIfAbsent(key, message);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    /**
     * This method handles the get meme request.
     *
     * @param authKey valid authentication key for extended checking
     * @return {@link ResponseEntity}
     * @throws IOException if an error occurs with OCR service.
     */
    @RequestMapping(value = Constants.MEME_ENDPOINT + "/{meme-id}", method = RequestMethod.GET)
    @ApiOperation(value = "This method handles get meme request.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "The request was successful."),
                    @ApiResponse(code = 403, message = "The request is forbidden, invalid auth key."),
            }
    )
    public ResponseEntity<?> getMeme(
            @RequestHeader(value = "x-auth-key", required = true) final String authKey,
            @RequestHeader(value = "x-user-id", required = true) final String userId,
            @PathVariable(value = "meme-id") final String memeId) {

        if (!validAuthKey(authKey)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        ValueOperations<String, String> ops = this.redis.opsForValue();
        String key = "spring.boot.redis.memes." + memeId;

        LOGGER.info("Get meme {} for user {}", key, userId);
        String meme = ops.get(key);

        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(meme);

    }

    /**
     * This method handles the delete meme request.
     *
     * @param authKey valid authentication key for extended checking
     * @return {@link ResponseEntity}
     * @throws IOException if an error occurs with OCR service.
     */
    @RequestMapping(value = Constants.MEME_ENDPOINT + "/{meme-id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "This method handles delete meme request.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "The request was successful."),
                    @ApiResponse(code = 403, message = "The request is forbidden, invalid auth key."),
            }
    )
    public ResponseEntity<?> removeMeme(
            @RequestHeader(value = "x-auth-key", required = true) final String authKey,
            @RequestHeader(value = "x-user-id", required = true) final String userId,
            @PathVariable(value = "meme-id") final String memeId) {

        if (!validAuthKey(authKey)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .build();
        }

        String key = "spring.boot.redis.memes." + memeId;
        LOGGER.info("Delete meme {} for user {}", key, userId);
        this.redis.delete(key);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .contentType(MediaType.APPLICATION_JSON)
                .build();
    }


    @Bean
    public JedisConnectionFactory connectionFactory() {
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory();
        connectionFactory.setHostName("redis");
        connectionFactory.setPort(6379);
        return connectionFactory;
    }

    // validates authKey
    private boolean validAuthKey(final String authKey) {
        // Validate authKey
        final String authKeyValue = dateTimeConfiguration.getAuthKey();
        return authKey != null && authKey.equals(authKeyValue);

    }

}

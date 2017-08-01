/**
 * (C) Copyright 2016 HP Development Company, L.P.
 * Confidential computer software. Valid license from HP required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial Items are licensed
 * to the U.S. Government under vendor's standard commercial license.
 */

package com.hp.ccp.datetime.rest;

import com.hp.ccp.datetime.commons.Constants;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is the controller class that receives calls to retrieve images.
 */
@RestController
@Api(
        value = Constants.IMAGE_ENDPOINT,
        description = "Operations to retrieve images."
)
public class ImageController {

    /**
     * Logger object that is used to log messages for a specific application component.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    /**
     * This method retrieves an image.
     *
     * @return an image
     * @throws IOException if an error occurs in the api
     * @throws URISyntaxException if an error occurs to get the file path
     */
    @RequestMapping(value = Constants.IMAGE_ENDPOINT, method = RequestMethod.GET)
    public ResponseEntity<byte[]> retrieveImage() throws IOException, URISyntaxException {
        LOGGER.info("image endpoint invoked.");

        File file;
        final URL resourceImage = ClassLoader.getSystemResource("image.jpg");

        if (resourceImage == null) {
            file = new File("/app/image.jpg");
        } else {
            final Path path = Paths.get(resourceImage.toURI());
            file = path.toFile();
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(
                IOUtils.toByteArray(new FileInputStream(file)),
                headers,
                HttpStatus.OK);
    }
}

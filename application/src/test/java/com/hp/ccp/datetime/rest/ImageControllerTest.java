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

import org.springframework.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;

@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {

    @InjectMocks
    private ImageController imageController;

    @Test
    public void getImage_withSuccess_shouldReturnImage() throws IOException, URISyntaxException {
       final ResponseEntity<byte[]> response = this.imageController.retrieveImage();
        assertThat(response, is(notNullValue()));
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getHeaders().getContentType(), is(MediaType.IMAGE_JPEG));
    }
}

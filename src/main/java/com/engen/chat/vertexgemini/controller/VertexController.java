package com.engen.chat.vertexgemini.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.HttpStatus;

import java.util.Base64;
import java.util.Map;

@RestController
public class VertexController {

    public static Log logger = LogFactory.getLog(VertexController.class);

    /**
     *
     * @return String
     */
    @RequestMapping(path = "/healthcheck", method = RequestMethod.GET)
    public ResponseEntity healthcheck() {
        return ResponseEntity.ok("alive");

    }

    /**
     *
     * @param request
     * @return decoded token value
     * @throws JWTDecodeException
     */

    @RequestMapping(path = "/identity", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity identity() {

        String authInfo = "blah blah";
        byte data[] = Base64.getDecoder().decode(authInfo);
        return ResponseEntity.ok(new String(data));

    }

}
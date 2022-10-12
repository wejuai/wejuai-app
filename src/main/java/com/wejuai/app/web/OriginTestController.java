package com.wejuai.app.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ZM.Wang
 */
@RestController
@RequestMapping("/test")
public class OriginTestController {

    private final static Logger logger = LoggerFactory.getLogger(OriginTestController.class);

    @GetMapping
    public void getUsers(HttpServletRequest request) {
        logger.info(request.getLocalAddr());
        logger.info(request.getContextPath());
        logger.info(request.getServletPath());
    }
}

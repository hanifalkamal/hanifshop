package com.hanif.test.controller;

import com.hanif.test.util.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 * @author Hanif al kamal 10/10/23
 * @contact hanif.alkamal@gmail.com
 */
@Controller
public interface PersonController {

    /**
     * For get person information
     */
    @GetMapping(Constant.ControllerRoute.personRoute)
    ResponseEntity<?> personInquiry();

}

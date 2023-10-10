package com.hanif.test.controller.impl;

import com.hanif.test.controller.PersonController;
import com.hanif.test.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.Map;

/**
 * @author Hanif al kamal 10/10/23
 * @contact hanif.alkamal@gmail.com
 */
@Controller
public class PersonControllerImpl implements PersonController {
    @Autowired
    private PersonService personService;

    @Override
    public ResponseEntity<?> personInquiry() {
        Map<String, Object> mapping = personService.personInquiry();
        return new ResponseEntity<>(mapping,
                mapping.containsKey("error") ?
                        HttpStatus.valueOf(
                                Integer.parseInt(mapping.get("status").toString()))
                        : HttpStatus.OK);
    }

}

package com.hanif.test.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface PersonService {

    /**
     * person inquiry service
     */
    Map<String, Object> personInquiry();

}

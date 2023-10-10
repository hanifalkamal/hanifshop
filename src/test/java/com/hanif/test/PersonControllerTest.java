package com.hanif.test;

import com.hanif.test.controller.PersonController;
import com.hanif.test.util.Constant;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanif al kamal 10/10/23
 * @contact hanif.alkamal@gmail.com
 */

@WebMvcTest(PersonController.class)
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonController personController;


    @Test
    public void personInquirySuccess() throws Exception {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("address", "Random Address");
        mapping.put("gender", "unknown");
        mapping.put("fullname", "Mr Hanif Al Kamal");
        mapping.put("picture", "https://randomuser.me/api/portraits/men/42.jpg");

        Mockito.when(personController.personInquiry()).thenReturn(new ResponseEntity(mapping, HttpStatus.OK));

        mockMvc.perform(MockMvcRequestBuilders.get(Constant.ControllerRoute.personRoute))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullname").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.picture").isNotEmpty());
    }


    @Test
    public void personInquiryFailed() throws Exception {
        Mockito.when(personController.personInquiry())
                .thenThrow(new RuntimeException("Test Failed Exception Message"))
                .thenReturn(null);


        mockMvc.perform(MockMvcRequestBuilders.get(Constant.ControllerRoute.personRoute))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").exists());
    }

    @Test
    public void personInquiryServiceFailed() throws Exception {
        Map<String, Object> mapping = new HashMap<>();
        mapping.put("timestamp", LocalDateTime.now());
        mapping.put("status", 500);
        mapping.put("error", "RESULTS IS NULL");
        mapping.put("path", Constant.ControllerRoute.personRoute);

        Mockito.when(personController.personInquiry()).thenReturn(new ResponseEntity(mapping, HttpStatus.INTERNAL_SERVER_ERROR));


        mockMvc.perform(MockMvcRequestBuilders.get(Constant.ControllerRoute.personRoute))
                .andExpect(MockMvcResultMatchers.status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.path").exists());
    }
}

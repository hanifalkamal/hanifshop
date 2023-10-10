package com.hanif.test.service.impl;

import com.hanif.test.model.Person;
import com.hanif.test.model.Result;
import com.hanif.test.repository.ApiService;
import com.hanif.test.service.PersonService;
import com.hanif.test.util.Constant;
import com.hanif.test.util.EngineUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hanif al kamal 10/10/23
 * @contact hanif.alkamal@gmail.com
 */

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private ApiService apiService;

    @Override
    public Map<String, Object> personInquiry() {
        Map<String, Object> mapping = new HashMap<>();
        try {

            Call<Person> call = apiService.getPersonInfo();

            Response<Person> response = call.execute();

            if (!response.isSuccessful()) {
                return EngineUtils.createFailedReponse(response.code(), response.message(), Constant.ControllerRoute.personRoute);
            }

            Person person = response.body();

            if (person == null){
                throw new RuntimeException("PERSON IS NULL");
            }

            if (person.getResults() == null){
                throw new RuntimeException("RESULTS IS NULL");
            }

            Result result = person.firstResult();
            mapping.put("gender", result.getGender());
            mapping.put("fullname", result.getName().fullName());
            mapping.put("address", result.getLocation().address());
            mapping.put("picture", result.getPicture().getLarge());
            return mapping;

        } catch (RuntimeException | IOException re) {
            re.printStackTrace();
            return EngineUtils.createFailedReponse(500, re.getMessage(), Constant.ControllerRoute.personRoute);
        }
    }


}

package com.hanif.test.repository;

import com.hanif.test.model.Person;
import com.hanif.test.util.Constant;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.http.GET;

@Service
public interface ApiService {

    @GET(Constant.randomUserPath)
    Call<Person> getPersonInfo();
}

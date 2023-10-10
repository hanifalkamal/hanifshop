package com.hanif.test.util;

import com.hanif.test.repository.ApiService;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * @author Hanif al kamal 10/10/23
 * @contact hanif.alkamal@gmail.com
 */
@Configuration
public class RetrofitConfig {


    @Bean
    public Retrofit retrofit() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(Constant.baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
    }

    @Bean
    public ApiService apiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

}

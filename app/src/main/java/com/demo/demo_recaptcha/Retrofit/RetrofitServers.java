package com.demo.demo_recaptcha.Retrofit;


import com.demo.demo_recaptcha.Retrofit.responsePara.VerifyResponse;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface RetrofitServers {

    @Headers({
//            "Accept:application/json",
            "Content-Type:application/json"
    })
    @POST()
    Call<VerifyResponse> verify(@Url String verifyData);


}

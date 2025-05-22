package com.example.diary;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface TranslationApi {
    @GET
    Call<TranslationResponse> getTranslation(@Url String url);
}

package com.example.diary;

import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ZenQuotesApi {
    @GET("api/quotes")
    Call<List<Quote>> getQuotes();
}


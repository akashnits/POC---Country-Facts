package com.example.akash.proofofconcept.networkUtils;

import com.example.akash.proofofconcept.model.Country;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {
    @GET("facts")
    public Single<Country> getCountry();
}

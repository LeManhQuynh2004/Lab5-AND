package com.quynhlm.dev.lab5.Api;

import com.quynhlm.dev.lab5.Model.Distribute;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServer {
    @GET("/")
    Call<List<Distribute>> getAllData();
    @POST("/")
    Call<Void> createUser(@Body Distribute distribute);
    @DELETE("/{id}")
    Call<Void> deleteDistribute(@Path("id") String distributeId);
    @GET("/search")
    Call<List<Distribute>> search(@Query("key") String keyword);
}

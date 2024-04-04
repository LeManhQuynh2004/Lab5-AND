package com.quynhlm.dev.lab5.Api;

import com.quynhlm.dev.lab5.Model.Distribute;
import com.quynhlm.dev.lab5.Model.Fruits;
import com.quynhlm.dev.lab5.Model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiServer {
    @GET("/")
    Call<List<Distribute>> getAllDistributor();
    @POST("/")
    Call<Void> createUser(@Body Distribute distribute);
    @DELETE("/{id}")
    Call<Void> deleteDistribute(@Path("id") String distributeId);
    @GET("/search")
    Call<List<Distribute>> search(@Query("key") String keyword);
    @PUT("/{id}")
    Call<Distribute> updateData(@Path("id") String id, @Body Distribute distribute);
    @GET("/")
    Call<List<Distribute>> getAllData(@Header("Authorization") String token);
    @GET("/fruits")
    Call<List<Fruits>> getAllData();
    @Multipart
    @POST("/uploadFile")
    Call<Void> addFruitWithFileImage(@PartMap Map<String, RequestBody> requestBodyMap,
                                                @Part ArrayList<MultipartBody.Part> images
    );
    @Multipart
    @POST("/users/uploadFile")
    Call<Void> createUser(
            @PartMap Map<String, RequestBody> requestBodyMap,
            @Part MultipartBody.Part image
    );
    @GET("/users")
    Call<List<User>> getAllUsers();

    @POST("/login")
    Call<Void> login (@Body User user);
    @POST("/auth/token")
    Call<String> getToken(@Header("Authorization") String authHeader);
}

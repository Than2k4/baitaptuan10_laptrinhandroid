package com.example.upload_file_image;

import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ServiceAPI {
    // Khởi tạo Retrofit
    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    ServiceAPI servieapi = new Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ServiceAPI.class);

    // API Upload ảnh
    @Multipart
    @POST("upload.php")
    Call<List<ImageUpload>> upload(@Part(Const.MY_USERNAME) RequestBody username,
                                   @Part MultipartBody.Part avatar);

    @Multipart
    @POST("upload1.php")
    Call<Message> upload1(@Part(Const.MY_USERNAME) RequestBody username,
                          @Part MultipartBody.Part avatar);
}
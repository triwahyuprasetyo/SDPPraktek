package com.example.why.sdppraktek.retrofit;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by why on 8/1/16.
 */
public interface KtpService {
    @GET("android/list.php")
    Call<KtpWrapper> listKtps();

    @Multipart
    @POST("android/upload.php")
    Call<KtpWrapper> uploadFoto(@Part("description") RequestBody description,
                                  @Part MultipartBody.Part file);

    @Multipart
    @POST("android/upload.php")
    Call<ResponseBody> uploadFoto2(@Part("title") RequestBody description,
                                @Part MultipartBody.Part file);

}

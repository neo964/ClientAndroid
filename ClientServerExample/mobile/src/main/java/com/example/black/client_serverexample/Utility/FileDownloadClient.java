package com.example.black.client_serverexample.Utility;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface FileDownloadClient {
    @GET ("images/futurestudio-university-logo.png")
    Call<ResponseBody> downloadFile();


    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileStream (@Url String url);

}

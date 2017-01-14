package ru.nadocars.messanger.http;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.nadocars.messanger.api.HttpApi;
import ru.nadocars.messanger.api.HttpEndpointsApi;

public class RetrofitFactory {

    private static HttpEndpointsApi mHttpEndpointsApi;

    public static HttpEndpointsApi getHttpEndpointApi() {
        if (null == mHttpEndpointsApi) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(HttpApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                    .client(new OkHttpClient.Builder()
                            .readTimeout(60, TimeUnit.SECONDS)
                            .connectTimeout(60, TimeUnit.SECONDS)
                            .build())
                    .build();
            mHttpEndpointsApi = retrofit.create(HttpEndpointsApi.class);
            return mHttpEndpointsApi;
        } else {
            return mHttpEndpointsApi;
        }
    }

}

package ru.nadocars.messanger.api;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.nadocars.messanger.json.GetUserResponse;

public interface HttpEndpointsApi {

    @FormUrlEncoded
    @POST(HttpApi.USER_GET)
    Call<GetUserResponse> getUserInfo(@Field("access_token") String token);

    @FormUrlEncoded
    @POST(HttpApi.USER_UPDATE)
    Call<String> updateUser(@Field("email") String email,
                       @Field("phone") String phone,
                       @Field("access_token") String token);

}


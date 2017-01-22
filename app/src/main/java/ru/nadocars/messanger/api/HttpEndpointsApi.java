package ru.nadocars.messanger.api;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import ru.nadocars.messanger.json.car.GetCarsResponse;
import ru.nadocars.messanger.json.car.calendar.GetCarCalendarResponse;
import ru.nadocars.messanger.json.user.GetUserResponse;

public interface HttpEndpointsApi {

    @FormUrlEncoded
    @POST(HttpApi.USER_GET)
    Call<GetUserResponse> getUserInfo(@Field("access_token") String token);

    @FormUrlEncoded
    @POST(HttpApi.GET_CARS_CALENDAR)
    Call<GetCarCalendarResponse> getCarCalendar(@Field("car_id") String carId);

    @FormUrlEncoded
    @POST(HttpApi.USER_UPDATE)
    Call<Object> updateUser(@Field("email") String email,
                            @Field("phone") String phone,
                            @Field("access_token") String token);

    @FormUrlEncoded
    @POST(HttpApi.ADD_BUSY_DAYS)
    Call<Object> addBusyDays(@Field("access_token") String token,
                             @Field("car_id") String carId,
                             @Field("date_start") String dateStart,
                             @Field("time_start") String timeStart,
                             @Field("date_end") String dateEnd,
                             @Field("time_end") String timeEnd);

    @FormUrlEncoded
    @POST(HttpApi.USER_UPDATE)
    Call<Object> updateUser(@Field("email") String email,
                            @Field("phone") String phone,
                            @Field("access_token") String token,
                            @Field("session_id") String sessionId,
                            @Field("code") long code);

    @FormUrlEncoded
    @POST(HttpApi.CARS_GET)
    Call<GetCarsResponse> getCars(@Field("access_token") String token,
                                  @Field("offset") int offset,
                                  @Field("count") int count);

    @FormUrlEncoded
    @POST(HttpApi.UPDATE_CAR_PRICE)
    Call<ResponseBody> updateCarPrice(@Field("access_token") String token,
                                      @Field("car_id") String carId,
                                      @Field("type") String type,
                                      @Field("price") String price);

    @FormUrlEncoded
    @POST(HttpApi.DELETE_CAR_PHOTO)
    Call<ResponseBody> deleteCarPhoto(@Field("access_token") String token,
                                      @Field("car_id") String carId,
                                      @Field("photo_id") String photoId);

//    @Multipart
//    @POST(HttpApi.UPLOAD_USER_AVATAR)
//    Call<ResponseBody> uploadUserAvatar(@Part("access_token") RequestBody token,
//                                        @Part("avatar") RequestBody file);

    @Multipart
    @POST(HttpApi.UPLOAD_USER_AVATAR)
    Call<ResponseBody> uploadUserAvatar(@Part("access_token") RequestBody token,
                                        @Part MultipartBody.Part file);

    @Multipart
    @POST(HttpApi.UPLOAD_CAR_PHOTO)
    Call<ResponseBody> uploadCarPhoto(@Part("access_token") RequestBody token,
                                      @Part("car_id") RequestBody carId,
                                      @Part("car_photo") RequestBody file);

}


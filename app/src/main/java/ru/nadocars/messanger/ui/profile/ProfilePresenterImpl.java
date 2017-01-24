package ru.nadocars.messanger.ui.profile;

import android.support.v4.view.PagerAdapter;
import android.webkit.MimeTypeMap;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nadocars.messanger.api.HttpEndpointsApi;
import ru.nadocars.messanger.http.RetrofitFactory;
import ru.nadocars.messanger.json.car.GetCarsResponse;
import ru.nadocars.messanger.json.car.calendar.GetCarCalendarResponse;
import ru.nadocars.messanger.json.car.photo.UploadPhotoResponse;
import ru.nadocars.messanger.json.user.GetUserResponse;
import ru.nadocars.messanger.json.user.update.error102.UserUpdateError102;
import ru.nadocars.messanger.json.user.update.error103.UserUpdateError103;
import ru.nadocars.messanger.json.user.update.error104.UserUpdateError104;

public class ProfilePresenterImpl implements ProfilePresenter {

    private static ProfilePresenterImpl profilePresenter = new ProfilePresenterImpl();
    private ProfileView mProfileView;
    private HttpEndpointsApi mHttpEndpointApi;
    private GetCarsResponse mGetCarsResponse;
    private PagerAdapter mPagerAdapter;

    private ProfilePresenterImpl() {
        mHttpEndpointApi = RetrofitFactory.getHttpEndpointApi();
    }

    public static ProfilePresenter getPreLoginPresenter() {
        return profilePresenter;
    }

    @Override
    public void setView(ProfileView view) {
        mProfileView = view;
    }

    public GetCarsResponse getGetCarsResponse() {
        return mGetCarsResponse;
    }

    @Override
    public void getUserInfo(String token) {
        Call<GetUserResponse> userInfo = mHttpEndpointApi.getUserInfo(token);
        userInfo.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (null != response && response.isSuccessful()) {
                    if (null != mProfileView) {
                        mProfileView.setProfileInfo(response.body());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                if (null != mProfileView) {
                    mProfileView.showError("Ошибка сервера");
                }
            }
        });
    }

    @Override
    public void updateUserInfo(final String email, final String phoneNumber, final String token) {
        Call<Object> updateUserCall = mHttpEndpointApi.updateUser(email, phoneNumber, token);
        updateUserCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                System.out.println();
                if (response.body().toString().contains("104")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError104 userUpdateError104 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError104.class);
                    if (null != mProfileView) {
                        mProfileView.requestVerificationCode(email, phoneNumber, token,
                                userUpdateError104.getError().getErrorDetail().getCode(),
                                userUpdateError104.getError()
                                        .getErrorDetail().getSessionId());
                    }
                } else if (response.body().toString().contains("103")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError103 userUpdateError103 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError103.class);
                    if (null != mProfileView) {
                        mProfileView.showError(userUpdateError103.getError().getErrorMsg());
                    }
                } else if (response.body().toString().contains("102")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError102 userUpdateError102 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError102.class);
                    if (null != mProfileView) {
                        mProfileView.showError(userUpdateError102.getError().getErrorMsg());
                    }
                } else if (response.body().toString().equals("1.0")) {
                    if (null != mProfileView) {
                        mProfileView.hideUpdateButton();
                        mProfileView.showError("Данные успешно обновлены");
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (null != mProfileView) {
                    mProfileView.showError("Ошибка сервера");
                }
            }
        });
    }

    @Override
    public void updateUserInfo(String email, String phoneNumber, String token,
                               String sesionId, long code) {
        Call<Object> updateUserCall =
                mHttpEndpointApi.updateUser(email, phoneNumber, token, sesionId, code);
        updateUserCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.body().toString().contains("104")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError104 userUpdateError104 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError104.class);
                    if (null != mProfileView) {
                        mProfileView.showError(userUpdateError104.getError().getErrorMsg());
                    }
                } else if (response.body().toString().contains("103")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError103 userUpdateError103 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError103.class);
                    if (null != mProfileView) {
                        mProfileView.showError(userUpdateError103.getError().getErrorMsg());
                    }
                } else if (response.body().toString().contains("102")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError102 userUpdateError102 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError102.class);
                    if (null != mProfileView) {
                        mProfileView.showError(userUpdateError102.getError().getErrorMsg());
                    }
                } else if (response.body().toString().equals("1.0")) {
                    if (null != mProfileView) {
                        mProfileView.hideCodeLayout();
                        mProfileView.showError("Обновление успешно");
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (null != mProfileView) {
                    mProfileView.showError("Ошибка сервера");
                }
            }
        });
    }

    @Override
    public void getCars(String token) {
        Call<GetCarsResponse> getCarsCall = mHttpEndpointApi.getCars(token, 0, 100);
        getCarsCall.enqueue(new Callback<GetCarsResponse>() {
            @Override
            public void onResponse(Call<GetCarsResponse> call, Response<GetCarsResponse> response) {
                if (response.isSuccessful()) {
                    if (null != mProfileView) {
                        mGetCarsResponse = response.body();
                        mProfileView.setCarsInfo();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetCarsResponse> call, Throwable t) {
                if (null != mProfileView) {
                    mProfileView.showError("Ошибка соединения с сервером");
                }
            }
        });
    }

    @Override
    public void uploadAvatar(String token, String uri, final boolean restartActivityOnSuccess) {
        File file = new File(uri);
        RequestBody requestFile = RequestBody.create(MediaType.parse(MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri))), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", file.getName(),
                requestFile);
        RequestBody tokenBody = RequestBody.create(okhttp3.MultipartBody.FORM, token);
        Call<ResponseBody> call = mHttpEndpointApi.uploadUserAvatar(tokenBody, body);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (restartActivityOnSuccess) {
                    if (null != mProfileView) {
                        mProfileView.restartActivity();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println();
            }
        });
    }

    @Override
    public void uploadCarPhoto(final String token, String carId, String uri, final boolean restartActivityOnSuccess) {
        File file = new File(uri);
        RequestBody requestFile = RequestBody.create(MediaType.parse(MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(uri))), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("photo1", file.getName(),
                requestFile);
        RequestBody tokenBody = RequestBody.create(okhttp3.MultipartBody.FORM, token);
        RequestBody carIdBody = RequestBody.create(okhttp3.MultipartBody.FORM, carId);
        Call<UploadPhotoResponse> call = mHttpEndpointApi.uploadCarPhoto(tokenBody, carIdBody, body);
        call.enqueue(new Callback<UploadPhotoResponse>() {
            @Override
            public void onResponse(Call<UploadPhotoResponse> call, Response<UploadPhotoResponse> response) {
                if (response.isSuccessful() && null != response.body()) {
                    if (restartActivityOnSuccess) {
                        if (null != mProfileView) {
                            mProfileView.restartActivity();
                        }
                    } else {
                        if (null != mProfileView) {
                            UploadPhotoResponse uploadPhotoResponse = response.body();
                            mProfileView.setCarPhotoId(uploadPhotoResponse.getResponse().get(0).getId());
                            mProfileView.showError("Фото загружено");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadPhotoResponse> call, Throwable t) {
                if (null != mProfileView) {
                    mProfileView.showError("Ошибка сервера");
                }
            }
        });
    }

    @Override
    public void deleteCarPhoto(final String token, final String carId, final String photoId) {
        Call<Object> deleteCarPhotoCall = mHttpEndpointApi.deleteCarPhoto(token, carId, photoId);
        deleteCarPhotoCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (null != response && response.isSuccessful()) {
                    response.body().toString().equals("1.0");
                    if (null != mProfileView) {
                        mProfileView.deletePhotoFromViewPager(carId, photoId);
                        mProfileView.showError("Фото удалено");
                    }
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                if (null != mProfileView) {
                    mProfileView.showError("Ошибка сервера");
                }
            }
        });
    }

    @Override
    public void getCarCalendar(String carId) {
        Call<GetCarCalendarResponse> carCalendarCall = mHttpEndpointApi.getCarCalendar(carId);
        carCalendarCall.enqueue(new Callback<GetCarCalendarResponse>() {
            @Override
            public void onResponse(Call<GetCarCalendarResponse> call, Response<GetCarCalendarResponse> response) {
                if (response.isSuccessful() && null != mProfileView) {
                    mProfileView.setBusyDays(response.body());
                } else if (null != mProfileView){
                    mProfileView.showError("Ошибка сервера");
                }
            }

            @Override
            public void onFailure(Call<GetCarCalendarResponse> call, Throwable t) {
                if (null != mProfileView) {
                    mProfileView.showError("Ошибка сервера");
                }
            }
        });
    }

    @Override
    public void sendBusyDays(String token, String carId, String dateStart, String timeStart, String dateEnd, String timeEnd) {
        Call<Object> addBusyDaysCall = mHttpEndpointApi.addBusyDays(token, carId, dateStart, timeStart, dateEnd, timeEnd);
        addBusyDaysCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful() && response.body().toString().contains("1.0")) {
                    mProfileView.updateCalendar();
                    mProfileView.showError("Готово");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                mProfileView.showError("Ошибка сервера");
            }
        });
    }

    @Override
    public void updateCarPrice(String token, String carId, final String type, String price) {
        Call<ResponseBody> updateCarPrice = mHttpEndpointApi.updateCarPrice(token, carId, type, price);
        updateCarPrice.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (null != mProfileView) {
                    mProfileView.updatePriceStatus(type);
                    mProfileView.showError("Цена обновлена");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (null != mProfileView) {
                    mProfileView.showError("Цена не обновлена");
                }
            }
        });
    }
}

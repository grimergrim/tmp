package ru.nadocars.messanger.ui.profile;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nadocars.messanger.api.HttpEndpointsApi;
import ru.nadocars.messanger.http.RetrofitFactory;
import ru.nadocars.messanger.json.car.GetCarsResponse;
import ru.nadocars.messanger.json.user.GetUserResponse;
import ru.nadocars.messanger.json.user.update.error102.UserUpdateError102;
import ru.nadocars.messanger.json.user.update.error103.UserUpdateError103;
import ru.nadocars.messanger.json.user.update.error104.UserUpdateError104;

public class ProfilePresenterImpl implements ProfilePresenter {

    private static ProfilePresenterImpl profilePresenter = new ProfilePresenterImpl();
    private ProfileView mProfileView;
    private HttpEndpointsApi mHttpEndpointApi;

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

    @Override
    public void getUserInfo(String token) {
        Call<GetUserResponse> userInfo = mHttpEndpointApi.getUserInfo(token);
        userInfo.enqueue(new Callback<GetUserResponse>() {
            @Override
            public void onResponse(Call<GetUserResponse> call, Response<GetUserResponse> response) {
                if (null != response && response.isSuccessful()) {
                    mProfileView.setProfileInfo(response.body());
                }
            }

            @Override
            public void onFailure(Call<GetUserResponse> call, Throwable t) {
                //TODO add error codes handle
                mProfileView.showError("Some error while getting profile info");
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
                    mProfileView.requestVerificationCode(email, phoneNumber, token,
                            userUpdateError104.getError().getErrorDetail().getCode(),
                            userUpdateError104.getError()
                            .getErrorDetail().getSessionId());
                } else if (response.body().toString().contains("103")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError103 userUpdateError103 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError103.class);
                    mProfileView.showError(userUpdateError103.getError().getErrorMsg());
                } else if (response.body().toString().contains("102")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError102 userUpdateError102 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError102.class);
                    mProfileView.showError(userUpdateError102.getError().getErrorMsg());
                } else if (response.body().toString().equals("1.0")) {
                    mProfileView.hideUpdateButton();
                    mProfileView.showError("Данные успешно обновлены");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                mProfileView.showError("Ошибка сервера");
            }
        });
    }

    @Override
    public void updateUserInfo(String email, String phoneNumber, String token, String sesionId, long code) {
        Call<Object> updateUserCall = mHttpEndpointApi.updateUser(email, phoneNumber, token, sesionId, code);
        updateUserCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.body().toString().contains("104")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError104 userUpdateError104 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError104.class);
                    mProfileView.showError(userUpdateError104.getError().getErrorMsg());
                } else if (response.body().toString().contains("103")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError103 userUpdateError103 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError103.class);
                    mProfileView.showError(userUpdateError103.getError().getErrorMsg());
                } else if (response.body().toString().contains("102")) {
                    JsonObject jsonObject = new GsonBuilder().create()
                            .toJsonTree(response.body()).getAsJsonObject();
                    UserUpdateError102 userUpdateError102 = new GsonBuilder().create()
                            .fromJson(jsonObject.toString(), UserUpdateError102.class);
                    mProfileView.showError(userUpdateError102.getError().getErrorMsg());
                } else if (response.body().toString().equals("1.0")) {
                    mProfileView.hideCodeLayout();
                    mProfileView.showError("Обновление успешно");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                mProfileView.showError("Ошибка сервера");
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
                    mProfileView.setCarsInfo(response.body());
                }
            }

            @Override
            public void onFailure(Call<GetCarsResponse> call, Throwable t) {
                mProfileView.showError("Ошибка соединения с сервером");
            }
        });
    }
}

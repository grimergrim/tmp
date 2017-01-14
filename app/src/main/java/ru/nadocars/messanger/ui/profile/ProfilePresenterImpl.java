package ru.nadocars.messanger.ui.profile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.nadocars.messanger.api.HttpEndpointsApi;
import ru.nadocars.messanger.http.RetrofitFactory;
import ru.nadocars.messanger.json.GetUserResponse;

public class ProfilePresenterImpl implements ProfilePresenter {

    private static ProfilePresenterImpl profilePresenter = new ProfilePresenterImpl();

    private ProfileView mProfileView;

    private ProfilePresenterImpl() {
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
        HttpEndpointsApi httpEndpointApi = RetrofitFactory.getHttpEndpointApi();
        Call<GetUserResponse> userInfo = httpEndpointApi.getUserInfo(token);
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
}

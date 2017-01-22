package ru.nadocars.messanger.ui.profile;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import ru.nadocars.messanger.ui.Presenter;

public interface ProfilePresenter extends Presenter<ProfileView> {

    void getUserInfo(String token);
    void updateUserInfo(String email, String phoneNumber, String token);
    void updateUserInfo(String email, String phoneNumber, String token, String ssesionId, long code);
    void getCars(String token);
    void uploadAvatar(String token, String uri, Context context, AppCompatActivity appCompatActivity);
    void uploadCarPhoto(String token, String carId, String uri);
    void deleteCarPhoto(String token, String carId, String photoId);
    void getCarCalendar(String carId);
    void sendBusyDays(String token, String carId, String dateStart, String timeStart, String dateEnd, String timeEnd);
    void updateCarPrice(String token, String carId, String type, String price);

}

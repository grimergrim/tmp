package ru.nadocars.messanger.ui.profile;

import ru.nadocars.messanger.ui.Presenter;

public interface ProfilePresenter extends Presenter<ProfileView> {

    void getUserInfo(String token);
    void updateUserInfo(String email, String phoneNumber, String token);
    void updateUserInfo(String email, String phoneNumber, String token, String ssesionId, long code);
    void getCars(String token);
    void uploadAvatar(String token, String uri);
    void uploadCarPhoto(String token, String uri);
    void deleteCarPhoto(String token, String carId, String photoId);
    void getCarCalendar(String carId);

}

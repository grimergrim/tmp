package ru.nadocars.messanger.ui.profile;

import android.content.ContentResolver;

import ru.nadocars.messanger.json.car.GetCarsResponse;
import ru.nadocars.messanger.json.car.calendar.GetCarCalendarResponse;
import ru.nadocars.messanger.json.user.GetUserResponse;

public interface ProfileView {

    void setProfileInfo(GetUserResponse profileInfo);
    void showError(String message);
    void requestVerificationCode(String email, String phoneNumber, String token, String ssesionId, long code);
    void hideUpdateButton();
    void hideCodeLayout();
    void setCarsInfo(GetCarsResponse carsInfo);
    void setBusyDays(GetCarCalendarResponse getCarCalendarResponse);
    void updateCalendar();
    ContentResolver getContentResolver();

}

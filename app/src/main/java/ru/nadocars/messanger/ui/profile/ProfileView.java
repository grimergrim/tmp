package ru.nadocars.messanger.ui.profile;

import ru.nadocars.messanger.json.car.calendar.GetCarCalendarResponse;
import ru.nadocars.messanger.json.user.GetUserResponse;

public interface ProfileView {

    void setProfileInfo(GetUserResponse profileInfo);
    void showError(String message);
    void requestVerificationCode(String email, String phoneNumber, String token, String ssesionId, long code);
    void hideUpdateButton();
    void hideCodeLayout();
    void setCarsInfo();
    void setBusyDays(GetCarCalendarResponse getCarCalendarResponse);
    void updateCalendar();
    void updatePriceStatus(String viewName);
    void setCarPhotoId(String photoId);
    void deletePhotoFromViewPager(String carId, String photoId);
    void restartActivity();

}

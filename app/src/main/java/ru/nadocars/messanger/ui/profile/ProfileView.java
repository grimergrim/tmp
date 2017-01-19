package ru.nadocars.messanger.ui.profile;

import ru.nadocars.messanger.json.user.GetUserResponse;

public interface ProfileView {

    void setProfileInfo(GetUserResponse profileInfo);
    void showError(String message);
    void requestVerificationCode(String email, String phoneNumber, String token, String ssesionId, long code);
    void hideUpdateButton();
    void hideCodeLayout();

}

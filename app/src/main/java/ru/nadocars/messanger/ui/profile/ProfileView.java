package ru.nadocars.messanger.ui.profile;

import ru.nadocars.messanger.json.GetUserResponse;

public interface ProfileView {

    void setProfileInfo(GetUserResponse profileInfo);
    void showError(String message);

}

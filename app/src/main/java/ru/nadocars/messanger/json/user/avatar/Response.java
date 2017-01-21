
package ru.nadocars.messanger.json.user.avatar;

import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("avatar")
    private String mAvatar;

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

}

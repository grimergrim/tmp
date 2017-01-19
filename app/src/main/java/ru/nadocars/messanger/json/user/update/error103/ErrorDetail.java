
package ru.nadocars.messanger.json.user.update.error103;

import com.google.gson.annotations.SerializedName;

public class ErrorDetail {

    @SerializedName("email")
    private String mEmail;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

}

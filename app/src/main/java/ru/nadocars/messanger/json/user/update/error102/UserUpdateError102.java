
package ru.nadocars.messanger.json.user.update.error102;

import com.google.gson.annotations.SerializedName;

public class UserUpdateError102 {

    @SerializedName("error")
    private Error mError;

    public Error getError() {
        return mError;
    }

    public void setError(Error error) {
        mError = error;
    }

}


package ru.nadocars.messanger.json.user.avatar;

import com.google.gson.annotations.SerializedName;

public class UploadAvatarResponse {

    @SerializedName("response")
    private Response mResponse;

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

}

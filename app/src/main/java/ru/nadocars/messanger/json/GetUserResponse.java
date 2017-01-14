package ru.nadocars.messanger.json;

import com.google.gson.annotations.SerializedName;

public class GetUserResponse {

    @SerializedName("response")
    private Response mResponse;

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }
}

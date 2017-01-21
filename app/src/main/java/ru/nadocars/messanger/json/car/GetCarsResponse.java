
package ru.nadocars.messanger.json.car;

import com.google.gson.annotations.SerializedName;

public class GetCarsResponse {

    @SerializedName("response")
    private Response mResponse;

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

}

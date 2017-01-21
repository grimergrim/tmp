
package ru.nadocars.messanger.json.car.calendar;

import com.google.gson.annotations.SerializedName;

public class GetCarCalendarResponse {

    @SerializedName("response")
    private Response mResponse;

    public Response getResponse() {
        return mResponse;
    }

    public void setResponse(Response response) {
        mResponse = response;
    }

}

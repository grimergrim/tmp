
package ru.nadocars.messanger.json.car.photo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UploadPhotoResponse {

    @SerializedName("response")
    private List<Response> mResponse;

    public List<Response> getResponse() {
        return mResponse;
    }

    public void setResponse(List<Response> response) {
        mResponse = response;
    }

}

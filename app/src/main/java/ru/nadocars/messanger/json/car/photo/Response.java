
package ru.nadocars.messanger.json.car.photo;

import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("id")
    private String mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("image_250")
    private String mImage250;
    @SerializedName("image_293x200")
    private String mImage293x200;
    @SerializedName("image_300x180")
    private String mImage300x180;
    @SerializedName("image_600x360")
    private String mImage600x360;
    @SerializedName("image_80x49")
    private String mImage80x49;
    @SerializedName("key")
    private String mKey;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("status")
    private String mStatus;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public String getImage250() {
        return mImage250;
    }

    public void setImage250(String image_250) {
        mImage250 = image_250;
    }

    public String getImage293x200() {
        return mImage293x200;
    }

    public void setImage293x200(String image_293x200) {
        mImage293x200 = image_293x200;
    }

    public String getImage300x180() {
        return mImage300x180;
    }

    public void setImage300x180(String image_300x180) {
        mImage300x180 = image_300x180;
    }

    public String getImage600x360() {
        return mImage600x360;
    }

    public void setImage600x360(String image_600x360) {
        mImage600x360 = image_600x360;
    }

    public String getImage80x49() {
        return mImage80x49;
    }

    public void setImage80x49(String image_80x49) {
        mImage80x49 = image_80x49;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}

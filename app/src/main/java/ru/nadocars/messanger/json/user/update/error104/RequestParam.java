
package ru.nadocars.messanger.json.user.update.error104;

import com.google.gson.annotations.SerializedName;

public class RequestParam {

    @SerializedName("key")
    private String mKey;
    @SerializedName("value")
    private String mValue;

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

}

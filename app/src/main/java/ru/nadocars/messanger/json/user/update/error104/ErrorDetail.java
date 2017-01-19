
package ru.nadocars.messanger.json.user.update.error104;

import com.google.gson.annotations.SerializedName;

public class ErrorDetail {

    @SerializedName("code")
    private String mCode;
    @SerializedName("session_id")
    private Long mSessionId;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public Long getSessionId() {
        return mSessionId;
    }

    public void setSessionId(Long session_id) {
        mSessionId = session_id;
    }

}

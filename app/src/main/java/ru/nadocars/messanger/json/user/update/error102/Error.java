
package ru.nadocars.messanger.json.user.update.error102;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Error {

    @SerializedName("error_code")
    private Long mErrorCode;
    @SerializedName("error_detail")
    private Object mErrorDetail;
    @SerializedName("error_msg")
    private String mErrorMsg;
    @SerializedName("request_params")
    private List<RequestParam> mRequestParams;

    public Long getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(Long error_code) {
        mErrorCode = error_code;
    }

    public Object getErrorDetail() {
        return mErrorDetail;
    }

    public void setErrorDetail(Object error_detail) {
        mErrorDetail = error_detail;
    }

    public String getErrorMsg() {
        return mErrorMsg;
    }

    public void setErrorMsg(String error_msg) {
        mErrorMsg = error_msg;
    }

    public List<RequestParam> getRequestParams() {
        return mRequestParams;
    }

    public void setRequestParams(List<RequestParam> request_params) {
        mRequestParams = request_params;
    }

}


package ru.nadocars.messanger.json.car;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Item {

    @SerializedName("day_price")
    private Long mDayPrice;
    @SerializedName("id")
    private String mId;
    @SerializedName("mark")
    private String mMark;
    @SerializedName("model")
    private String mModel;
    @SerializedName("month_price")
    private Long mMonthPrice;
    @SerializedName("photos")
    private List<Photo> mPhotos;
    @SerializedName("rent_enabled")
    private Long mRentEnabled;
    @SerializedName("url")
    private String mUrl;
    @SerializedName("week_price")
    private Long mWeekPrice;
    @SerializedName("year")
    private String mYear;

    public Long getDayPrice() {
        return mDayPrice;
    }

    public void setDayPrice(Long day_price) {
        mDayPrice = day_price;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getMark() {
        return mMark;
    }

    public void setMark(String mark) {
        mMark = mark;
    }

    public String getModel() {
        return mModel;
    }

    public void setModel(String model) {
        mModel = model;
    }

    public Long getMonthPrice() {
        return mMonthPrice;
    }

    public void setMonthPrice(Long month_price) {
        mMonthPrice = month_price;
    }

    public List<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(List<Photo> photos) {
        mPhotos = photos;
    }

    public Long getRentEnabled() {
        return mRentEnabled;
    }

    public void setRentEnabled(Long rent_enabled) {
        mRentEnabled = rent_enabled;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public Long getWeekPrice() {
        return mWeekPrice;
    }

    public void setWeekPrice(Long week_price) {
        mWeekPrice = week_price;
    }

    public String getYear() {
        return mYear;
    }

    public void setYear(String year) {
        mYear = year;
    }

}

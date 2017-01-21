
package ru.nadocars.messanger.json.car.calendar;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("busy_from")
    private String mBusyFrom;
    @SerializedName("busy_to")
    private String mBusyTo;
    @SerializedName("owner_busy")
    private Long mOwnerBusy;

    public String getBusyFrom() {
        return mBusyFrom;
    }

    public void setBusyFrom(String busy_from) {
        mBusyFrom = busy_from;
    }

    public String getBusyTo() {
        return mBusyTo;
    }

    public void setBusyTo(String busy_to) {
        mBusyTo = busy_to;
    }

    public Long getOwnerBusy() {
        return mOwnerBusy;
    }

    public void setOwnerBusy(Long owner_busy) {
        mOwnerBusy = owner_busy;
    }

}

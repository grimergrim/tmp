
package ru.nadocars.messanger.json.car.calendar;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("items")
    private List<Item> mItems;

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        mItems = items;
    }

}

package ru.nadocars.messanger.json;

import com.google.gson.annotations.SerializedName;

public class Response {

    @SerializedName("user_id")
    private String userId;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("middle_name")
    private String middleName;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("is_owner")
    private int isOwner;
    @SerializedName("is_renter")
    private int isRenter;
    @SerializedName("balance")
    private int balance;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(int isOwner) {
        this.isOwner = isOwner;
    }

    public int getIsRenter() {
        return isRenter;
    }

    public void setIsRenter(int isRenter) {
        this.isRenter = isRenter;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

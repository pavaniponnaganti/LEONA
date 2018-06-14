package com.leona.models;

/**
 * Created by krify on 15/6/17.
 */

public class NotifyModel {

    public String getUserNotiID() {
        return userNotiID;
    }

    public void setUserNotiID(String userNotiID) {
        this.userNotiID = userNotiID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDiscountID() {
        return discountID;
    }

    public void setDiscountID(String discountID) {
        this.discountID = discountID;
    }

    public String getDiscountDateTime() {
        return discountDateTime;
    }

    public void setDiscountDateTime(String discountDateTime) {
        this.discountDateTime = discountDateTime;
    }

    public String getCreatDatetime() {
        return creatDatetime;
    }

    public void setCreatDatetime(String creatDatetime) {
        this.creatDatetime = creatDatetime;
    }

    String userNotiID;
    String message;
    String discountID;
    String discountDateTime;
    String creatDatetime;


}

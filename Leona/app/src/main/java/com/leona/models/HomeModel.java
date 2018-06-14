package com.leona.models;


public class HomeModel {

    private String discountID;
    private String discountName;
    private String image;
    private String originalPrice;
    private String discountPrice;
    private String startTimeDate;
    private String endTimeDate;
    private String description;
    private String featured;
    private String timeDateStamp;
    private String merchantID;

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    private String storeName;

    public String getAdID() {
        return adID;
    }

    public void setAdID(String adID) {
        this.adID = adID;
    }

    private String adID;



    public String getDiscountID() {
        return discountID;
    }

    public void setDiscountID(String discountID) {
        this.discountID = discountID;
    }

    public String getDiscountName() {
        return discountName;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getStartTimeDate() {
        return startTimeDate;
    }

    public void setStartTimeDate(String startTimeDate) {
        this.startTimeDate = startTimeDate;
    }

    public String getEndTimeDate() {
        return endTimeDate;
    }

    public void setEndTimeDate(String endTimeDate) {
        this.endTimeDate = endTimeDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String discription) {
        this.description = description;
    }

    public String getFeatured() {
        return featured;
    }

    public void setFeatured(String featured) {
        this.featured = featured;
    }

    public String getTimeDateStamp() {
        return timeDateStamp;
    }

    public void setTimeDateStamp(String timeDateStamp) {
        this.timeDateStamp = timeDateStamp;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }
}

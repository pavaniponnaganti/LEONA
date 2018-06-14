package com.leona.models;

/**
 * Created by krify on 9/5/17.
 */

public class NearbyModel {

    private String merchantID;
    private String storeName;
    private String storeImage;
    private String description;

    private String latitude;
    private String logitude;
    private String distance;

    private String offersCount;

    public String getBranchsCount() {
        return branchsCount;
    }

    public void setBranchsCount(String branchsCount) {
        this.branchsCount = branchsCount;
    }

    private String branchsCount;

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public void setStoreImage(String storeImage) {
        this.storeImage = storeImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLogitude() {
        return logitude;
    }

    public void setLogitude(String logitude) {
        this.logitude = logitude;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getOffersCount() {
        return offersCount;
    }

    public void setOffersCount(String offersCount) {
        this.offersCount = offersCount;
    }
}

package com.erxprescriptionuser.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProviderData {
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("avgRating")
    @Expose
    private Integer avgRating;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("fullName")
    @Expose
    private String fullName;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Integer getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Integer avgRating) {
        this.avgRating = avgRating;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }


}

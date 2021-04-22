package com.erxprescriptionuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class SignupModel implements Parcelable {

    private String fullName;
    private String email;
    private String countryCode;
    private String mobileNumber;
    private String address;
    private String password;
    private String deviceType;
    private String deviceToken;
    private Double latitude;
    private Double longitude;
    private String emiratesId;
    private String dob;
    private String gender;
    private String insuranceCardPicture;
    private String emiratesIdPicture;
    private String additionalMobileNo;
    private String userId;
    private String otp;

    public SignupModel()
    {

    }


    protected SignupModel(Parcel in) {
        fullName = in.readString();
        email = in.readString();
        countryCode = in.readString();
        mobileNumber = in.readString();
        address = in.readString();
        password = in.readString();
        deviceType = in.readString();
        deviceToken = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        emiratesId = in.readString();
        dob = in.readString();
        gender = in.readString();
        insuranceCardPicture = in.readString();
        emiratesIdPicture = in.readString();
        additionalMobileNo = in.readString();
        userId = in.readString();
        otp = in.readString();
    }

    public static final Creator<SignupModel> CREATOR = new Creator<SignupModel>() {
        @Override
        public SignupModel createFromParcel(Parcel in) {
            return new SignupModel(in);
        }

        @Override
        public SignupModel[] newArray(int size) {
            return new SignupModel[size];
        }
    };

    public String getAdditionalMobileNo() {
        return additionalMobileNo;
    }

    public void setAdditionalMobileNo(String additionalMobileNo) {
        this.additionalMobileNo = additionalMobileNo;
    }



    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEmiratesId() {
        return emiratesId;
    }

    public void setEmiratesId(String emiratesId) {
        this.emiratesId = emiratesId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getInsuranceCardPicture() {
        return insuranceCardPicture;
    }

    public void setInsuranceCardPicture(String insuranceCardPicture) {
        this.insuranceCardPicture = insuranceCardPicture;
    }

    public String getEmiratesIdPicture() {
        return emiratesIdPicture;
    }

    public void setEmiratesIdPicture(String emiratesIdPicture) {
        this.emiratesIdPicture = emiratesIdPicture;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(countryCode);
        dest.writeString(mobileNumber);
        dest.writeString(address);
        dest.writeString(password);
        dest.writeString(deviceType);
        dest.writeString(deviceToken);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(emiratesId);
        dest.writeString(dob);
        dest.writeString(gender);
        dest.writeString(insuranceCardPicture);
        dest.writeString(emiratesIdPicture);
        dest.writeString(additionalMobileNo);
        dest.writeString(userId);
        dest.writeString(otp);
    }
}

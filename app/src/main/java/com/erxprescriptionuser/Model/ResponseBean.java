package com.erxprescriptionuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

 public class ResponseBean implements Parcelable {

     @SerializedName("insuranceType")
     @Expose
     private String insuranceType;

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("location")
    @Expose
    private Location location;





    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("userType")
    @Expose
    private String userType;
    @SerializedName("profilePic")
    @Expose
    private String profilePic;
    @SerializedName("notificationStatus")
    @Expose
    private Boolean notificationStatus;
    @SerializedName("totalPrescription")
    @Expose
    private Integer totalPrescription;
    @SerializedName("totalCancelPrescription")
    @Expose
    private Integer totalCancelPrescription;
    @SerializedName(value = "fullName",alternate = "username")
    @Expose
    private String fullName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("countryCode")
    @Expose
    private String countryCode;
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    @SerializedName("deviceToken")
    @Expose
    private String deviceToken;
    @SerializedName("latitude")
    @Expose
    private Double latitude;
    @SerializedName("longitude")
    @Expose
    private Double longitude;
    @SerializedName("emiratesId")
    @Expose
    private String emiratesId;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("insuranceCardPicture")
    @Expose
    private String insuranceCardPicture;
    @SerializedName("emiratesIdPicture")
    @Expose
    private String emiratesIdPicture;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("__v")
    @Expose
    private Integer v;
    @SerializedName("jwtToken")
    @Expose
    private String jwtToken;
    @SerializedName(value = "title",alternate = "notiTitle")
    @Expose
    private String title;

    @SerializedName(value = "body",alternate = "notiMessage")
    @Expose
    private String body;

    @SerializedName("isSeen")
    @Expose
    private boolean isSeen;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("providerId")
    @Expose
    private String providerId;

    @SerializedName("prescriptionNumber")
    @Expose
    private String prescriptionNumber;

    @SerializedName("amount")
    @Expose
    private String amount;

    @SerializedName("currency")
    @Expose
    private String currency;

    @SerializedName("providerData")
    @Expose
    private ProviderData providerData;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("roomId")
    @Expose
    private String roomId;

     @SerializedName("senderId")
     @Expose
     private String senderId;

     @SerializedName("receiverId")
     @Expose
     private String receiverId;

     @SerializedName("providerAmount")
     @Expose
     String providerAmount;

     @SerializedName("adminAmount")
     @Expose
     String adminAmount;

     @SerializedName("finalAmount")
     @Expose
     String finalAmount;

     @SerializedName("deliveryToAdminAmount")
     @Expose
     private String deliveryToAdminAmount;

     @SerializedName("companyAmount")
     @Expose
     private String companyAmount;

     @SerializedName("chargeData")
     @Expose
     private ChargeResponse chargeData;

     @SerializedName("prescriptionData")
     @Expose
     private PrescriptionData prescriptionData;

     @SerializedName("paymentId")
     @Expose
     private String paymentId;

     @SerializedName("paymentType")
     @Expose
     private String paymentType;

     @SerializedName("date")
     @Expose
     private String date;

     @SerializedName("otp")
     @Expose
     private String otp;

     @SerializedName("description")
     @Expose
     private String description;

     public String getInsuranceType() {
         return insuranceType;
     }

     public void setInsuranceType(String insuranceType) {
         this.insuranceType = insuranceType;
     }

     public String getDescription() {
         return description;
     }

     public void setDescription(String description) {
         this.description = description;
     }

     public String getOtp() {
         return otp;
     }

     public void setOtp(String otp) {
         this.otp = otp;
     }

     protected ResponseBean(Parcel in) {
         insuranceType = in.readString();
         id = in.readString();
         status = in.readString();
         userType = in.readString();
         profilePic = in.readString();
         byte tmpNotificationStatus = in.readByte();
         notificationStatus = tmpNotificationStatus == 0 ? null : tmpNotificationStatus == 1;
         if (in.readByte() == 0) {
             totalPrescription = null;
         } else {
             totalPrescription = in.readInt();
         }
         if (in.readByte() == 0) {
             totalCancelPrescription = null;
         } else {
             totalCancelPrescription = in.readInt();
         }
         fullName = in.readString();
         email = in.readString();
         countryCode = in.readString();
         mobileNumber = in.readString();
         address = in.readString();
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
         gender = in.readString();
         dob = in.readString();
         insuranceCardPicture = in.readString();
         emiratesIdPicture = in.readString();
         createdAt = in.readString();
         updatedAt = in.readString();
         if (in.readByte() == 0) {
             v = null;
         } else {
             v = in.readInt();
         }
         jwtToken = in.readString();
         title = in.readString();
         body = in.readString();
         isSeen = in.readByte() != 0;
         userId = in.readString();
         providerId = in.readString();
         prescriptionNumber = in.readString();
         amount = in.readString();
         currency = in.readString();
         message = in.readString();
         roomId = in.readString();
         senderId = in.readString();
         receiverId = in.readString();
         providerAmount = in.readString();
         adminAmount = in.readString();
         finalAmount = in.readString();
         deliveryToAdminAmount  = in.readString();
         companyAmount  = in.readString();
         chargeData = in.readParcelable(ChargeResponse.class.getClassLoader());
         prescriptionData = in.readParcelable(PrescriptionData.class.getClassLoader());
         paymentId=in.readString();
         paymentType=in.readString();
         date=in.readString();
         otp=in.readString();
         description=in.readString();
     }

     public static final Creator<ResponseBean> CREATOR = new Creator<ResponseBean>() {
         @Override
         public ResponseBean createFromParcel(Parcel in) {
             return new ResponseBean(in);
         }

         @Override
         public ResponseBean[] newArray(int size) {
             return new ResponseBean[size];
         }
     };

     public String getProviderAmount() {
         return providerAmount;
     }

     public void setProviderAmount(String providerAmount) {
         this.providerAmount = providerAmount;
     }

     public String getAdminAmount() {
         return adminAmount;
     }

     public void setAdminAmount(String adminAmount) {
         this.adminAmount = adminAmount;
     }

     public String getFinalAmount() {
         return finalAmount;
     }

     public void setFinalAmount(String finalAmount) {
         this.finalAmount = finalAmount;
     }

     public ChargeResponse getChargeData() {
         return chargeData;
     }

     public void setChargeData(ChargeResponse chargeData) {
         this.chargeData = chargeData;
     }

     public String getRoomId() {
         return roomId;
     }

     public void setRoomId(String roomId) {
         this.roomId = roomId;
     }

     public String getSenderId() {
         return senderId;
     }

     public void setSenderId(String senderId) {
         this.senderId = senderId;
     }

     public String getReceiverId() {
         return receiverId;
     }

     public void setReceiverId(String receiverId) {
         this.receiverId = receiverId;
     }

     public String getMessage() {
         return message;
     }

     public void setMessage(String message) {
         this.message = message;
     }

     public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public Boolean getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(Boolean notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public Integer getTotalPrescription() {
        return totalPrescription;
    }

    public void setTotalPrescription(Integer totalPrescription) {
        this.totalPrescription = totalPrescription;
    }

    public Integer getTotalCancelPrescription() {
        return totalCancelPrescription;
    }

    public void setTotalCancelPrescription(Integer totalCancelPrescription) {
        this.totalCancelPrescription = totalCancelPrescription;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

     public String getUserId() {
         return userId;
     }

     public void setUserId(String userId) {
         this.userId = userId;
     }

     public String getProviderId() {
         return providerId;
     }

     public void setProviderId(String providerId) {
         this.providerId = providerId;
     }

     public String getPrescriptionNumber() {
         return prescriptionNumber;
     }

     public void setPrescriptionNumber(String prescriptionNumber) {
         this.prescriptionNumber = prescriptionNumber;
     }

     public String getAmount() {
         return amount;
     }

     public void setAmount(String amount) {
         this.amount = amount;
     }

     public String getCurrency() {
         return currency;
     }

     public void setCurrency(String currency) {
         this.currency = currency;
     }

     public String getDeliveryToAdminAmount() {
         return deliveryToAdminAmount;
     }

     public void setDeliveryToAdminAmount(String deliveryToAdminAmount) {
         this.deliveryToAdminAmount = deliveryToAdminAmount;
     }

     public String getCompanyAmount() {
         return companyAmount;
     }

     public void setCompanyAmount(String companyAmount) {
         this.companyAmount = companyAmount;
     }

     public ProviderData getProviderData() {
         return providerData;
     }

     public void setProviderData(ProviderData providerData) {
         this.providerData = providerData;
     }

     public PrescriptionData getPrescriptionData() {
         return prescriptionData;
     }

     public void setPrescriptionData(PrescriptionData prescriptionData) {
         this.prescriptionData = prescriptionData;
     }

     public String getPaymentId() {
         return paymentId;
     }

     public void setPaymentId(String paymentId) {
         this.paymentId = paymentId;
     }

     public String getPaymentType() {
         return paymentType;
     }

     public void setPaymentType(String paymentType) {
         this.paymentType = paymentType;
     }

     public String getDate() {
         return date;
     }

     public void setDate(String date) {
         this.date = date;
     }

     @Override
     public int describeContents() {
         return 0;
     }

     @Override
     public void writeToParcel(Parcel dest, int flags) {
         dest.writeString(insuranceType);
         dest.writeString(id);
         dest.writeString(status);
         dest.writeString(userType);
         dest.writeString(profilePic);
         dest.writeByte((byte) (notificationStatus == null ? 0 : notificationStatus ? 1 : 2));
         if (totalPrescription == null) {
             dest.writeByte((byte) 0);
         } else {
             dest.writeByte((byte) 1);
             dest.writeInt(totalPrescription);
         }
         if (totalCancelPrescription == null) {
             dest.writeByte((byte) 0);
         } else {
             dest.writeByte((byte) 1);
             dest.writeInt(totalCancelPrescription);
         }
         dest.writeString(fullName);
         dest.writeString(email);
         dest.writeString(countryCode);
         dest.writeString(mobileNumber);
         dest.writeString(address);
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
         dest.writeString(gender);
         dest.writeString(dob);
         dest.writeString(insuranceCardPicture);
         dest.writeString(emiratesIdPicture);
         dest.writeString(createdAt);
         dest.writeString(updatedAt);
         if (v == null) {
             dest.writeByte((byte) 0);
         } else {
             dest.writeByte((byte) 1);
             dest.writeInt(v);
         }
         dest.writeString(jwtToken);
         dest.writeString(title);
         dest.writeString(body);
         dest.writeByte((byte) (isSeen ? 1 : 0));
         dest.writeString(userId);
         dest.writeString(providerId);
         dest.writeString(prescriptionNumber);
         dest.writeString(amount);
         dest.writeString(currency);
         dest.writeString(message);
         dest.writeString(roomId);
         dest.writeString(senderId);
         dest.writeString(receiverId);
         dest.writeString(providerAmount);
         dest.writeString(adminAmount);
         dest.writeString(finalAmount);
         dest.writeString(deliveryToAdminAmount);
         dest.writeString(companyAmount);
         dest.writeParcelable(chargeData, flags);
         dest.writeParcelable(prescriptionData, flags);
         dest.writeString(paymentId);
         dest.writeString(paymentType);
         dest.writeString(date);
         dest.writeString(otp);
         dest.writeString(description);
     }
 }

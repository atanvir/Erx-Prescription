package com.erxprescriptionuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PrescriptionModelObject implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private PrescriptionModelObject.Data data = null;


    protected PrescriptionModelObject(Parcel in) {
        status = in.readString();
        message = in.readString();
        data = in.readParcelable(PrescriptionModelObject.Data.class.getClassLoader());
    }

    public static final Creator<PrescriptionModelObject> CREATOR = new Creator<PrescriptionModelObject>() {
        @Override
        public PrescriptionModelObject createFromParcel(Parcel in) {
            return new PrescriptionModelObject(in);
        }

        @Override
        public PrescriptionModelObject[] newArray(int size) {
            return new PrescriptionModelObject[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public PrescriptionModelObject.Data getData() {
        return data;
    }

    public void setData(PrescriptionModelObject.Data data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(message);
        dest.writeParcelable(data, flags);
    }


    public static class Data implements Parcelable{
        @SerializedName("location")
        @Expose
        private Location location;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("prescriptionStatus")
        @Expose
        private String prescriptionStatus;
        @SerializedName("cancelStatus")
        @Expose
        private Boolean cancelStatus;
        @SerializedName("deleteStatus")
        @Expose
        private Boolean deleteStatus;
        @SerializedName("paymentType")
        @Expose
        private String paymentType;
        @SerializedName("assignDriverStatus")
        @Expose
        private Boolean assignDriverStatus;
        @SerializedName("driverAssignRequest")
        @Expose
        private Boolean driverAssignRequest;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("prescriptionNumber")
        @Expose
        private String prescriptionNumber;
        @SerializedName("prescriptionImage")
        @Expose
        private String prescriptionImage;
        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("year")
        @Expose
        private Integer year;
        @SerializedName("month")
        @Expose
        private Integer month;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;
        @SerializedName("__v")
        @Expose
        private Integer v;
        @SerializedName("deliveryRoomId")
        @Expose
        private String deliveryRoomId;
        @SerializedName("deliveryType")
        @Expose
        private String deliveryType;
        @SerializedName("invoice")
        @Expose
        private String invoice;
        @SerializedName("offerId")
        @Expose
        private String offerId;
        @SerializedName("providerId")
        @Expose
        private PrescriptionModelList.ProviderId providerId;
        @SerializedName("providerRoomId")
        @Expose
        private String providerRoomId;
        @SerializedName("companyId")
        @Expose
        private String companyId;
        @SerializedName("deliveryId")
        @Expose
        private DeliveryId deliveryId;

        protected Data(Parcel in) {
            status = in.readString();
            prescriptionStatus = in.readString();
            byte tmpCancelStatus = in.readByte();
            cancelStatus = tmpCancelStatus == 0 ? null : tmpCancelStatus == 1;
            byte tmpDeleteStatus = in.readByte();
            deleteStatus = tmpDeleteStatus == 0 ? null : tmpDeleteStatus == 1;
            paymentType = in.readString();
            byte tmpAssignDriverStatus = in.readByte();
            assignDriverStatus = tmpAssignDriverStatus == 0 ? null : tmpAssignDriverStatus == 1;
            byte tmpDriverAssignRequest = in.readByte();
            driverAssignRequest = tmpDriverAssignRequest == 0 ? null : tmpDriverAssignRequest == 1;
            id = in.readString();
            userId = in.readString();
            prescriptionNumber = in.readString();
            prescriptionImage = in.readString();
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
            address = in.readString();
            if (in.readByte() == 0) {
                year = null;
            } else {
                year = in.readInt();
            }
            if (in.readByte() == 0) {
                month = null;
            } else {
                month = in.readInt();
            }
            createdAt = in.readString();
            updatedAt = in.readString();
            if (in.readByte() == 0) {
                v = null;
            } else {
                v = in.readInt();
            }
            deliveryRoomId = in.readString();
            deliveryType = in.readString();
            invoice = in.readString();
            offerId = in.readString();
            providerId = in.readParcelable(PrescriptionModelList.ProviderId.class.getClassLoader());
            providerRoomId = in.readString();
            companyId = in.readString();
            deliveryId = in.readParcelable(PrescriptionModelList.DeliveryId.class.getClassLoader());
        }

        public static final Creator<PrescriptionModelList.Data> CREATOR = new Creator<PrescriptionModelList.Data>() {
            @Override
            public PrescriptionModelList.Data createFromParcel(Parcel in) {
                return new PrescriptionModelList.Data(in);
            }

            @Override
            public PrescriptionModelList.Data[] newArray(int size) {
                return new PrescriptionModelList.Data[size];
            }
        };

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

        public String getPrescriptionStatus() {
            return prescriptionStatus;
        }

        public void setPrescriptionStatus(String prescriptionStatus) {
            this.prescriptionStatus = prescriptionStatus;
        }

        public Boolean getCancelStatus() {
            return cancelStatus;
        }

        public void setCancelStatus(Boolean cancelStatus) {
            this.cancelStatus = cancelStatus;
        }

        public Boolean getDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(Boolean deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public Boolean getAssignDriverStatus() {
            return assignDriverStatus;
        }

        public void setAssignDriverStatus(Boolean assignDriverStatus) {
            this.assignDriverStatus = assignDriverStatus;
        }

        public Boolean getDriverAssignRequest() {
            return driverAssignRequest;
        }

        public void setDriverAssignRequest(Boolean driverAssignRequest) {
            this.driverAssignRequest = driverAssignRequest;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPrescriptionNumber() {
            return prescriptionNumber;
        }

        public void setPrescriptionNumber(String prescriptionNumber) {
            this.prescriptionNumber = prescriptionNumber;
        }

        public String getPrescriptionImage() {
            return prescriptionImage;
        }

        public void setPrescriptionImage(String prescriptionImage) {
            this.prescriptionImage = prescriptionImage;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Integer getYear() {
            return year;
        }

        public void setYear(Integer year) {
            this.year = year;
        }

        public Integer getMonth() {
            return month;
        }

        public void setMonth(Integer month) {
            this.month = month;
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

        public String getDeliveryRoomId() {
            return deliveryRoomId;
        }

        public void setDeliveryRoomId(String deliveryRoomId) {
            this.deliveryRoomId = deliveryRoomId;
        }

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getOfferId() {
            return offerId;
        }

        public void setOfferId(String offerId) {
            this.offerId = offerId;
        }

        public PrescriptionModelList.ProviderId getProviderId() {
            return providerId;
        }

        public void setProviderId(PrescriptionModelList.ProviderId providerId) {
            this.providerId = providerId;
        }

        public String getProviderRoomId() {
            return providerRoomId;
        }

        public void setProviderRoomId(String providerRoomId) {
            this.providerRoomId = providerRoomId;
        }

        public String getCompanyId() {
            return companyId;
        }

        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }

        public DeliveryId getDeliveryId() {
            return deliveryId;
        }

        public void setDeliveryId(DeliveryId deliveryId) {
            this.deliveryId = deliveryId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(status);
            dest.writeString(prescriptionStatus);
            dest.writeByte((byte) (cancelStatus == null ? 0 : cancelStatus ? 1 : 2));
            dest.writeByte((byte) (deleteStatus == null ? 0 : deleteStatus ? 1 : 2));
            dest.writeString(paymentType);
            dest.writeByte((byte) (assignDriverStatus == null ? 0 : assignDriverStatus ? 1 : 2));
            dest.writeByte((byte) (driverAssignRequest == null ? 0 : driverAssignRequest ? 1 : 2));
            dest.writeString(id);
            dest.writeString(userId);
            dest.writeString(prescriptionNumber);
            dest.writeString(prescriptionImage);
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
            dest.writeString(address);
            if (year == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(year);
            }
            if (month == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(month);
            }
            dest.writeString(createdAt);
            dest.writeString(updatedAt);
            if (v == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(v);
            }
            dest.writeString(deliveryRoomId);
            dest.writeString(deliveryType);
            dest.writeString(invoice);
            dest.writeString(offerId);
            dest.writeParcelable(providerId, flags);
            dest.writeString(providerRoomId);
            dest.writeString(companyId);
            dest.writeParcelable(deliveryId,flags);
        }
    }


    public static class DeliveryId implements Parcelable {

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("deviceToken")
        @Expose
        private String deviceToken;

        protected DeliveryId(Parcel in) {
            id = in.readString();
            deviceToken = in.readString();
        }

        public static final Creator<PrescriptionModelList.DeliveryId> CREATOR = new Creator<PrescriptionModelList.DeliveryId>() {
            @Override
            public PrescriptionModelList.DeliveryId createFromParcel(Parcel in) {
                return new PrescriptionModelList.DeliveryId(in);
            }

            @Override
            public PrescriptionModelList.DeliveryId[] newArray(int size) {
                return new PrescriptionModelList.DeliveryId[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(deviceToken);
        }
    }

    public static class ProviderId implements Parcelable{

        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("profilePic")
        @Expose
        private String profilePic;
        @SerializedName("avgRating")
        @Expose
        private String avgRating;

        @SerializedName("countryCode")
        @Expose
        private String countryCode;

        @SerializedName("fullName")
        @Expose
        private String fullName;

        @SerializedName("mobileNumber")
        @Expose
        private String mobileNumber;

        protected ProviderId(Parcel in) {
            id = in.readString();
            profilePic = in.readString();
            avgRating = in.readString();
            countryCode = in.readString();
            fullName = in.readString();
            mobileNumber = in.readString();
        }

        public static final Creator<PrescriptionModelList.ProviderId> CREATOR = new Creator<PrescriptionModelList.ProviderId>() {
            @Override
            public PrescriptionModelList.ProviderId createFromParcel(Parcel in) {
                return new PrescriptionModelList.ProviderId(in);
            }

            @Override
            public PrescriptionModelList.ProviderId[] newArray(int size) {
                return new PrescriptionModelList.ProviderId[size];
            }
        };

        public String getProfilePic() {
            return profilePic;
        }

        public void setProfilePic(String profilePic) {
            this.profilePic = profilePic;
        }

        public String getAvgRating() {
            return avgRating;
        }

        public void setAvgRating(String avgRating) {
            this.avgRating = avgRating;
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

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(profilePic);
            dest.writeString(avgRating);
            dest.writeString(countryCode);
            dest.writeString(fullName);
            dest.writeString(mobileNumber);
        }
    }

}

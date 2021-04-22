package com.erxprescriptionuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommonModel implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private ResponseBean data;

    protected CommonModel(Parcel in) {
        status = in.readString();
        message = in.readString();
        data = in.readParcelable(ResponseBean.class.getClassLoader());
    }

    public static final Creator<CommonModel> CREATOR = new Creator<CommonModel>() {
        @Override
        public CommonModel createFromParcel(Parcel in) {
            return new CommonModel(in);
        }

        @Override
        public CommonModel[] newArray(int size) {
            return new CommonModel[size];
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

    public ResponseBean getData() {
        return data;
    }

    public void setData(ResponseBean data) {
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
}

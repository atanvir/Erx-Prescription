package com.erxprescriptionuser.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrescriptionData implements Parcelable {
    @SerializedName("prescriptionNumber")
    @Expose
    private String prescriptionNumber;

    protected PrescriptionData(Parcel in) {
        prescriptionNumber = in.readString();
    }

    public static final Creator<PrescriptionData> CREATOR = new Creator<PrescriptionData>() {
        @Override
        public PrescriptionData createFromParcel(Parcel in) {
            return new PrescriptionData(in);
        }

        @Override
        public PrescriptionData[] newArray(int size) {
            return new PrescriptionData[size];
        }
    };

    public String getPrescriptionNumber() {
        return prescriptionNumber;
    }

    public void setPrescriptionNumber(String prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(prescriptionNumber);
    }
}

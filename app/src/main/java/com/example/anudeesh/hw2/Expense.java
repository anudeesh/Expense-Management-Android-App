package com.example.anudeesh.hw2;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Anudeesh on 9/13/2016.
 */
public class Expense implements Parcelable, Comparable<Expense> {

    String expName, expCategory, expAmount, expDate;
    Uri billImage;

    public Expense(String expName, String expCategory, String expAmount, String expDate, Uri billImage) {
        this.expName = expName;
        this.expCategory = expCategory;
        this.expAmount = expAmount;
        this.expDate = expDate;
        this.billImage = billImage;
    }

    protected Expense(Parcel in) {
        this.expName = in.readString();
        this.expCategory = in.readString();
        this.expAmount = in.readString();
        this.expDate = in.readString();
        this.billImage = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Expense> CREATOR = new Creator<Expense>() {
        @Override
        public Expense createFromParcel(Parcel in) {
            return new Expense(in);
        }

        @Override
        public Expense[] newArray(int size) {
            return new Expense[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expName);
        dest.writeString(expCategory);
        dest.writeString(expAmount);
        dest.writeString(expDate);
        dest.writeParcelable(billImage,flags);
    }

    @Override
    public int compareTo(Expense another) {
        if(this.expName.compareToIgnoreCase(another.expName) >0)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getExpCategory() {
        return expCategory;
    }

    public void setExpCategory(String expCategory) {
        this.expCategory = expCategory;
    }

    public String getExpAmount() {
        return expAmount;
    }

    public void setExpAmount(String expAmount) {
        this.expAmount = expAmount;
    }

    public String getExpDate() {
        return expDate;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "expName='" + expName + '\'' +
                ", expCategory='" + expCategory + '\'' +
                ", expAmount='" + expAmount + '\'' +
                ", expDate='" + expDate + '\'' +
                ", billImage=" + billImage +
                '}';
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Uri getBillImage() {
        return billImage;
    }

    public void setBillImage(Uri billImage) {
        this.billImage = billImage;
    }
}

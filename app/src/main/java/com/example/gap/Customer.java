package com.example.gap;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {
    private int customerId;
    private String username;
    private String password;
    private String fullName;
    private String phone;
    private String email;

    public Customer(int customerId, String username, String password, String fullName, String phone, String email) {
        this.customerId = customerId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }

    protected Customer(Parcel in) {
        customerId = in.readInt();
        username = in.readString();
        password = in.readString();
        fullName = in.readString();
        phone = in.readString();
        email = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public int getCustomerId() {
        return customerId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(customerId);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(fullName);
        dest.writeString(phone);
        dest.writeString(email);
    }
}
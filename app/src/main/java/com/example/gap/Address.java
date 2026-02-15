package com.example.gap;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private int addressId;
    private String label;
    private String houseNo;
    private String street;
    private String area;
    private String city;
    private String state;
    private String pincode;
    private String landmark;
    private boolean isDefault;
    private double latitude;
    private double longitude;

    public Address(int addressId, String label, String houseNo, String street, String area,
                   String city, String state, String pincode, String landmark, boolean isDefault,
                   double latitude, double longitude) {
        this.addressId = addressId;
        this.label = label;
        this.houseNo = houseNo;
        this.street = street;
        this.area = area;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.landmark = landmark;
        this.isDefault = isDefault;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Constructor for new address (without ID)
    public Address(String label, String houseNo, String street, String area,
                   String city, String state, String pincode, String landmark, boolean isDefault,
                   double latitude, double longitude) {
        this(0, label, houseNo, street, area, city, state, pincode, landmark, isDefault, latitude, longitude);
    }

    // Constructor for backward compatibility (without location)
    public Address(String label, String houseNo, String street, String area,
                   String city, String state, String pincode, String landmark, boolean isDefault) {
        this(0, label, houseNo, street, area, city, state, pincode, landmark, isDefault, 0, 0);
    }

    protected Address(Parcel in) {
        addressId = in.readInt();
        label = in.readString();
        houseNo = in.readString();
        street = in.readString();
        area = in.readString();
        city = in.readString();
        state = in.readString();
        pincode = in.readString();
        landmark = in.readString();
        isDefault = in.readByte() != 0;
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public int getAddressId() {
        return addressId;
    }

    public String getLabel() {
        return label;
    }

    public String getHouseNo() {
        return houseNo;
    }

    public String getStreet() {
        return street;
    }

    public String getArea() {
        return area;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPincode() {
        return pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(houseNo);
        if (street != null && !street.isEmpty()) {
            sb.append(", ").append(street);
        }
        sb.append(", ").append(area);
        sb.append(", ").append(city);
        sb.append(", ").append(state);
        sb.append(" - ").append(pincode);
        if (landmark != null && !landmark.isEmpty()) {
            sb.append("\nLandmark: ").append(landmark);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return label + (isDefault ? " (Default)" : "") + "\n" + getFullAddress();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(addressId);
        dest.writeString(label);
        dest.writeString(houseNo);
        dest.writeString(street);
        dest.writeString(area);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(pincode);
        dest.writeString(landmark);
        dest.writeByte((byte) (isDefault ? 1 : 0));
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
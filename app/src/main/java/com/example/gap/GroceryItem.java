package com.example.gap;

import android.os.Parcel;
import android.os.Parcelable;

public class GroceryItem implements Parcelable {
    private String name;
    private double price;
    private int imageResId;
    private int quantity;

    public GroceryItem(String name, double price, int imageResId) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = 0;
    }

    protected GroceryItem(Parcel in) {
        name = in.readString();
        price = in.readDouble();
        imageResId = in.readInt();
        quantity = in.readInt();
    }

    public static final Creator<GroceryItem> CREATOR = new Creator<GroceryItem>() {
        @Override
        public GroceryItem createFromParcel(Parcel in) {
            return new GroceryItem(in);
        }

        @Override
        public GroceryItem[] newArray(int size) {
            return new GroceryItem[size];
        }
    };

    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getImageResId() { return imageResId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(price);
        dest.writeInt(imageResId);
        dest.writeInt(quantity);
    }
}

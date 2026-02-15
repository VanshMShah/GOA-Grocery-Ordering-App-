package com.example.gap;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    private final List<GroceryItem> items;
    private final double total;

    public Order(List<GroceryItem> items) {
        this.items = items;
        double sum = 0;
        for (GroceryItem item : items) {
            sum += item.getPrice() * item.getQuantity();
        }
        this.total = sum;
    }

    protected Order(Parcel in) {
        items = in.createTypedArrayList(GroceryItem.CREATOR);
        total = in.readDouble();
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public List<GroceryItem> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeDouble(total);
    }
}

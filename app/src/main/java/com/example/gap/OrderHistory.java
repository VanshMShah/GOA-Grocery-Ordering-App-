package com.example.gap;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class OrderHistory implements Parcelable {
    private int orderId;
    private String orderDate;
    private double total;
    private String deliveryAddress;
    private String paymentMethod;
    private List<GroceryItem> items;

    public OrderHistory(int orderId, String orderDate, double total, String deliveryAddress,
                        String paymentMethod, List<GroceryItem> items) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.total = total;
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.items = items;
    }

    protected OrderHistory(Parcel in) {
        orderId = in.readInt();
        orderDate = in.readString();
        total = in.readDouble();
        deliveryAddress = in.readString();
        paymentMethod = in.readString();
        items = in.createTypedArrayList(GroceryItem.CREATOR);
    }

    public static final Creator<OrderHistory> CREATOR = new Creator<OrderHistory>() {
        @Override
        public OrderHistory createFromParcel(Parcel in) {
            return new OrderHistory(in);
        }

        @Override
        public OrderHistory[] newArray(int size) {
            return new OrderHistory[size];
        }
    };

    public int getOrderId() {
        return orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public double getTotal() {
        return total;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public List<GroceryItem> getItems() {
        return items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderId);
        dest.writeString(orderDate);
        dest.writeDouble(total);
        dest.writeString(deliveryAddress);
        dest.writeString(paymentMethod);
        dest.writeTypedList(items);
    }
}
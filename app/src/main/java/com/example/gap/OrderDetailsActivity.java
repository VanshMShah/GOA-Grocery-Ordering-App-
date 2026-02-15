package com.example.gap;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class OrderDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Try to get OrderHistory first
        OrderHistory orderHistory = getIntent().getParcelableExtra("orderHistory");
        Order order = getIntent().getParcelableExtra("order");

        ListView orderItemsListView = findViewById(R.id.orderItemsListView);
        TextView totalOrderPriceTextView = findViewById(R.id.totalOrderPriceTextView);
        TextView orderDetailsTitle = findViewById(R.id.orderDetailsTitle);

        if (orderHistory != null) {
            // Display order history details
            orderDetailsTitle.setText("Order #" + orderHistory.getOrderId() + " Details");

            OrderItemsAdapter adapter = new OrderItemsAdapter(orderHistory.getItems());
            orderItemsListView.setAdapter(adapter);

            String details = "Total: ₹" + orderHistory.getTotal() +
                    "\n\nDate: " + orderHistory.getOrderDate() +
                    "\nPayment Method: " + orderHistory.getPaymentMethod() +
                    "\n\nDelivery Address:\n" + orderHistory.getDeliveryAddress();
            totalOrderPriceTextView.setText(details);
        } else if (order != null) {
            // Fallback to regular order
            OrderItemsAdapter adapter = new OrderItemsAdapter(order.getItems());
            orderItemsListView.setAdapter(adapter);
            totalOrderPriceTextView.setText("Total: ₹" + order.getTotal());
        }
    }

    class OrderItemsAdapter extends BaseAdapter {
        private final List<GroceryItem> items;

        public OrderItemsAdapter(List<GroceryItem> items) {
            this.items = items;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
            }

            GroceryItem item = items.get(position);
            TextView text1 = convertView.findViewById(android.R.id.text1);
            TextView text2 = convertView.findViewById(android.R.id.text2);

            text1.setText(item.getName());
            text2.setText("Price: ₹" + item.getPrice() + " | Quantity: " + item.getQuantity() +
                    " | Subtotal: ₹" + (item.getPrice() * item.getQuantity()));

            return convertView;
        }
    }
}
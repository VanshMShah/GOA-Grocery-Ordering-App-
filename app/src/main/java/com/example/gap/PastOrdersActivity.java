package com.example.gap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PastOrdersActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Customer customer;
    private List<OrderHistory> orderHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_orders);

        dbHelper = new DatabaseHelper(this);
        customer = getIntent().getParcelableExtra("customer");

        ListView pastOrdersListView = findViewById(R.id.pastOrdersListView);

        if (customer != null) {
            // Load orders from database
            orderHistories = dbHelper.getCustomerOrders(customer.getCustomerId());

            if (orderHistories != null && !orderHistories.isEmpty()) {
                PastOrdersAdapter adapter = new PastOrdersAdapter(orderHistories);
                pastOrdersListView.setAdapter(adapter);

                pastOrdersListView.setOnItemClickListener((parent, view, position, id) -> {
                    OrderHistory selectedOrder = orderHistories.get(position);
                    Intent intent = new Intent(PastOrdersActivity.this, OrderDetailsActivity.class);
                    intent.putExtra("orderHistory", selectedOrder);
                    startActivity(intent);
                });
            } else {
                Toast.makeText(this, "No past orders found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Customer information not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class PastOrdersAdapter extends BaseAdapter {
        private final List<OrderHistory> orders;

        public PastOrdersAdapter(List<OrderHistory> orders) {
            this.orders = orders;
        }

        @Override
        public int getCount() {
            return orders.size();
        }

        @Override
        public Object getItem(int position) {
            return orders.get(position);
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

            OrderHistory order = orders.get(position);
            TextView text1 = convertView.findViewById(android.R.id.text1);
            TextView text2 = convertView.findViewById(android.R.id.text2);

            text1.setText("Order #" + order.getOrderId() + " - " + order.getOrderDate());
            text2.setText("Total: ₹" + order.getTotal() + " | Payment: " + order.getPaymentMethod());

            return convertView;
        }
    }
}
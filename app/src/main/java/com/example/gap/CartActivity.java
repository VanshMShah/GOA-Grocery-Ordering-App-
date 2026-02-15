package com.example.gap;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private static final int ADDRESS_REQUEST_CODE = 100;
    private Customer customer;
    private DatabaseHelper dbHelper;
    private Address selectedAddress;
    private TextView tvDeliveryAddress;
    private ArrayList<GroceryItem> cartList;
    private String selectedPaymentMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ListView listView = findViewById(R.id.listViewCart);
        TextView totalText = findViewById(R.id.txtTotal);
        Button placeOrderBtn = findViewById(R.id.btnPlaceOrder);
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress);
        Button btnChangeAddress = findViewById(R.id.btnChangeAddress);

        cartList = getIntent().getParcelableArrayListExtra("cartList");
        customer = getIntent().getParcelableExtra("customer");

        if (cartList == null || cartList.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load default address
        if (customer != null) {
            selectedAddress = dbHelper.getDefaultAddress(customer.getCustomerId());
            updateAddressDisplay();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        double total = 0;
        for (GroceryItem g : cartList) {
            adapter.add(g.getName() + " x" + g.getQuantity() + " = ₹" + (g.getQuantity() * g.getPrice()));
            total += g.getQuantity() * g.getPrice();
        }
        listView.setAdapter(adapter);
        totalText.setText("Total: ₹" + total);

        btnChangeAddress.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, AddressManagementActivity.class);
            intent.putExtra("customer", customer);
            intent.putExtra("fromCart", true);
            startActivityForResult(intent, ADDRESS_REQUEST_CODE);
        });

        placeOrderBtn.setOnClickListener(v -> {
            if (selectedAddress == null) {
                Toast.makeText(this, "Please select a delivery address", Toast.LENGTH_SHORT).show();
                return;
            }
            showPaymentDialog();
        });
    }

    private void showPaymentDialog() {
        String[] paymentMethods = {"UPI", "Credit Card", "Cash on Delivery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Payment Method");
        builder.setItems(paymentMethods, (dialog, which) -> {
            selectedPaymentMethod = paymentMethods[which];
            placeOrder();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void placeOrder() {
        Order order = new Order(cartList);

        // Save order to database
        long orderId = dbHelper.saveOrder(
                customer.getCustomerId(),
                order,
                selectedAddress.toString(),
                selectedPaymentMethod
        );

        if (orderId > 0) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("order", order);
            setResult(RESULT_OK, resultIntent);

            Toast.makeText(this,
                    "Order placed successfully!\nPayment: " + selectedPaymentMethod +
                            "\nDelivering to: " + selectedAddress.getLabel(),
                    Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAddressDisplay() {
        if (selectedAddress != null) {
            tvDeliveryAddress.setText("Deliver to:\n" + selectedAddress.toString());
        } else {
            tvDeliveryAddress.setText("No address selected\nPlease add an address");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADDRESS_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            selectedAddress = data.getParcelableExtra("selectedAddress");
            updateAddressDisplay();
            Toast.makeText(this, "Address selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_past_orders_cart) {
            Intent intent = new Intent(CartActivity.this, PastOrdersActivity.class);
            intent.putExtra("customer", customer);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
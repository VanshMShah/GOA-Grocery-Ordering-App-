package com.example.gap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

public class AddressManagementActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Customer customer;
    private List<Address> addresses;
    private ListView listView;
    private AddressAdapter adapter;
    private Address selectedAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_management);

        dbHelper = new DatabaseHelper(this);
        customer = getIntent().getParcelableExtra("customer");
        boolean isFromCart = getIntent().getBooleanExtra("fromCart", false);

        Toolbar toolbar = findViewById(R.id.toolbarAddress);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        listView = findViewById(R.id.listViewAddresses);
        Button btnAddNew = findViewById(R.id.btnAddNewAddress);
        Button btnSelectAddress = findViewById(R.id.btnSelectAddress);

        if (isFromCart) {
            btnSelectAddress.setVisibility(View.VISIBLE);
        } else {
            btnSelectAddress.setVisibility(View.GONE);
        }

        loadAddresses();

        btnAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(AddressManagementActivity.this, AddAddressActivity.class);
            intent.putExtra("customer", customer);
            startActivity(intent);
        });

        btnSelectAddress.setOnClickListener(v -> {
            if (selectedAddress != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedAddress", selectedAddress);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "Please select an address", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAddresses();
    }

    private void loadAddresses() {
        addresses = dbHelper.getCustomerAddresses(customer.getCustomerId());
        if (addresses.isEmpty()) {
            Toast.makeText(this, "No addresses found. Please add one.", Toast.LENGTH_LONG).show();
        }
        adapter = new AddressAdapter();
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    class AddressAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return addresses.size();
        }

        @Override
        public Object getItem(int position) {
            return addresses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_address, parent, false);
            }

            Address address = addresses.get(position);

            RadioButton rbSelect = convertView.findViewById(R.id.rbSelectAddress);
            TextView tvLabel = convertView.findViewById(R.id.tvAddressLabel);
            TextView tvAddress = convertView.findViewById(R.id.tvAddressDetails);
            Button btnSetDefault = convertView.findViewById(R.id.btnSetDefault);
            Button btnDelete = convertView.findViewById(R.id.btnDeleteAddress);

            tvLabel.setText(address.getLabel() + (address.isDefault() ? " (Default)" : ""));
            tvAddress.setText(address.getFullAddress());

            if (address.isDefault()) {
                btnSetDefault.setEnabled(false);
                btnSetDefault.setText("Default");
                rbSelect.setChecked(true);
                selectedAddress = address;
            } else {
                btnSetDefault.setEnabled(true);
                btnSetDefault.setText("Set as Default");
            }

            rbSelect.setOnClickListener(v -> {
                selectedAddress = address;
                notifyDataSetChanged();
            });

            btnSetDefault.setOnClickListener(v -> {
                dbHelper.setDefaultAddress(customer.getCustomerId(), address.getAddressId());
                Toast.makeText(AddressManagementActivity.this, "Default address updated", Toast.LENGTH_SHORT).show();
                loadAddresses();
            });

            btnDelete.setOnClickListener(v -> {
                if (address.isDefault() && addresses.size() > 1) {
                    Toast.makeText(AddressManagementActivity.this, "Cannot delete default address. Set another as default first.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (addresses.size() == 1) {
                    Toast.makeText(AddressManagementActivity.this, "You must have at least one address", Toast.LENGTH_SHORT).show();
                    return;
                }

                new AlertDialog.Builder(AddressManagementActivity.this)
                        .setTitle("Delete Address")
                        .setMessage("Are you sure you want to delete this address?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            dbHelper.deleteAddress(address.getAddressId());
                            Toast.makeText(AddressManagementActivity.this, "Address deleted", Toast.LENGTH_SHORT).show();
                            loadAddresses();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });

            return convertView;
        }
    }
}